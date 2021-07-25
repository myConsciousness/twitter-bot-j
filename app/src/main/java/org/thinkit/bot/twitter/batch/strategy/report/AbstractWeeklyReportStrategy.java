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

import org.thinkit.bot.twitter.batch.catalog.DateFormat;
import org.thinkit.bot.twitter.batch.report.Report;
import org.thinkit.bot.twitter.batch.strategy.Strategy;
import org.thinkit.bot.twitter.util.DateUtils;
import org.thinkit.bot.twitter.util.Difference;
import org.thinkit.bot.twitter.util.DifferenceSymbolUtils;
import org.thinkit.bot.twitter.util.UserProfileDifference;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public abstract class AbstractWeeklyReportStrategy implements Strategy<Report> {

    /**
     * The user profile difference
     */
    private UserProfileDifference userProfileDifference;

    protected final Report toWeeklyReport(@NonNull final String template) {
        return Report.from(String.format(template, DateUtils.toString(DateFormat.YYYY_MM_DD_WITH_SLASH),
                this.getFollowingsDifferenceCount(), this.getFollowingsGrowthRate(), this.getFollowersDifferenceCount(),
                this.getFollowersGrowthRate()));
    }

    private String getFollowingsDifferenceCount() {
        return this.toReportCount(this.userProfileDifference.getFollowingsDifference());
    }

    private String getFollowingsGrowthRate() {
        return this.toReportGrowthRate(this.userProfileDifference.getFollowingsDifference());
    }

    private String getFollowersDifferenceCount() {
        return this.toReportCount(this.userProfileDifference.getFollowersDifference());
    }

    private String getFollowersGrowthRate() {
        return this.toReportGrowthRate(this.userProfileDifference.getFollowersDifference());
    }

    private String toReportCount(@NonNull final Difference difference) {
        return switch (difference.getDifferenceType()) {
            case NONE -> DifferenceSymbolUtils.toNoneString(difference.getValue());
            case INCREASE -> DifferenceSymbolUtils.toIncreaseString(difference.getValue());
            case DECREASE -> DifferenceSymbolUtils.toDecreaseString(difference.getValue());
        };
    }

    private String toReportGrowthRate(@NonNull final Difference difference) {
        return switch (difference.getDifferenceType()) {
            case NONE -> DifferenceSymbolUtils.toNoneString(difference.getGrowthRate());
            case INCREASE -> DifferenceSymbolUtils.toIncreaseString(difference.getGrowthRate());
            case DECREASE -> DifferenceSymbolUtils.toDecreaseString(difference.getGrowthRate());
        };
    }
}
