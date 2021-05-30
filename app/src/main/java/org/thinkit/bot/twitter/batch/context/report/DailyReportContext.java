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

package org.thinkit.bot.twitter.batch.context.report;

import java.io.Serializable;

import org.thinkit.bot.twitter.batch.catalog.Language;
import org.thinkit.bot.twitter.batch.context.Context;
import org.thinkit.bot.twitter.batch.report.Report;
import org.thinkit.bot.twitter.batch.strategy.report.DailyReportEnglishStrategy;
import org.thinkit.bot.twitter.batch.strategy.report.DailyReportJapaneseStrategy;
import org.thinkit.bot.twitter.util.UserProfileDifference;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "from")
public final class DailyReportContext implements Context<Report>, Serializable {

    /**
     * The language
     */
    private Language language;

    /**
     * The user profile difference
     */
    private UserProfileDifference userProfileDifference;

    @Override
    public Report evaluate() {
        return switch (this.language) {
            case JAPANESE -> DailyReportJapaneseStrategy.from(this.userProfileDifference).execute();
            case ENGLISH -> DailyReportEnglishStrategy.from(this.userProfileDifference).execute();
        };
    }
}
