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

package org.thinkit.bot.twitter.batch.policy;

import java.io.Serializable;

import org.thinkit.bot.twitter.batch.catalog.TaskType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The class that manages task.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "from")
public final class BatchTask implements Serializable {

    /**
     * The task type
     */
    private TaskType taskType;

    /**
     * Returns the task type code.
     *
     * @return The task type code
     */
    public int getTypeCode() {
        return this.taskType.getCode();
    }

    /**
     * Checks if the task is {@link TaskType#START_SESSION} .
     *
     * @return {@code true} if the task is {@link TaskType#START_SESSION} ,
     *         otherwise {@code false}
     */
    public boolean isStartSession() {
        return this.taskType == TaskType.START_SESSION;
    }
}
