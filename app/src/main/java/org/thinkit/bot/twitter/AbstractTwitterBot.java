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

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;

/**
 * The class that abstracts the process of Twitter bot.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class AbstractTwitterBot implements TwitterBot, Serializable {

    /**
     * The twitter
     */
    @Getter(AccessLevel.PROTECTED)
    private Twitter twitter;

    /**
     * The constructor.
     *
     * @param twitterConfiguration The twitter configuration
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    protected AbstractTwitterBot(@NonNull final Configuration twitterConfiguration) {
        this.twitter = new TwitterFactory(twitterConfiguration).getInstance();
    }
}
