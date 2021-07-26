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

package org.thinkit.bot.twitter.batch.dto;

import java.io.Serializable;

import org.springframework.batch.core.Step;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The class that manages collections of batch step.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class BatchStepCollections implements Serializable {

    /**
     * The execute auto tweet greeting step
     */
    @Getter
    private Step executeAutoTweetGreetingStep;

    /**
     * The execute auto tweet daily report step
     */
    @Getter
    private Step executeAutoTweetDailyReportStep;

    /**
     * The auto tweet weekly report step
     */
    @Getter
    private Step executeAutoTweetWeeklyReportStep;

    /**
     * The auto tweet monthly report step
     */
    @Getter
    private Step executeAutoTweetMonthlyReportStep;

    /**
     * The execute auto show user step
     */
    @Getter
    private Step executeAutoShowUserStep;

    /**
     * The execute auto tweet introduce step
     */
    @Getter
    private Step executeAutoTweetIntroduceStep;

    /**
     * The execute auto tweet PR step
     */
    @Getter
    private Step executeAutoTweetPrStep;

    /**
     * The close session step
     */
    @Getter
    private Step closeSessionStep;
}
