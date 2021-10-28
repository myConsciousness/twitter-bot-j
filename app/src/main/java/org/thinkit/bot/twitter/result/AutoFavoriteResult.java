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

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The class that manages the result of auto tweet command.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AutoFavoriteResult extends AbstractCommandResult {

    /**
     * Returns the new instance of {@link AutoFavoriteResultBuilder} .
     *
     * @return The new instance of {@link AutoFavoriteResultBuilder}
     */
    public static AutoFavoriteResultBuilder newBuilder() {
        return new AutoFavoriteResultBuilder();
    }

    /**
     * @author Kato Shinya
     * @since 1.0.0
     */
    public static final class AutoFavoriteResultBuilder
            extends AbstractCommandResultBuilder<AutoFavoriteResultBuilder> {

        /**
         * Returns the new instance of {@link AutoFavoriteResult} based on the
         * parameters.
         *
         * @return The new instance of {@link AutoFavoriteResult}
         */
        public AutoFavoriteResult build() {

            final AutoFavoriteResult autoFavoriteResult = new AutoFavoriteResult();
            autoFavoriteResult.actionStatus = super.actionStatus;
            autoFavoriteResult.actionErrors = super.actionErrors;

            return autoFavoriteResult;
        }
    }
}
