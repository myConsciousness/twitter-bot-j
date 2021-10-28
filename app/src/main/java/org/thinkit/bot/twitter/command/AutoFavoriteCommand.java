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

package org.thinkit.bot.twitter.command;

import java.util.List;

import org.thinkit.bot.twitter.catalog.ActionStatus;
import org.thinkit.bot.twitter.result.AutoFavoriteResult;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The class that manages auto favorite user command.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "from")
public class AutoFavoriteCommand extends AbstractBotCommand<AutoFavoriteResult> {

    /**
     * The tweet ids
     */
    private List<Long> tweetIds;

    @Override
    protected AutoFavoriteResult executeBotProcess() {

        final AutoFavoriteResult.AutoFavoriteResultBuilder autoShowUserResultBuilder = AutoFavoriteResult.newBuilder();

        for (final Long tweetId : this.tweetIds) {
            try {
                super.getTwitter().createFavorite(tweetId);
            } catch (Exception e) {
                autoShowUserResultBuilder.setActionStatus(ActionStatus.INTERRUPTED);
                autoShowUserResultBuilder.setActionErrors(List.of(super.getActionError(e)));
            }
        }

        return autoShowUserResultBuilder.build();
    }
}
