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

import com.mongodb.lang.NonNull;

import org.thinkit.bot.twitter.batch.catalog.DifferenceSymbol;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Kato Shinya
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DifferenceSymbolUtils {

    public static String toNoneString(final int number) {
        return toString(number, DifferenceSymbol.NONE);
    }

    public static String toIncreaseString(final int number) {
        return toString(number, DifferenceSymbol.INCREASE);
    }

    public static String toDecreaseString(final int number) {
        return toString(number, DifferenceSymbol.DECREASE);
    }

    public static String toNoneString(final float number) {
        return toString(number, DifferenceSymbol.NONE);
    }

    public static String toIncreaseString(final float number) {
        return toString(number, DifferenceSymbol.INCREASE);
    }

    public static String toDecreaseString(final float number) {
        return toString(number, DifferenceSymbol.DECREASE);
    }

    public static String toString(final int number, @NonNull final DifferenceSymbol differenceSymbol) {
        return new StringBuilder().append(differenceSymbol.getTag()).append(number).toString();
    }

    public static String toString(final float number, @NonNull final DifferenceSymbol differenceSymbol) {
        return new StringBuilder().append(differenceSymbol.getTag()).append(number).toString();
    }
}
