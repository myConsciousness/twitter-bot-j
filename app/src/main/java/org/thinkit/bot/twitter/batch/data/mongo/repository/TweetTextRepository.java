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

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.thinkit.bot.twitter.batch.data.mongo.entity.TweetText;

/**
 * The interface that manages tweet text repository.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@Repository
public interface TweetTextRepository extends MongoRepository<TweetText, String> {

    /**
     * Returns the list of tweet linked to the text code passed as an argument.
     *
     * @param textCode The text code
     * @return The list of tweet
     */
    public List<TweetText> findByTextCode(int textCode);

    /**
     * Returns the tweet linked to the text code and language code passed as
     * arguments.
     *
     * @param textCode     The text code
     * @param languageCode The language code
     * @return The tweet
     */
    public TweetText findByTextCodeAndLanguageCode(int textCode, int languageCode);
}
