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

package org.thinkit.bot.twitter;

import java.util.List;

import com.mongodb.lang.NonNull;

import org.thinkit.bot.twitter.param.Tweet;
import org.thinkit.bot.twitter.result.AutoFavoriteResult;
import org.thinkit.bot.twitter.result.AutoSearchResult;
import org.thinkit.bot.twitter.result.AutoShowUserResult;
import org.thinkit.bot.twitter.result.AutoTweetResult;

/**
 * The interface that represents the process of Twitter bot.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
public interface TwitterBot {

    /**
     * Executes the auto tweet command and returns the command result.
     *
     * @param tweet The tweet
     * @return The command result
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    public AutoTweetResult executeAutoTweet(@NonNull final Tweet tweet);

    /**
     * Executes the auto show user and returns the command result.
     *
     * @param userName The user name
     * @return The command result
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    public AutoShowUserResult executeAutoShowUser(@NonNull final String userName);

    /**
     * Executes the auto favorite.
     *
     * @param tweetIds The tweet ids
     * @return The command result
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    public AutoFavoriteResult executeAutoFavorite(@NonNull final List<Long> tweetIds);

    /**
     * Executes the auto search and returns the command result.
     *
     * @param keyword The keyword
     * @return The command result
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    public AutoSearchResult executeAutoSearch(@NonNull final String keyword);
}
