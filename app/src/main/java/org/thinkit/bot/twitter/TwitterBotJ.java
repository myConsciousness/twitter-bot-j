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

import org.thinkit.bot.twitter.command.AutoFavoriteCommand;
import org.thinkit.bot.twitter.command.AutoSearchCommand;
import org.thinkit.bot.twitter.command.AutoShowUserCommand;
import org.thinkit.bot.twitter.command.AutoTweetCommand;
import org.thinkit.bot.twitter.param.Tweet;
import org.thinkit.bot.twitter.result.AutoFavoriteResult;
import org.thinkit.bot.twitter.result.AutoSearchResult;
import org.thinkit.bot.twitter.result.AutoShowUserResult;
import org.thinkit.bot.twitter.result.AutoTweetResult;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import twitter4j.conf.Configuration;

/**
 * The class that manages the command of Twitter bot.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode(callSuper = false)
public final class TwitterBotJ extends AbstractTwitterBot {

    /**
     * The constructor.
     *
     * @param configuration The twitter configuration
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    private TwitterBotJ(@NonNull final Configuration twitterConfiguration) {
        super(twitterConfiguration);
    }

    /**
     * Returns the new instance of {@link TwitterBotJ} based on the twitter
     * confguration passed as an argument.
     *
     * @param twitterConfiguration The twitter argument
     * @return The new instance of {@link TwitterBotJ}
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    public static TwitterBot from(@NonNull final Configuration twitterConfiguration) {
        return new TwitterBotJ(twitterConfiguration);
    }

    @Override
    public AutoTweetResult executeAutoTweet(@NonNull final Tweet tweet) {
        return AutoTweetCommand.from(tweet).execute(super.getTwitter());
    }

    @Override
    public AutoShowUserResult executeAutoShowUser(@NonNull final String userName) {
        return AutoShowUserCommand.from(userName).execute(super.getTwitter());
    }

    @Override
    public AutoFavoriteResult executeAutoFavorite(@NonNull final List<Long> tweetIds) {
        return AutoFavoriteCommand.from(tweetIds).execute(super.getTwitter());
    }

    @Override
    public AutoSearchResult executeAutoSearch(@NonNull final String keyword) {
        return AutoSearchCommand.from(keyword).execute(super.getTwitter());
    }
}
