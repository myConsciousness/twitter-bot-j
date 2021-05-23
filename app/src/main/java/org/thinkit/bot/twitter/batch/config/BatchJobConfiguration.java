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

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.thinkit.bot.twitter.TwitterBot;
import org.thinkit.bot.twitter.TwitterBotJ;
import org.thinkit.bot.twitter.batch.dto.BatchStepCollections;
import org.thinkit.bot.twitter.batch.dto.MongoCollections;

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
     * The schedule cron
     */
    private static final String SCHEDULE_CRON = "${spring.batch.schedule.cron}";

    /**
     * The timezone
     */
    private static final String TIME_ZONE = "${spring.batch.schedule.timezone}";

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
     * The mongo collections
     */
    @Autowired
    private MongoCollections mongoCollections;

    @Bean
    public TwitterBot twitterBot() {
        return TwitterBotJ.newInstance();
    }

    @Scheduled(cron = SCHEDULE_CRON, zone = TIME_ZONE)
    public void performScheduledInitializeSession() throws Exception {
    }
}
