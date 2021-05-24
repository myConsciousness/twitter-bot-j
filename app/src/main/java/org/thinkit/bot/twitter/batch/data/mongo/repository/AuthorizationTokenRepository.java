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

package org.thinkit.bot.twitter.batch.data.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.thinkit.bot.twitter.batch.data.mongo.entity.AuthorizationToken;

/**
 * The interface that manages authorization token repository.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@Repository
public interface AuthorizationTokenRepository extends MongoRepository<AuthorizationToken, String> {

    /**
     * Returns the authorization token linked to the token type code passed as an
     * argument.
     *
     * @param tokenTypeCode The token type code
     * @return The authorization token
     */
    public AuthorizationToken findByTokenTypeCode(int tokenTypeCode);

}
