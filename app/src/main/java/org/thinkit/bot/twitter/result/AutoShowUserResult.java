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

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * The class that manages the result of auto show user command.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AutoShowUserResult extends AbstractCommandResult {

    /**
     * The user id
     */
    @Getter
    private long userId;

    /**
     * The screen name
     */
    @Getter
    private String screenName;

    /**
     * The user name
     */
    @Getter
    @NonNull
    private String userName;

    /**
     * The followers count
     */
    @Getter
    private int followersCount;

    /**
     * The followings count
     */
    @Getter
    private int followingsCount;

    /**
     * Returns the new instance of {@link AutoShowUserResultBuilder} .
     *
     * @return The new instance of {@link AutoShowUserResultBuilder}
     */
    public static AutoShowUserResultBuilder newBuilder() {
        return new AutoShowUserResultBuilder();
    }

    /**
     * @author Kato Shinya
     * @since 1.0.0
     */
    public static final class AutoShowUserResultBuilder
            extends AbstractCommandResultBuilder<AutoShowUserResultBuilder> {

        /**
         * The user id
         */
        private long userId;

        /**
         * The screen name
         */
        private String screenName;

        /**
         * The user name
         */
        private String userName;

        /**
         * The followers count
         */
        private int followersCount;

        /**
         * The followings count
         */
        private int followingsCount;

        /**
         * Sets the user id.
         *
         * @param userId The user id
         * @return This builder
         */
        public AutoShowUserResultBuilder setUserId(final long userId) {
            this.userId = userId;
            return this;
        }

        /**
         * Sets the screen name.
         *
         * @param screenName The screen name
         * @return This builder
         *
         * @exception NullPointerException If {@code null} is passed as an argument
         */
        public AutoShowUserResultBuilder setScreenName(@NonNull final String screenName) {
            this.screenName = screenName;
            return this;
        }

        /**
         * Sets the user name.
         *
         * @param userName The user name
         * @return This builder
         *
         * @exception NullPointerException If {@code null} is passed as an argument
         */
        public AutoShowUserResultBuilder setUserName(@NonNull final String userName) {
            this.userName = userName;
            return this;
        }

        /**
         * Sets the followers count.
         *
         * @param followersCount The followers count
         * @return This builder
         */
        public AutoShowUserResultBuilder setFollowersCount(final int followersCount) {
            this.followersCount = followersCount;
            return this;
        }

        /**
         * Sets the followings count.
         *
         * @param followingsCount The followings count
         * @return This builder
         */
        public AutoShowUserResultBuilder setFollowingsCount(final int followingsCount) {
            this.followingsCount = followingsCount;
            return this;
        }

        /**
         * Returns the new instance of {@link AutoShowUserResult} based on the
         * parameters.
         *
         * @return The new instance of {@link AutoShowUserResult}
         */
        public AutoShowUserResult build() {

            final AutoShowUserResult autoShowUserResult = new AutoShowUserResult();
            autoShowUserResult.actionStatus = super.actionStatus;
            autoShowUserResult.actionErrors = super.actionErrors;
            autoShowUserResult.userId = this.userId;
            autoShowUserResult.screenName = this.screenName;
            autoShowUserResult.userName = this.userName;
            autoShowUserResult.followersCount = this.followersCount;
            autoShowUserResult.followingsCount = this.followingsCount;

            return autoShowUserResult;
        }
    }
}
