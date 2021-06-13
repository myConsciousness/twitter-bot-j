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

package org.thinkit.bot.twitter.batch.data.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.thinkit.bot.twitter.batch.data.mongo.entity.UserProfile;

/**
 * The interface that manages user profile repository.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@Repository
public interface UserProfileRepository extends MongoRepository<UserProfile, String> {

    /**
     * Returns the user profile linked to the user id passed as an argument.
     *
     * @param name The name
     * @return The user profile
     */
    public UserProfile findByUserId(long userId);

    /**
     * Returns the user profile linked to the name passed as an argument.
     *
     * @param name The name
     * @return The user profile
     */
    public UserProfile findByScreenName(String screenName);
}
