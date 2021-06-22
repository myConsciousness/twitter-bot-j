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
 * The catalog that manages schedule type.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@RequiredArgsConstructor
public enum ScheduleType implements Catalog<ScheduleType> {

    /**
     * The tweet greeting
     */
    TWEET_GREETING(0),

    /**
     * The tweet daily report
     */
    TWEET_DAILY_REPORT(1),

    /**
     * The tweet introduce
     */
    TWEET_INTRODUCE(2),

    /**
     * The tweet PR
     */
    TWEET_PR(3),

    /**
     * The close session
     */
    CLOSE_SESSION(999);

    /**
     * The code
     */
    @Getter
    private final int code;
}
