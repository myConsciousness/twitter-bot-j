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

package org.thinkit.bot.twitter.command;

import java.util.List;

import org.thinkit.bot.twitter.catalog.ActionStatus;
import org.thinkit.bot.twitter.param.Tweet;
import org.thinkit.bot.twitter.result.AutoTweetResult;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import twitter4j.TwitterException;

/**
 * The class that manages auto tweet command
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "from")
public final class AutoTweetCommand extends AbstractBotCommand<AutoTweetResult> {

    /**
     * The tweet
     */
    private Tweet tweet;

    @Override
    protected AutoTweetResult executeBotProcess() {

        final AutoTweetResult.AutoTweetResultBuilder autoTweetResultBuilder = AutoTweetResult.newBuilder();
        autoTweetResultBuilder.setTweet(this.tweet);

        try {
            autoTweetResultBuilder.setStatus(super.getTwitter().updateStatus(tweet.getText()));
        } catch (TwitterException e) {
            autoTweetResultBuilder.setActionStatus(ActionStatus.INTERRUPTED);
            autoTweetResultBuilder.setActionErrors(List.of(super.getActionError(e)));
        }

        return autoTweetResultBuilder.build();
    }
}
