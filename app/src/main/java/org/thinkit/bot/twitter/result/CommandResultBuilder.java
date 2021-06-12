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

package org.thinkit.bot.twitter.result;

import java.io.Serializable;
import java.util.List;

import com.mongodb.lang.NonNull;

import org.thinkit.bot.twitter.catalog.ActionStatus;

/**
 * @author Kato Shinya
 * @since 1.0.0
 */
public interface CommandResultBuilder<R extends CommandResultBuilder<R>> extends Serializable {

    /**
     * Sets the action status.
     *
     * @param actionStatus The action status
     * @return The builder instance
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    public R setActionStatus(@NonNull final ActionStatus actionStatus);

    /**
     * Sets the action errors.
     *
     * @param actionErrors The action errors
     * @return The builder instance
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    public R setActionErrors(@NonNull final List<ActionError> actionErrors);
}
