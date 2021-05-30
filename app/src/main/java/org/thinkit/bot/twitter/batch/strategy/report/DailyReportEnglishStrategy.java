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

package org.thinkit.bot.twitter.batch.strategy.report;

import org.thinkit.bot.twitter.batch.report.Report;
import org.thinkit.bot.twitter.batch.strategy.Strategy;
import org.thinkit.bot.twitter.util.UserProfileDifference;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = false)
public final class DailyReportEnglishStrategy extends AbstractDailyReportStrategy {

    /**
     * The constructor.
     *
     * @param userProfileDifference The user profile difference
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    private DailyReportEnglishStrategy(@NonNull final UserProfileDifference userProfileDifference) {
        super(userProfileDifference);
    }

    /**
     * Returns the new instance of {@link DailyReportEnglishStrategy} based on the
     * object passed as an argument.
     *
     * @param userProfileDifference The user profile difference
     * @return The new instance of {@link DailyReportEnglishStrategy}
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    public static Strategy<Report> from(@NonNull final UserProfileDifference userProfileDifference) {
        return new DailyReportEnglishStrategy(userProfileDifference);
    }

    @Override
    public Report execute() {
        return super.toDailyReport("""
                Daily Report（%s）

                ・Followings: %s (%s%)
                ・Followers: %s (%s%)

                #dailytweet #programmer #engineer #developer
                """);
    }
}
