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

/**
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode(callSuper = false)
public final class MonthlyReportJapaneseStrategy extends AbstractReportStrategy {

    /**
     * The constructor.
     *
     * @param userProfileDifference The user profile difference
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    private MonthlyReportJapaneseStrategy(@NonNull final UserProfileDifference userProfileDifference) {
        super(userProfileDifference);
    }

    /**
     * Returns the new instance of {@link MonthlyReportJapaneseStrategy} based on
     * the object passed as an argument.
     *
     * @param userProfileDifference The user profile difference
     * @return The new instance of {@link MonthlyReportJapaneseStrategy}
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    public static Strategy<Report> from(@NonNull final UserProfileDifference userProfileDifference) {
        return new MonthlyReportJapaneseStrategy(userProfileDifference);
    }

    @Override
    public Report execute() {
        return super.toReport("""
                ?????????????????????%s???

                ?????????????????????????????????: %s (%s%%)
                ????????????????????????????????????: %s (%s%%)

                #?????????????????? #?????? #?????? #???????????????
                """);
    }
}
