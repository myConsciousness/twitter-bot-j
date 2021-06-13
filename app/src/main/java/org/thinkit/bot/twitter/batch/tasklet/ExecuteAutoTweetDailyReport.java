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
import org.thinkit.bot.twitter.batch.catalog.TweetTextPattern;
import org.thinkit.bot.twitter.batch.catalog.TweetType;
import org.thinkit.bot.twitter.batch.data.mongo.entity.TweetResult;
import org.thinkit.bot.twitter.batch.data.mongo.entity.UserProfile;
import org.thinkit.bot.twitter.batch.data.mongo.entity.UserProfileTransition;
import org.thinkit.bot.twitter.batch.data.mongo.repository.TweetResultRepository;
import org.thinkit.bot.twitter.batch.dto.MongoCollections;
import org.thinkit.bot.twitter.batch.report.DailyReportBuilder;
import org.thinkit.bot.twitter.batch.result.BatchTaskResult;
import org.thinkit.bot.twitter.catalog.ActionStatus;
import org.thinkit.bot.twitter.param.Tweet;
import org.thinkit.bot.twitter.result.ActionError;
import org.thinkit.bot.twitter.result.AutoTweetResult;
import org.thinkit.bot.twitter.util.UserProfileDifference;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
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

        final UserProfileTransition userProfileTransition = this.getLatestUserProfileTransition();

        if (userProfileTransition == null) {
            // When there is no comparison.
            return BatchTaskResult.builder().actionStatus(ActionStatus.SKIP).build();
        }

        final MongoCollections mongoCollections = super.getMongoCollections();
        final UserProfile userProfile = mongoCollections.getUserProfileRepository()
                .findByScreenName(super.getRunningUser().getName());
        final UserProfileDifference userProfileDifference = this.getUserProfileDifference(userProfile,
                userProfileTransition);

        final TweetResultRepository tweetResultRepository = mongoCollections.getTweetResultRepository();
        final List<ActionError> actionErrors = new ArrayList<>();

        for (final Language language : Language.values()) {
            final AutoTweetResult autoTweetResult = super.getTwitterBot()
                    .executeAutoTweet(Tweet.from(this.getDailyReport(language, userProfileDifference)));

            TweetResult tweetResult = new TweetResult();
            tweetResult.setTextCode(TweetTextPattern.DAILY_REPORT.getCode());
            tweetResult.setTypeCode(TweetType.REPORT.getCode());
            tweetResult.setLanguageCode(language.getCode());
            tweetResult.setTweet(autoTweetResult.getTweet().getText());
            tweetResult.setStatus(autoTweetResult.getStatus());

            tweetResult = tweetResultRepository.insert(tweetResult);
            log.debug("Inserted tweet result: {}", tweetResult);

            if (autoTweetResult.getActionErrors() != null) {
                for (final ActionError actionError : autoTweetResult.getActionErrors()) {
                    actionErrors.add(actionError);
                }
            }
        }

        final BatchTaskResult.BatchTaskResultBuilder batchTaskResultBuilder = BatchTaskResult.builder();
        batchTaskResultBuilder.actionCount(1);
        batchTaskResultBuilder.resultCount(1);
        batchTaskResultBuilder.actionErrors(actionErrors);

        log.debug("END");
        return batchTaskResultBuilder.build();
    }

    private UserProfileTransition getLatestUserProfileTransition() {
        log.debug("START");

        final List<UserProfileTransition> userProfileTransitions = super.getMongoCollections()
                .getUserProfileTransitionRepository().findAll();

        if (userProfileTransitions.isEmpty()) {
            return null;
        }

        log.debug("END");
        return userProfileTransitions.get(0);
    }

    private UserProfileDifference getUserProfileDifference(@NonNull final UserProfile userProfile,
            @NonNull final UserProfileTransition userProfileTransition) {
        return UserProfileDifference.newBuilder().userProfile(userProfile).userProfileTransition(userProfileTransition)
                .build();
    }

    private String getDailyReport(@NonNull final Language language,
            @NonNull final UserProfileDifference userProfileDifference) {
        return DailyReportBuilder.from(language, userProfileDifference).build().getText();
    }
}
