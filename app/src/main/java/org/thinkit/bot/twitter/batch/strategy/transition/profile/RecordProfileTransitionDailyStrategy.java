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

package org.thinkit.bot.twitter.batch.strategy.transition.profile;

import com.mongodb.lang.NonNull;

import org.thinkit.bot.twitter.batch.catalog.UserProfileTransitionType;
import org.thinkit.bot.twitter.batch.data.mongo.entity.UserProfile;
import org.thinkit.bot.twitter.batch.data.mongo.entity.UserProfileTransition;
import org.thinkit.bot.twitter.batch.data.mongo.repository.UserProfileTransitionRepository;
import org.thinkit.bot.twitter.batch.strategy.Strategy;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode(callSuper = false)
public final class RecordProfileTransitionDailyStrategy extends AbstractRecordProfileTransitionStrategy {

    /**
     * The constructor.
     *
     * @param userProfileTransitionRepository The user profile transition repository
     * @param userProfile                     The user profile
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    private RecordProfileTransitionDailyStrategy(
            @NonNull final UserProfileTransitionRepository userProfileTransitionRepository,
            @NonNull final UserProfile userProfile) {
        super(userProfileTransitionRepository, userProfile);
    }

    /**
     * Returns the new instance of {@link RecordProfileTransitionDailyStrategy}
     * based on the parameters passed as anh arguments.
     *
     * @param userProfileTransitionRepository The user profile transition repository
     * @param userProfile                     The user profile
     * @return The new instance of {@link RecordProfileTransitionDailyStrategy}
     *
     * @exception NullPointerException If {@code null} is passed as an argument.
     */
    public static Strategy<UserProfileTransition> from(
            @NonNull final UserProfileTransitionRepository userProfileTransitionRepository,
            @NonNull final UserProfile userProfile) {
        return new RecordProfileTransitionDailyStrategy(userProfileTransitionRepository, userProfile);
    }

    @Override
    public UserProfileTransition execute() {
        return super.recordTransition(UserProfileTransitionType.DAILY);
    }
}
