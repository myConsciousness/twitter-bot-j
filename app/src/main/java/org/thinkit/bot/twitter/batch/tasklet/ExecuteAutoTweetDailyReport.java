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

import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.stereotype.Component;
import org.thinkit.bot.twitter.batch.catalog.TaskType;
import org.thinkit.bot.twitter.batch.catalog.TweetTextPattern;
import org.thinkit.bot.twitter.batch.data.mongo.entity.TweetText;
import org.thinkit.bot.twitter.batch.data.mongo.repository.TweetTextRepository;
import org.thinkit.bot.twitter.batch.data.mongo.repository.UserProfileRepository;
import org.thinkit.bot.twitter.batch.data.mongo.repository.UserProfileTransitionRepository;
import org.thinkit.bot.twitter.batch.dto.MongoCollections;
import org.thinkit.bot.twitter.batch.result.BatchTaskResult;
import org.thinkit.bot.twitter.param.Tweet;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * The class that manages the auto tweet daily report.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@Slf4j
@ToString
@EqualsAndHashCode(callSuper = false)
@Component
public final class ExecuteAutoTweetDailyReport extends AbstractTasklet {

    /**
     * The default constructor.
     */
    private ExecuteAutoTweetDailyReport() {
        super(TaskType.AUTO_TWEET_DAILY_REPORT);
    }

    /**
     * Returns the new instance of {@link ExecuteAutoTweetDailyReport}
     *
     * @return The new instance of {@link ExecuteAutoTweetDailyReport}
     */
    public static Tasklet newInstance() {
        return new ExecuteAutoTweetDailyReport();
    }

    @Override
    protected BatchTaskResult executeTask(StepContribution contribution, ChunkContext chunkContext) {
        log.debug("STRAT");

        final MongoCollections mongoCollections = super.getMongoCollections();
        final TweetTextRepository tweetTextRepository = mongoCollections.getTweetTextRepository();
        final UserProfileRepository userProfileRepository = mongoCollections.getUserProfileRepository();
        final UserProfileTransitionRepository userProfileTransitionRepository = mongoCollections
                .getUserProfileTransitionRepository();

        final List<TweetText> tweetTexts = tweetTextRepository.findByTextCode(TweetTextPattern.DAILY_REPORT.getCode());

        for (final TweetText tweetText : tweetTexts) {
            super.getTwitterBot().executeAutoTweet(Tweet.from(tweetText.getText()));
        }

        log.debug("END");
        return BatchTaskResult.builder().build();
    }
}
