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

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public abstract class AbstractRecordProfileTransitionStrategy implements Strategy<UserProfileTransition> {

    /**
     * The user profile transition repository
     */
    private UserProfileTransitionRepository userProfileTransitionRepository;

    /**
     * The user profile
     */
    private UserProfile userProfile;

    /**
     * The constructor.
     *
     * @param userProfileTransitionRepository The user profile transition repository
     * @param userProfile                     The user profile
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    protected AbstractRecordProfileTransitionStrategy(
            @NonNull final UserProfileTransitionRepository userProfileTransitionRepository,
            @NonNull final UserProfile userProfile) {
        this.userProfileTransitionRepository = userProfileTransitionRepository;
        this.userProfile = userProfile;
    }

    protected UserProfileTransition recordTransition(
            @NonNull final UserProfileTransitionType userProfileTransitionType) {
        UserProfileTransition userProfileTransition = new UserProfileTransition();
        userProfileTransition.setUserId(userProfile.getUserId());
        userProfileTransition.setName(userProfile.getName());
        userProfileTransition.setUserProfileTransitionTypeCode(userProfileTransitionType.getCode());
        userProfileTransition.setFollowersCount(userProfile.getFollowersCount());
        userProfileTransition.setFollowingsCount(userProfile.getFollowingsCount());
        userProfileTransition.setRecordedAt(userProfile.getUpdatedAt());
        userProfileTransition.setLatest(true);

        userProfileTransition = this.userProfileTransitionRepository.insert(userProfileTransition);
        log.debug("Inserted user profile transition: {}", userProfileTransition);

        return userProfileTransition;
    }
}
