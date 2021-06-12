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

package org.thinkit.bot.twitter.result;

import com.mongodb.lang.NonNull;

import org.thinkit.bot.twitter.param.Tweet;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import twitter4j.Status;

/**
 * The class that manages the result of auto tweet command.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AutoTweetResult extends AbstractCommandResult {

    /**
     * The tweet
     */
    @Getter
    @NonNull
    private Tweet tweet;

    /**
     * The status
     */
    @Getter
    private Status status;

    /**
     * Returns the new instance of {@link AutoTweetResultBuilder} .
     *
     * @return The new instance of {@link AutoTweetResultBuilder}
     */
    public static AutoTweetResultBuilder newBuilder() {
        return new AutoTweetResultBuilder();
    }

    /**
     * @author Kato Shinya
     * @since 1.0.0
     */
    public static final class AutoTweetResultBuilder extends AbstractCommandResultBuilder<AutoTweetResultBuilder> {

        /**
         * The tweet
         */
        private Tweet tweet;

        /**
         * The status
         */
        private Status status;

        /**
         * Sets the tweet.
         *
         * @param tweet The tweet
         * @return This builder
         *
         * @exception NullPointerException If {@code null} is passed as an argument
         */
        public AutoTweetResultBuilder setTweet(@NonNull final Tweet tweet) {
            this.tweet = tweet;
            return this;
        }

        /**
         * Sets the status.
         *
         * @param status The status
         * @return This builder
         *
         * @exception NullPointerException If {@code null} is passed as an argument
         */
        public AutoTweetResultBuilder setStatus(@NonNull final Status status) {
            this.status = status;
            return this;
        }

        /**
         * Returns the new instance of {@link AutoTweetResult} based on the parameters.
         *
         * @return The new instance of {@link AutoTweetResult}
         */
        public AutoTweetResult build() {

            final AutoTweetResult autoTweetResult = new AutoTweetResult();
            autoTweetResult.actionStatus = super.actionStatus;
            autoTweetResult.actionErrors = super.actionErrors;
            autoTweetResult.tweet = this.tweet;
            autoTweetResult.status = this.status;

            return autoTweetResult;
        }
    }
}
