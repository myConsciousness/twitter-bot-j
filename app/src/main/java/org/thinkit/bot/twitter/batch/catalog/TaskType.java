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

package org.thinkit.bot.twitter.batch.catalog;

import org.thinkit.api.catalog.Catalog;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The catalog that manages task type.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@RequiredArgsConstructor
public enum TaskType implements Catalog<TaskType> {

    /**
     * The start session
     */
    START_SESSION(-1),

    /**
     * The auto tweet greeting
     */
    AUTO_TWEET_GREETING(0),

    /**
     * The auto tweet daily report
     */
    AUTO_TWEET_DAILY_REPORT(1),

    /**
     * The auto tweet introduce
     */
    AUTO_TWEET_INTRODUCE(2),

    /**
     * The auto show user
     */
    AUTO_SHOW_USER(100);

    /**
     * The code
     */
    @Getter
    private final int code;
}
