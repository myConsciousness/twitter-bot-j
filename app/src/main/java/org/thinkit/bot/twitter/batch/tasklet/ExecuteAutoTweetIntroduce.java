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

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.stereotype.Component;
import org.thinkit.bot.twitter.batch.catalog.Language;
import org.thinkit.bot.twitter.batch.catalog.TaskType;
import org.thinkit.bot.twitter.batch.catalog.TweetType;
import org.thinkit.bot.twitter.batch.data.mongo.entity.TweetResult;
import org.thinkit.bot.twitter.batch.data.mongo.entity.TweetText;
import org.thinkit.bot.twitter.batch.data.mongo.repository.TweetResultRepository;
import org.thinkit.bot.twitter.batch.data.mongo.repository.TweetTextRepository;
import org.thinkit.bot.twitter.batch.dto.MongoCollections;
import org.thinkit.bot.twitter.batch.result.BatchTaskResult;
import org.thinkit.bot.twitter.param.Tweet;
import org.thinkit.bot.twitter.result.ActionError;
import org.thinkit.bot.twitter.result.AutoTweetResult;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * The class that manages the auto tweet introduce.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@Slf4j
@ToString
@EqualsAndHashCode(callSuper = false)
@Component
public final class ExecuteAutoTweetIntroduce extends AbstractTasklet {

    /**
     * The default constructor.
     */
    private ExecuteAutoTweetIntroduce() {
        super(TaskType.AUTO_TWEET_INTRODUCE);
    }

    /**
     * Returns the new instance of {@link ExecuteAutoTweetIntroduce} .
     *
     * @return The new instance of {@link ExecuteAutoTweetIntroduce}
     */
    public static Tasklet newInstance() {
        return new ExecuteAutoTweetIntroduce();
    }

    @Override
    protected BatchTaskResult executeTask(StepContribution contribution, ChunkContext chunkContext) {
        log.debug("START");

        final MongoCollections mongoCollections = super.getMongoCollections();
        final TweetTextRepository tweetTextRepository = mongoCollections.getTweetTextRepository();
        final TweetResultRepository tweetResultRepository = mongoCollections.getTweetResultRepository();
        final List<ActionError> actionErrors = new ArrayList<>();

        int actionCount = 0;
        for (final Language language : Language.values()) {
            final TweetText tweetText = tweetTextRepository.findByTypeCodeAndLanguageCode(TweetType.INTRODUCE.getCode(),
                    language.getCode());

            if (tweetText == null) {
                log.warn(String.format("Introduce tweet for language '%s' is not defined. Skip tweet in this language.",
                        language.name()));
                continue;
            }

            final AutoTweetResult autoTweetResult = super.getTwitterBot()
                    .executeAutoTweet(Tweet.from(tweetText.getText()));

            TweetResult tweetResult = new TweetResult();
            tweetResult.setTextCode(tweetText.getTextCode());
            tweetResult.setLanguageCode(tweetText.getLanguageCode());
            tweetResult.setTweet(autoTweetResult.getTweet().getText());
            tweetResult.setStatus(autoTweetResult.getStatus());

            tweetResult = tweetResultRepository.insert(tweetResult);
            log.debug("Inserted tweet result: {}", tweetResult);

            if (autoTweetResult.getActionErrors() != null) {
                for (final ActionError actionError : autoTweetResult.getActionErrors()) {
                    actionErrors.add(actionError);
                }
            }

            actionCount++;
        }

        final BatchTaskResult.BatchTaskResultBuilder batchTaskResultBuilder = BatchTaskResult.builder();
        batchTaskResultBuilder.actionCount(actionCount);
        batchTaskResultBuilder.resultCount(actionCount - actionErrors.size());
        batchTaskResultBuilder.actionErrors(actionErrors);

        log.debug("END");
        return batchTaskResultBuilder.build();
    }
}
