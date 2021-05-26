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

package org.thinkit.bot.twitter.batch.config;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thinkit.bot.twitter.batch.catalog.BatchStep;
import org.thinkit.bot.twitter.batch.dto.BatchStepCollections;
import org.thinkit.bot.twitter.batch.tasklet.ExecuteAutoShowUserTasklet;
import org.thinkit.bot.twitter.batch.tasklet.ExecuteAutoTweetGreetingTasklet;

/**
 * The class that manages the batch step configuration of Twitter bot command.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@Configuration
public class BatchStepConfiguration {

    /**
     * The step builder factory
     */
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    /**
     * The execute auto tweet greeting taklet
     */
    @Autowired
    private Tasklet executeAutoTweetGreetingTasklet;

    /**
     * The execute auto show user tasklet
     */
    @Autowired
    private Tasklet executeAutoShowUserTasklet;

    /**
     * Registers the instance of {@link BatchStepCollections} as bean.
     *
     * @return The instance of {@link BatchStepCollections}
     */
    @Bean
    public BatchStepCollections batchStepCollections() {
        final BatchStepCollections.BatchStepCollectionsBuilder batchStepCollectionsBuilder = BatchStepCollections
                .builder();
        batchStepCollectionsBuilder.executeAutoTweetGreetingStep(this.executeAutoTweetGreetingStep());
        batchStepCollectionsBuilder.executeAutoShowUserStep(this.executeAutoShowUserStep());

        return batchStepCollectionsBuilder.build();
    }

    /**
     * Returns the step of {@link ExecuteAutoTweetGreetingTasklet} .
     *
     * @return The step of {@link ExecuteAutoTweetGreetingTasklet}
     */
    private Step executeAutoTweetGreetingStep() {
        return this.stepBuilderFactory.get(BatchStep.AUTO_TWEET_GREETING.getTag())
                .tasklet(this.executeAutoTweetGreetingTasklet).build();
    }

    /**
     * Returns the step of {@link ExecuteAutoShowUserTasklet} .
     *
     * @return The step of {@link ExecuteAutoShowUserTasklet}
     */
    private Step executeAutoShowUserStep() {
        return this.stepBuilderFactory.get(BatchStep.AUTO_SHOW_USER.getTag()).tasklet(this.executeAutoShowUserTasklet)
                .build();
    }
}
