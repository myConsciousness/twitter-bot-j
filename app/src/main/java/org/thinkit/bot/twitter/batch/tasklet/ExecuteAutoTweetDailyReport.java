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

import com.mongodb.lang.NonNull;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.stereotype.Component;
import org.thinkit.bot.twitter.batch.catalog.TaskType;
import org.thinkit.bot.twitter.batch.catalog.TweetTextPattern;
import org.thinkit.bot.twitter.batch.data.mongo.entity.TweetText;
import org.thinkit.bot.twitter.batch.data.mongo.entity.UserProfile;
import org.thinkit.bot.twitter.batch.data.mongo.entity.UserProfileTransition;
import org.thinkit.bot.twitter.batch.result.BatchTaskResult;
import org.thinkit.bot.twitter.catalog.ActionStatus;
import org.thinkit.bot.twitter.param.Tweet;
import org.thinkit.bot.twitter.util.UserProfileDifference;

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

        final UserProfileTransition userProfileTransition = this.getLatestUserProfileTransition();

        if (userProfileTransition == null) {
            // When there is no comparison.
            return BatchTaskResult.builder().actionStatus(ActionStatus.SKIP).build();
        }

        final UserProfile userProfile = super.getMongoCollections().getUserProfileRepository()
                .findByName(super.getRunningUser().getName());
        final UserProfileDifference userProfileDifference = this.getUserProfileDifference(userProfile,
                userProfileTransition);

        for (final TweetText tweetText : this.getTweetTexts()) {
            super.getTwitterBot().executeAutoTweet(Tweet.from(tweetText.getText()));
        }

        log.debug("END");
        return BatchTaskResult.builder().build();
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

    private List<TweetText> getTweetTexts() {
        return super.getMongoCollections().getTweetTextRepository()
                .findByTextCode(TweetTextPattern.DAILY_REPORT.getCode());

    }
}
