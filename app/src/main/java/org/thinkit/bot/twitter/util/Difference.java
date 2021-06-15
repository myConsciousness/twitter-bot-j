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

import java.io.Serializable;

import org.thinkit.bot.twitter.batch.catalog.DifferenceType;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Difference implements Serializable {

    /**
     * The difference type
     */
    @Getter
    private DifferenceType differenceType;

    /**
     * The value
     */
    @Getter
    private int value;

    /**
     * The growth rate
     */
    @Getter
    private float growthRate;

    public static DifferenceBuilder newBuilder() {
        return new DifferenceBuilder();
    }

    /**
     * @author Kato Shinya
     * @since 1.0.0
     */
    public static class DifferenceBuilder {

        /**
         * The base
         */
        private int base;

        /**
         * The comparison
         */
        private int comparison;

        /**
         * Sets the base.
         *
         * @param base The base
         * @return This builder
         */
        public DifferenceBuilder base(final int base) {
            this.base = base;
            return this;
        }

        /**
         * Sets the comparison.
         *
         * @param comparison The comparison
         * @return This builder
         */
        public DifferenceBuilder comparison(final int comparison) {
            this.comparison = comparison;
            return this;
        }

        /**
         * Returns the new instance of {@link Difference} based on the parameters.
         *
         * @return The new instance of {@link Difference}
         */
        public Difference build() {

            final int differenceValue = this.base - this.comparison;

            final Difference difference = new Difference();
            difference.differenceType = this.getDifferenceType(differenceValue);
            difference.value = differenceValue;
            difference.growthRate = ((float) differenceValue / (float) this.base) * 100.0f;

            return difference;
        }

        /**
         * Returns the difference type based on the int value passed as an argument.
         *
         * @param value The value
         * @return The difference type
         */
        private DifferenceType getDifferenceType(final int value) {

            if (value == 0) {
                return DifferenceType.NONE;
            } else if (value > 0) {
                return DifferenceType.INCREASE;
            }

            return DifferenceType.DECREASE;
        }
    }
}
