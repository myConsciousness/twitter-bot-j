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
import org.thinkit.bot.twitter.batch.tasklet.ExecuteAutoTweetGoodMorningTasklet;

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
     * The execute auto tweet good morning taklet
     */
    @Autowired
    private Tasklet executeAutoTweetGoodMorningTasklet;

    /**
     * Registers the instance of {@link BatchStepCollections} as bean.
     *
     * @return The instance of {@link BatchStepCollections}
     */
    @Bean
    public BatchStepCollections batchStepCollections() {
        final BatchStepCollections.BatchStepCollectionsBuilder batchStepCollectionsBuilder = BatchStepCollections
                .builder();
        batchStepCollectionsBuilder.executeAutoTweetGoodMorningStep(this.executeAutoTweetGoodMorningStep());

        return batchStepCollectionsBuilder.build();
    }

    /**
     * Returns the step of {@link ExecuteAutoTweetGoodMorningTasklet} .
     *
     * @return The step of {@link ExecuteAutoTweetGoodMorningTasklet}
     */
    private Step executeAutoTweetGoodMorningStep() {
        return this.stepBuilderFactory.get(BatchStep.AUTO_TWEET_GOOD_MORNING.getTag())
                .tasklet(this.executeAutoTweetGoodMorningTasklet).build();
    }
}
