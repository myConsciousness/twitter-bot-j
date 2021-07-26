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

package org.thinkit.bot.twitter.batch.context.transition.profile;

import com.mongodb.lang.NonNull;

import org.thinkit.bot.twitter.batch.catalog.UserProfileTransitionType;
import org.thinkit.bot.twitter.batch.context.Context;
import org.thinkit.bot.twitter.batch.data.mongo.entity.UserProfile;
import org.thinkit.bot.twitter.batch.data.mongo.entity.UserProfileTransition;
import org.thinkit.bot.twitter.batch.data.mongo.repository.UserProfileTransitionRepository;
import org.thinkit.bot.twitter.batch.strategy.transition.profile.RecordProfileTransitionDailyStrategy;
import org.thinkit.bot.twitter.batch.strategy.transition.profile.RecordProfileTransitionMonthlyStrategy;
import org.thinkit.bot.twitter.batch.strategy.transition.profile.RecordProfileTransitionWeeklyStrategy;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RecordProfileTransitionContext implements Context<UserProfileTransition> {

    /**
     * The user profile transition repository
     */
    private UserProfileTransitionRepository userProfileTransitionRepository;

    /**
     * The user profile
     */
    private UserProfile userProfile;

    /**
     * The user profile transition type
     */
    private UserProfileTransitionType userProfileTransitionType;

    /**
     * The constructor.
     *
     * @param userProfileTransitionRepository The user profile transition repository
     * @param userProfile                     The user profile
     * @param userProfileTransitionType       The user profile transition type
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    private RecordProfileTransitionContext(
            @NonNull final UserProfileTransitionRepository userProfileTransitionRepository,
            @NonNull final UserProfile userProfile,
            @NonNull final UserProfileTransitionType userProfileTransitionType) {
        this.userProfileTransitionRepository = userProfileTransitionRepository;
        this.userProfile = userProfile;
        this.userProfileTransitionType = userProfileTransitionType;
    }

    /**
     * Returns the new instance of {@link RecordProfileTransitionContext} based on
     * the parameters passed as an argument.
     *
     * @param userProfileTransitionRepository The user profile transition repository
     * @param userProfile                     The user profile
     * @param userProfileTransitionType       The user profile transition type
     * @return Returns the new instance of
     *         {@link {@link RecordProfileTransitionContext}}
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    public static Context<UserProfileTransition> from(
            @NonNull final UserProfileTransitionRepository userProfileTransitionRepository,
            @NonNull final UserProfile userProfile,
            @NonNull final UserProfileTransitionType userProfileTransitionType) {
        return new RecordProfileTransitionContext(userProfileTransitionRepository, userProfile,
                userProfileTransitionType);
    }

    @Override
    public UserProfileTransition evaluate() {
        return switch (this.userProfileTransitionType) {
            case DAILY -> RecordProfileTransitionDailyStrategy
                    .from(this.userProfileTransitionRepository, this.userProfile).execute();
            case WEEKLY -> RecordProfileTransitionWeeklyStrategy
                    .from(this.userProfileTransitionRepository, this.userProfile).execute();
            case MONTHLY -> RecordProfileTransitionMonthlyStrategy
                    .from(this.userProfileTransitionRepository, this.userProfile).execute();
        };
    }
}
