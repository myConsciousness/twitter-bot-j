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

import java.util.List;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import twitter4j.Status;

/**
 * The class that manages the result of auto search command.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AutoSearchResult extends AbstractCommandResult {

    /**
     * The tweets
     */
    @Getter
    private List<Status> tweets;

    /**
     * Returns the new instance of {@link AutoSearchResultBuilder} .
     *
     * @return The new instance of {@link AutoSearchResultBuilder}
     */
    public static AutoSearchResultBuilder newBuilder() {
        return new AutoSearchResultBuilder();
    }

    /**
     * @author Kato Shinya
     * @since 1.0.0
     */
    public static final class AutoSearchResultBuilder extends AbstractCommandResultBuilder<AutoSearchResultBuilder> {

        /**
         * The tweets
         */
        private List<Status> tweets;

        /**
         * Sets the tweets.
         *
         * @param tweets The tweets
         * @return This builder
         */
        public AutoSearchResultBuilder tweets(@NonNull final List<Status> tweets) {
            this.tweets = tweets;
            return this;
        }

        /**
         * Returns the new instance of {@link AutoSearchResult} based on the parameters.
         *
         * @return The new instance of {@link AutoSearchResult}
         */
        public AutoSearchResult build() {

            final AutoSearchResult AutoSearchResult = new AutoSearchResult();
            AutoSearchResult.tweets = this.tweets;
            AutoSearchResult.actionStatus = super.actionStatus;
            AutoSearchResult.actionErrors = super.actionErrors;

            return AutoSearchResult;
        }
    }
}
