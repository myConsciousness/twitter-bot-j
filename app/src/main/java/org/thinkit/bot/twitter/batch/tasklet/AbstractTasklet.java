/*
 * Copyright 2021 Kato Shinya.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.thinkit.bot.twitter.batch.tasklet;

import java.util.Date;
import java.util.List;

import com.mongodb.lang.NonNull;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.thinkit.bot.twitter.TwitterBot;
import org.thinkit.bot.twitter.batch.catalog.TaskType;
import org.thinkit.bot.twitter.batch.catalog.VariableName;
import org.thinkit.bot.twitter.batch.data.content.mapper.DefaultTaskExecutionRuleMapper;
import org.thinkit.bot.twitter.batch.data.content.mapper.DefaultVariableMapper;
import org.thinkit.bot.twitter.batch.data.mongo.entity.ActionRecord;
import org.thinkit.bot.twitter.batch.data.mongo.entity.Error;
import org.thinkit.bot.twitter.batch.data.mongo.entity.LastAction;
import org.thinkit.bot.twitter.batch.data.mongo.entity.TaskExecutionControl;
import org.thinkit.bot.twitter.batch.data.mongo.entity.Variable;
import org.thinkit.bot.twitter.batch.data.mongo.repository.ErrorRepository;
import org.thinkit.bot.twitter.batch.data.mongo.repository.LastActionRepository;
import org.thinkit.bot.twitter.batch.data.mongo.repository.TaskExecutionControlRepository;
import org.thinkit.bot.twitter.batch.data.mongo.repository.VariableRepository;
import org.thinkit.bot.twitter.batch.dto.MongoCollections;
import org.thinkit.bot.twitter.batch.policy.BatchTask;
import org.thinkit.bot.twitter.batch.policy.RunningUser;
import org.thinkit.bot.twitter.batch.result.BatchTaskResult;
import org.thinkit.bot.twitter.catalog.ActionStatus;
import org.thinkit.bot.twitter.result.ActionError;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@Slf4j
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Component
public abstract class AbstractTasklet implements Tasklet {

    /**
     * The batch task
     */
    private final BatchTask batchTask;

    /**
     * The running user
     */
    @Autowired
    @Getter(AccessLevel.PROTECTED)
    private RunningUser runningUser;

    /**
     * The twitter bot
     */
    @Autowired
    @Getter(AccessLevel.PROTECTED)
    private TwitterBot twitterBot;

    /**
     * The configurable application context
     */
    @Autowired
    private ConfigurableApplicationContext context;

    /**
     * The mongo collections
     */
    @Autowired
    @Getter(AccessLevel.PROTECTED)
    private MongoCollections mongoCollections;

    /**
     * The constructor.
     *
     * @param taskType The task type
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    protected AbstractTasklet(@NonNull final TaskType taskType) {
        this.batchTask = BatchTask.from(taskType);
    }

    /**
     * Given the current context in the form of a step contribution, do whatever is
     * necessary to process this unit inside a transaction.
     *
     * <p>
     * Implementations return {@link RepeatStatus#FINISHED} if finished. If not they
     * return {@link RepeatStatus#CONTINUABLE}. On failure throws an exception.
     *
     * @param contribution The mutable state to be passed back to update the current
     *                     step execution
     * @param chunkContext The attributes shared between invocations but not between
     *                     restarts
     * @return The batch task result
     */
    protected abstract BatchTaskResult executeTask(StepContribution contribution, ChunkContext chunkContext);

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.debug("START");

        if (!this.isTaskActivated()) {
            log.debug("END");
            return RepeatStatus.FINISHED;
        }

        log.debug("END");
        return this.executeTaskProcess(contribution, chunkContext);
    }

    protected String getVariableValue(@NonNull final VariableName variableName) {
        return this.getVariable(variableName).getValue();
    }

    protected int getIntVariableValue(@NonNull final VariableName variableName) {
        return Integer.parseInt(this.getVariable(variableName).getValue());
    }

    /**
     * Returns the variable from {@code Variable} collection on MongoDB linked by
     * the {@code variableName} passed as an argument. If the corresponding variable
     * document does not exist in the {@code Variable} collection, it will be
     * generated with the default value.
     *
     * @param variableName The variable name
     * @return The variable linked by the {@code variableName} passed as an argument
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    protected Variable getVariable(@NonNull final VariableName variableName) {
        log.debug("START");

        final VariableRepository variableRepository = this.mongoCollections.getVariableRepository();
        Variable variable = variableRepository.findByName(variableName.getTag());

        if (variable == null) {
            variable = new Variable();
            variable.setName(variableName.getTag());
            variable.setValue(this.getDefaultVariableValue(variableName));

            variable = variableRepository.insert(variable);
            log.debug("Inserted variable: {}", variable);
        }

        log.debug("The variable: {}", variable);
        log.debug("END");
        return variable;
    }

    protected void saveVariable(@NonNull final VariableName variableName, @NonNull final Object value) {
        log.debug("START");

        final Variable variable = this.getVariable(variableName);
        variable.setValue(String.valueOf(value));
        variable.setUpdatedAt(new Date());

        this.mongoCollections.getVariableRepository().save(variable);
        log.debug("Updated variable: {}", variable);

        log.debug("END");
    }

    private RepeatStatus executeTaskProcess(StepContribution contribution, ChunkContext chunkContext) {
        log.debug("START");

        this.updateStartAction();

        final BatchTaskResult batchTaskResult = this.executeTask(contribution, chunkContext);
        log.debug("The batch task result: {}", batchTaskResult);

        final int actionCount = batchTaskResult.getActionCount();
        final ActionStatus actionStatus = batchTaskResult.getActionStatus();
        final List<ActionError> actionErrors = batchTaskResult.getActionErrors();

        this.saveActionRecord(actionCount, actionStatus);

        if (!actionErrors.isEmpty()) {
            this.saveActionError(actionErrors);
        }

        this.updateEndAction();

        if (this.batchTask.isClosable()) {
            this.closeSession();
        }

        log.debug("END");
        return batchTaskResult.getRepeatStatus();
    }

    private boolean isTaskActivated() {

        if (this.batchTask.isStartSession()) {
            // Before the initialization process, the session of the running user has not
            // been created yet, so the process of judging the effectiveness of the task
            // cannot be performed. The initialization process must be performed.
            return true;
        }

        final TaskExecutionControlRepository taskExecutionControlRepository = this.mongoCollections
                .getTaskExecutionControlRepository();
        TaskExecutionControl taskExecutionControl = taskExecutionControlRepository
                .findByTaskTypeCode(this.batchTask.getTypeCode());

        if (taskExecutionControl == null) {
            taskExecutionControl = new TaskExecutionControl();
            taskExecutionControl.setTaskTypeCode(this.batchTask.getTypeCode());
            taskExecutionControl.setActive(this.getDefaultTaskExecutionRule());

            taskExecutionControl = taskExecutionControlRepository.insert(taskExecutionControl);
            log.debug("Inserted task execution control: {}", taskExecutionControl);
        }

        return taskExecutionControl.isActive();
    }

    private void updateStartAction() {
        log.debug("START");

        final LastActionRepository lastActionRepository = this.mongoCollections.getLastActionRepository();
        LastAction lastAction = lastActionRepository.findByTaskTypeCode(this.batchTask.getTypeCode());

        if (lastAction == null) {
            lastAction = new LastAction();
            lastAction.setTaskTypeCode(this.batchTask.getTypeCode());
        }

        lastAction.setStart(new Date());
        lastAction.setEnd(null);
        lastAction.setUpdatedAt(new Date());

        lastActionRepository.save(lastAction);
        log.debug("Updated last action: {}", lastAction);

        log.debug("END");
    }

    private void updateEndAction() {
        log.debug("START");

        final LastActionRepository lastActionRepository = this.mongoCollections.getLastActionRepository();
        final LastAction lastAction = lastActionRepository.findByTaskTypeCode(this.batchTask.getTypeCode());

        lastAction.setEnd(new Date());
        lastAction.setUpdatedAt(new Date());

        lastActionRepository.save(lastAction);
        log.debug("Updated last action: {}", lastAction);

        log.debug("END");
    }

    private void saveActionRecord(final int actionCount, @NonNull final ActionStatus actionStatus) {
        log.debug("START");

        final ActionRecord actionRecord = new ActionRecord();
        actionRecord.setTaskTypeCode(this.batchTask.getTypeCode());
        actionRecord.setCount(actionCount);
        actionRecord.setActionStatusCode(actionStatus.getCode());

        this.mongoCollections.getActionRecordRepository().insert(actionRecord);
        log.debug("Inserted action record: {}", actionRecord);

        log.debug("END");
    }

    /**
     * Close the session.
     */
    private void closeSession() {
        final int exitCode = SpringApplication.exit(context, () -> 0);
        System.exit(exitCode);
    }

    private void saveActionError(@NonNull final List<ActionError> actionErrors) {
        log.debug("START");

        final ErrorRepository errorRepository = this.mongoCollections.getErrorRepository();

        for (final ActionError actionError : actionErrors) {
            final Error error = new Error();
            error.setTaskTypeCode(this.batchTask.getTypeCode());
            error.setMessage(actionError.getMessage());
            error.setLocalizedMessage(actionError.getLocalizedMessage());
            error.setStackTrace(actionError.getStackTrace());

            final Error insertedError = errorRepository.insert(error);
            log.debug("Inserted error: {}", insertedError);
        }

        log.debug("END");
    }

    private String getDefaultVariableValue(@NonNull final VariableName variableName) {
        final DefaultVariableMapper defaultVariableMapper = DefaultVariableMapper.from(variableName.getTag());
        return defaultVariableMapper.scan().get(0).getValue();
    }

    private boolean getDefaultTaskExecutionRule() {
        return DefaultTaskExecutionRuleMapper.from(this.batchTask.getTypeCode()).scan().get(0).isActive();
    }
}
