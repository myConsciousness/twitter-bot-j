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

import java.io.Serializable;

import org.thinkit.bot.twitter.result.ActionError;
import org.thinkit.bot.twitter.util.StackTraceUtils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import twitter4j.Twitter;

/**
 * The class that abstracts the process of bot command.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode

public abstract class AbstractBotCommand<R> implements BotCommand<R>, Serializable {

    /**
     * The twitter
     */
    @Getter(AccessLevel.PROTECTED)
    private Twitter twitter;

    /**
     * Executes the bot process and returns the command result.
     *
     * @return The command result
     */
    protected abstract R executeBotProcess();

    @Override
    public R execute(@NonNull final Twitter twitter) {
        this.twitter = twitter;
        return this.executeBotProcess();
    }

    protected ActionError getActionError(@NonNull final Exception exception) {

        final ActionError.ActionErrorBuilder actionErrorBuilder = ActionError.builder();
        actionErrorBuilder.message(exception.getMessage());
        actionErrorBuilder.localizedMessage(exception.getLocalizedMessage());
        actionErrorBuilder.stackTrace(StackTraceUtils.toString(exception));

        return actionErrorBuilder.build();
    }
}
