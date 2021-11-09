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

package org.thinkit.bot.twitter.batch.tasklet;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.stereotype.Component;
import org.thinkit.bot.twitter.batch.catalog.TaskType;
import org.thinkit.bot.twitter.batch.result.BatchTaskResult;
import org.thinkit.bot.twitter.result.AutoSearchResult;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import twitter4j.Status;

/**
 * The class that manages the auto show user.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@Slf4j
@ToString
@EqualsAndHashCode(callSuper = false)
@Component
public final class ExecuteAutoSearchTasklet extends AbstractTasklet {

    /**
     * The default constructor.
     */
    private ExecuteAutoSearchTasklet() {
        super(TaskType.AUTO_SEARCH);
    }

    /**
     * Returns the new instance of {@link ExecuteAutoSearchTasklet} .
     *
     * @return The new instance of {@link ExecuteAutoSearchTasklet}
     */
    public static Tasklet newInstance() {
        return new ExecuteAutoSearchTasklet();
    }

    @Override
    protected BatchTaskResult executeTask(StepContribution contribution, ChunkContext chunkContext) {
        log.debug("START");

        final AutoSearchResult autoSearchResult = super.getTwitterBot().executeAutoSearch("Duolingo");

        final List<Long> tweetIds = new ArrayList<>();
        for (final Status tweet : autoSearchResult.getTweets()) {
            if (!tweetIds.contains(tweet.getId())) {
                tweetIds.add(tweet.getId());
                System.out.println(tweet.getText());
            }
        }

        super.getTwitterBot().executeAutoFavorite(tweetIds);

        final BatchTaskResult.BatchTaskResultBuilder batchTaskResultBuilder = BatchTaskResult.builder();
        batchTaskResultBuilder.actionCount(1);
        batchTaskResultBuilder.resultCount(1);

        log.debug("END");
        return batchTaskResultBuilder.build();
    }
}
