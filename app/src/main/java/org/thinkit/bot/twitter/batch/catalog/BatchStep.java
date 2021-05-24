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

import org.thinkit.api.catalog.BiCatalog;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The catalog that manages batch step.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@RequiredArgsConstructor
public enum BatchStep implements BiCatalog<BatchStep, String> {

    /**
     * The start session step
     */
    START_SESSION(-1, "StartSessionStep"),

    /**
     * The auto tweet good morning step
     */
    AUTO_TWEET_GOOD_MORNING(0, "AutoTweetGoodMorning"),

    /**
     * The notify result report
     */
    NOTIFY_RESULT_REPORT(900, "NotifyResultReport"),

    /**
     * The continue session
     */
    CONTINUE_SESSION(997, "ContinueSession"),

    /**
     * The clear session
     */
    CLEAR_SESSION(998, "ClearSession"),

    /**
     * The close session
     */
    CLOSE_SESSION(999, "CloseSession");

    /**
     * The code
     */
    @Getter
    private final int code;

    /**
     * The tag
     */
    @Getter
    private final String tag;
}
