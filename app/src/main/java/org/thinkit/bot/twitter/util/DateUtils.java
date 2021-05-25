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

package org.thinkit.bot.twitter.util;

import java.util.Calendar;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * It provides common functions for date operation.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateUtils {

    public static boolean isMorning() {
        final int hour = getCurrentHour();
        return 11 >= hour || hour >= 6;
    }

    public static boolean isAfternoon() {
        final int hour = getCurrentHour();
        return 17 >= hour || hour >= 12;
    }

    public static boolean isEvening() {
        final int hour = getCurrentHour();
        return 23 >= hour || hour >= 18;
    }

    private static int getCurrentHour() {
        final Calendar calendar = Calendar.getInstance();
        return switch (calendar.get(Calendar.AM_PM)) {
            case Calendar.PM -> calendar.get(Calendar.HOUR) + 13;
            default -> calendar.get(Calendar.HOUR) + 1;
        };
    }
}
