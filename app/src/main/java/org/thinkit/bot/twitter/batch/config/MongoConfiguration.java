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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.thinkit.bot.twitter.batch.catalog.MongoDatabase;
import org.thinkit.bot.twitter.batch.data.mongo.repository.ActionRecordRepository;
import org.thinkit.bot.twitter.batch.data.mongo.repository.AuthorizationTokenRepository;
import org.thinkit.bot.twitter.batch.data.mongo.repository.ErrorRepository;
import org.thinkit.bot.twitter.batch.data.mongo.repository.LastActionRepository;
import org.thinkit.bot.twitter.batch.data.mongo.repository.TaskExecutionControlRepository;
import org.thinkit.bot.twitter.batch.data.mongo.repository.TweetTextRepository;
import org.thinkit.bot.twitter.batch.data.mongo.repository.VariableRepository;
import org.thinkit.bot.twitter.batch.dto.MongoCollections;

/**
 * The configuration class for MongoDB.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@Configuration
public class MongoConfiguration extends AbstractMongoClientConfiguration {

    /**
     * The authorization token repository
     */
    @Autowired
    private AuthorizationTokenRepository authorizationTokenRepository;

    /**
     * The action record repository
     */
    @Autowired
    private ActionRecordRepository actionRecordRepository;

    /**
     * The error repository
     */
    @Autowired
    private ErrorRepository errorRepository;

    /**
     * The last action repository
     */
    @Autowired
    private LastActionRepository lastActionRepository;

    /**
     * The task execution control repository
     */
    @Autowired
    private TaskExecutionControlRepository taskExecutionControlRepository;

    /**
     * The variable repository
     */
    @Autowired
    private VariableRepository variableRepository;

    /**
     * The tweet text repository
     */
    @Autowired
    private TweetTextRepository tweetTextRepository;

    @Override
    protected String getDatabaseName() {
        return MongoDatabase.TWITTER.getTag();
    }

    /**
     * The bean that returns the mongo collections.
     *
     * @return The mongo collections.
     */
    @Bean
    public MongoCollections mongoCollections() {
        final MongoCollections.MongoCollectionsBuilder mongoCollectionsBuilder = MongoCollections.builder();
        mongoCollectionsBuilder.authorizationTokenRepository(this.authorizationTokenRepository);
        mongoCollectionsBuilder.actionRecordRepository(this.actionRecordRepository);
        mongoCollectionsBuilder.errorRepository(this.errorRepository);
        mongoCollectionsBuilder.lastActionRepository(this.lastActionRepository);
        mongoCollectionsBuilder.taskExecutionControlRepository(this.taskExecutionControlRepository);
        mongoCollectionsBuilder.variableRepository(this.variableRepository);
        mongoCollectionsBuilder.tweetTextRepository(this.tweetTextRepository);

        return mongoCollectionsBuilder.build();
    }
}
