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

package org.thinkit.bot.twitter.batch.context.flow;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.thinkit.bot.twitter.batch.catalog.ScheduleType;
import org.thinkit.bot.twitter.batch.context.Context;
import org.thinkit.bot.twitter.batch.dto.BatchStepCollections;
import org.thinkit.bot.twitter.batch.strategy.flow.JobFlowTweetDailyReportStrategy;
import org.thinkit.bot.twitter.batch.strategy.flow.JobFlowTweetGreetingStrategy;
import org.thinkit.bot.twitter.batch.strategy.flow.JobFlowTweetIntroduceStrategy;
import org.thinkit.bot.twitter.batch.strategy.flow.JobFlowTweetPrStrategy;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "from")
public final class JobFlowContext implements Context<Job> {

    /**
     * The schedule type
     */
    private ScheduleType scheduleType;

    /**
     * The job builder
     */
    private JobBuilder jobBuilder;

    /**
     * The batch step collections
     */
    private BatchStepCollections batchStepCollections;

    @Override
    public Job evaluate() {
        return switch (this.scheduleType) {
            case TWEET_GREETING -> JobFlowTweetGreetingStrategy.from(this.jobBuilder, this.batchStepCollections)
                    .execute();
            case TWEET_DAILY_REPORT -> JobFlowTweetDailyReportStrategy.from(this.jobBuilder, this.batchStepCollections)
                    .execute();
            case TWEET_INTRODUCE -> JobFlowTweetIntroduceStrategy.from(this.jobBuilder, this.batchStepCollections)
                    .execute();
            case TWEET_PR -> JobFlowTweetPrStrategy.from(this.jobBuilder, this.batchStepCollections).execute();
        };
    }
}
