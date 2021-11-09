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

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.thinkit.bot.twitter.TwitterBot;
import org.thinkit.bot.twitter.TwitterBotJ;
import org.thinkit.bot.twitter.batch.catalog.BatchJob;
import org.thinkit.bot.twitter.batch.catalog.ScheduleType;
import org.thinkit.bot.twitter.batch.context.flow.JobFlowContext;
import org.thinkit.bot.twitter.batch.dto.BatchStepCollections;

import lombok.NonNull;

/**
 * The class that manages the batch job configuration of Twitter bot.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@Configuration
@EnableScheduling
public class BatchJobConfiguration {

    /**
     * The schedule cron for tweet greeting
     */
    private static final String SCHEDULE_CRON_TWEET_GREETING = "${spring.batch.schedule.cron.tweet.greeting}";

    /**
     * The schedule cron for tweet report
     */
    private static final String SCHEDULE_CRON_TWEET_REPORT = "${spring.batch.schedule.cron.tweet.report}";

    /**
     * The schedule cron for tweet report
     */
    private static final String SCHEDULE_CRON_TWEET_INTRODUCE = "${spring.batch.schedule.cron.tweet.introduce}";

    /**
     * The schedule cron for tweet PR
     */
    private static final String SCHEDULE_CRON_TWEET_PR = "${spring.batch.schedule.cron.tweet.pr}";

    /**
     * The schedule cron for search
     */
    private static final String SCHEDULE_CRON_SEARCH = "${spring.batch.schedule.cron.search}";

    /**
     * The schedule cron for close session
     */
    private static final String SCHEDULE_CRON_CLOSE_SESSION = "${spring.batch.schedule.cron.session.close}";

    /**
     * The timezone
     */
    private static final String TIME_ZONE = "${spring.batch.schedule.timezone}";

    /**
     * The twitter configuration
     */
    @Autowired
    private twitter4j.conf.Configuration configuration;

    /**
     * The job builder factory
     */
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    /**
     * The job launcher
     */
    @Autowired
    private SimpleJobLauncher simpleJobLauncher;

    /**
     * The batch step collections
     */
    @Autowired
    private BatchStepCollections batchStepCollections;

    /**
     * Registers the instance of {@link TwitterBot} as bean.
     *
     * @return The instance of {@link TwitterBot}
     */
    @Bean
    public TwitterBot twitterBot() {
        return TwitterBotJ.from(configuration);
    }

    @Scheduled(cron = SCHEDULE_CRON_TWEET_GREETING, zone = TIME_ZONE)
    public void performScheduledTweetGreeting() throws Exception {
        this.runJobLauncher(ScheduleType.TWEET_GREETING);
    }

    @Scheduled(cron = SCHEDULE_CRON_TWEET_REPORT, zone = TIME_ZONE)
    public void performScheduledTweetDailyReport() throws Exception {
        this.runJobLauncher(ScheduleType.TWEET_REPORT);
    }

    @Scheduled(cron = SCHEDULE_CRON_TWEET_INTRODUCE, zone = TIME_ZONE)
    public void performScheduledTweetIntroduce() throws Exception {
        this.runJobLauncher(ScheduleType.TWEET_INTRODUCE);
    }

    @Scheduled(cron = SCHEDULE_CRON_TWEET_PR, zone = TIME_ZONE)
    public void performScheduledTweetPr() throws Exception {
        this.runJobLauncher(ScheduleType.TWEET_PR);
    }

    @Scheduled(cron = SCHEDULE_CRON_SEARCH, zone = TIME_ZONE)
    public void performScheduledSearch() throws Exception {
        this.runJobLauncher(ScheduleType.SEARCH);
    }

    @Scheduled(cron = SCHEDULE_CRON_CLOSE_SESSION, zone = TIME_ZONE)
    public void performScheduledSessionClose() throws Exception {
        this.runJobLauncher(ScheduleType.CLOSE_SESSION);
    }

    private void runJobLauncher(@NonNull final ScheduleType scheduleType) throws Exception {
        final JobParameters param = new JobParametersBuilder()
                .addString(BatchJob.TWITTER_BOT.getTag(), String.valueOf(System.currentTimeMillis())).toJobParameters();

        this.simpleJobLauncher.run(this.createJob(scheduleType), param);
    }

    private Job createJob(@NonNull final ScheduleType scheduleType) {
        return JobFlowContext.from(scheduleType, this.getTwitterBotJobBuilder(), this.batchStepCollections).evaluate();
    }

    private JobBuilder getTwitterBotJobBuilder() {
        return this.jobBuilderFactory.get(BatchJob.TWITTER_BOT.getTag());
    }
}
