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

package org.thinkit.bot.twitter.command;

import lombok.NonNull;
import twitter4j.Twitter;

/**
 * The interface that represents the process of bot command.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
public interface BotCommand<R> {

    /**
     * Executes the bot command and returns the result object.
     *
     * @param twitter The twitter
     * @return The command result
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    public R execute(@NonNull final Twitter twitter);
}
