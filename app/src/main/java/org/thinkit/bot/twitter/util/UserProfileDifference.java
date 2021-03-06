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

package org.thinkit.bot.twitter.util;

import java.io.Serializable;

import org.thinkit.bot.twitter.batch.data.mongo.entity.UserProfile;
import org.thinkit.bot.twitter.batch.data.mongo.entity.UserProfileTransition;
import org.thinkit.common.base.precondition.Preconditions;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserProfileDifference implements Serializable {

    /**
     * The followers difference
     */
    @Getter
    private Difference followersDifference;

    /**
     * The followings difference
     */
    @Getter
    private Difference followingsDifference;

    /**
     * Returns the builder.
     *
     * @return The new instance of builder.
     */
    public static UserProfileDifferenceBuilder newBuilder() {
        return new UserProfileDifferenceBuilder();
    }

    /**
     * The inner class that manages the builder of {@link UserProfileDifference} .
     */
    public static class UserProfileDifferenceBuilder {

        /**
         * The user profile
         */
        private UserProfile userProfile;

        /**
         * The user profile transition
         */
        private UserProfileTransition userProfileTransition;

        public UserProfileDifferenceBuilder userProfile(@NonNull final UserProfile userProfile) {
            this.userProfile = userProfile;
            return this;
        }

        public UserProfileDifferenceBuilder userProfileTransition(
                @NonNull final UserProfileTransition userProfileTransition) {
            this.userProfileTransition = userProfileTransition;
            return this;
        }

        public UserProfileDifference build() {
            Preconditions.requireNonNull(this.userProfile);
            Preconditions.requireNonNull(this.userProfileTransition);

            final UserProfileDifference userProfileDifference = new UserProfileDifference();
            userProfileDifference.followersDifference = this.buildDifference(this.userProfile.getFollowersCount(),
                    this.userProfileTransition.getFollowersCount());
            userProfileDifference.followingsDifference = this.buildDifference(this.userProfile.getFollowingsCount(),
                    this.userProfileTransition.getFollowingsCount());

            return userProfileDifference;
        }

        private Difference buildDifference(final int base, final int comparison) {
            return Difference.newBuilder().base(base).comparison(comparison).build();
        }
    }
}
