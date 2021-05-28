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

import com.mongodb.lang.NonNull;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.stereotype.Component;
import org.thinkit.bot.twitter.batch.catalog.TaskType;
import org.thinkit.bot.twitter.batch.data.mongo.entity.UserProfile;
import org.thinkit.bot.twitter.batch.data.mongo.entity.UserProfileTransition;
import org.thinkit.bot.twitter.batch.data.mongo.repository.UserProfileRepository;
import org.thinkit.bot.twitter.batch.dto.MongoCollections;
import org.thinkit.bot.twitter.batch.result.BatchTaskResult;
import org.thinkit.bot.twitter.result.AutoShowUserResult;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * The class that manages the auto show user.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@Slf4j
@ToString
@EqualsAndHashCode(callSuper = false)
@Component
public final class ExecuteAutoShowUserTasklet extends AbstractTasklet {

    /**
     * The default constructor.
     */
    private ExecuteAutoShowUserTasklet() {
        super(TaskType.AUTO_SHOW_USER);
    }

    /**
     * Returns the new instance of {@link ExecuteAutoShowUserTasklet} .
     *
     * @return The new instance of {@link ExecuteAutoShowUserTasklet}
     */
    public static Tasklet newInstance() {
        return new ExecuteAutoShowUserTasklet();
    }

    @Override
    protected BatchTaskResult executeTask(StepContribution contribution, ChunkContext chunkContext) {
        log.debug("START");

        final AutoShowUserResult autoShowUserResult = super.getTwitterBot()
                .executeAutoShowUser(super.getRunningUser().getName());

        final MongoCollections mongoCollections = super.getMongoCollections();
        final UserProfileRepository userProfileRepository = mongoCollections.getUserProfileRepository();

        UserProfile userProfile = userProfileRepository.findByUserId(autoShowUserResult.getUserId());

        if (userProfile != null) {
            this.recordUserProfileAsTransition(userProfile);
            this.updateUserProfile(autoShowUserResult, userProfile);
        } else {
            // Enter here only on first launch.
            this.insertUserProfile(autoShowUserResult);
        }

        final BatchTaskResult.BatchTaskResultBuilder batchTaskResultBuilder = BatchTaskResult.builder();
        batchTaskResultBuilder.actionStatus(autoShowUserResult.getActionStatus());
        batchTaskResultBuilder.actionCount(1);
        batchTaskResultBuilder.resultCount(1);
        batchTaskResultBuilder.actionErrors(autoShowUserResult.getActionErrors());

        log.debug("END");
        return batchTaskResultBuilder.build();
    }

    private void recordUserProfileAsTransition(@NonNull final UserProfile userProfile) {
        log.debug("START");

        UserProfileTransition userProfileTransition = new UserProfileTransition();
        userProfileTransition.setUserId(userProfile.getUserId());
        userProfileTransition.setName(userProfile.getName());
        userProfileTransition.setFollowersCount(userProfile.getFollowersCount());
        userProfileTransition.setFollowingsCount(userProfile.getFollowingsCount());
        userProfileTransition.setRecordedAt(userProfile.getUpdatedAt());

        userProfileTransition = super.getMongoCollections().getUserProfileTransitionRepository()
                .insert(userProfileTransition);
        log.debug("Inserted user profile transition: {}", userProfileTransition);

        log.debug("END");
    }

    private void updateUserProfile(@NonNull final AutoShowUserResult autoShowUserResult,
            @NonNull final UserProfile userProfile) {
        log.debug("START");

        if (!userProfile.getName().equals(autoShowUserResult.getUserName())) {
            // When the screen name has changed.
            userProfile.setName(autoShowUserResult.getUserName());
        }

        userProfile.setFollowersCount(autoShowUserResult.getFollowersCount());
        userProfile.setFollowingsCount(autoShowUserResult.getFollowingsCount());
        userProfile.setUpdatedAt(new Date());

        super.getMongoCollections().getUserProfileRepository().save(userProfile);
        log.debug("Updated user profile: {}", userProfile);

        log.debug("END");
    }

    private void insertUserProfile(@NonNull final AutoShowUserResult autoShowUserResult) {
        log.debug("START");

        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(autoShowUserResult.getUserId());
        userProfile.setName(autoShowUserResult.getUserName());
        userProfile.setFollowersCount(autoShowUserResult.getFollowersCount());
        userProfile.setFollowingsCount(autoShowUserResult.getFollowingsCount());

        userProfile = super.getMongoCollections().getUserProfileRepository().insert(userProfile);
        log.debug("Inserted user profile: {}", userProfile);

        log.debug("END");
    }
}