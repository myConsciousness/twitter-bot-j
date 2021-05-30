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

package org.thinkit.bot.twitter.batch.config;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thinkit.bot.twitter.batch.catalog.TokenType;
import org.thinkit.bot.twitter.batch.catalog.VariableName;
import org.thinkit.bot.twitter.batch.data.content.mapper.DefaultVariableMapper;
import org.thinkit.bot.twitter.batch.data.mongo.entity.AuthorizationToken;
import org.thinkit.bot.twitter.batch.data.mongo.entity.UserAccount;
import org.thinkit.bot.twitter.batch.data.mongo.entity.Variable;
import org.thinkit.bot.twitter.batch.data.mongo.repository.AuthorizationTokenRepository;
import org.thinkit.bot.twitter.batch.data.mongo.repository.UserAccountRepository;
import org.thinkit.bot.twitter.batch.data.mongo.repository.VariableRepository;
import org.thinkit.bot.twitter.batch.dto.MongoCollections;
import org.thinkit.bot.twitter.batch.exception.AuthorizationConfigNotFoundException;
import org.thinkit.bot.twitter.batch.exception.AvailableUserAccountNotFoundException;
import org.thinkit.bot.twitter.batch.policy.RunningUser;

import lombok.NonNull;
import twitter4j.conf.ConfigurationBuilder;

/**
 * The configuration class for Twitter.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@Configuration
public class TwitterConfiguration {

    /**
     * The mongo collections
     */
    @Autowired
    private MongoCollections mongoCollections;

    /**
     * Registers the instance of {@link RunningUser} as bean.
     *
     * @return The instance of {@link RunningUser}
     */
    @Bean
    public RunningUser runningUser() {

        final UserAccountRepository userAccountRepository = this.mongoCollections.getUserAccountRepository();
        final List<UserAccount> userAccounts = userAccountRepository.findAll();

        if (userAccounts.isEmpty()) {
            throw new AvailableUserAccountNotFoundException(
                    "No available user accounts were found. Please check the definition of the user account.");
        }

        return RunningUser.from(userAccounts.get(0).getName());
    }

    /**
     * Registers the instance of twitter configuration as bean.
     *
     * @return The instance of twitter configuration
     */
    @Bean
    public twitter4j.conf.Configuration configuration() {
        return new ConfigurationBuilder().setDebugEnabled(this.getDebugEnabled())
                .setOAuthConsumerKey(this.getOAuthConsumerKey()).setOAuthConsumerSecret(this.getOAuthConsumerSecret())
                .setOAuthAccessToken(this.getOAuthAccessToken())
                .setOAuthAccessTokenSecret(this.getOAuthAccessTokenSecret()).build();
    }

    /**
     * Returns the debug enabled flag as boolean.
     *
     * @return The debug enabled flag.
     */
    private boolean getDebugEnabled() {
        return Boolean.parseBoolean(this.getVariableValue(VariableName.TWITTER_DEBUG_ENABLED));
    }

    /**
     * Returns the OAuth consumer key.
     *
     * @return The OAuth consumer key
     */
    private String getOAuthConsumerKey() {
        return this.getAuthorizationToken(TokenType.O_AUTH_CONSUMER_KEY);
    }

    /**
     * Returns the OAuth consumer secret.
     *
     * @return The OAuth consumer secret
     */
    private String getOAuthConsumerSecret() {
        return this.getAuthorizationToken(TokenType.O_AUTH_CONSUMER_SECRET);
    }

    /**
     * Returns the OAuth access token.
     *
     * @return The OAuth access token
     */
    private String getOAuthAccessToken() {
        return this.getAuthorizationToken(TokenType.O_AUTH_ACCESS_TOKEN);
    }

    /**
     * Returns the OAuth access token secret.
     *
     * @return The OAuth access token secret
     */
    private String getOAuthAccessTokenSecret() {
        return this.getAuthorizationToken(TokenType.O_AUTH_ACCESS_TOKEN_SECRET);
    }

    private String getAuthorizationToken(@NonNull final TokenType tokenType) {

        final AuthorizationTokenRepository authorizationTokenRepository = this.mongoCollections
                .getAuthorizationTokenRepository();
        final AuthorizationToken authorizationToken = authorizationTokenRepository
                .findByTokenTypeCode(tokenType.getCode());

        if (!this.isValidToken(authorizationToken)) {
            throw new AuthorizationConfigNotFoundException(String.format(
                    "The key or token '%s' required for Twitter API authorization has not been registered as a variable.",
                    tokenType.name()));
        }

        return authorizationToken.getToken();
    }

    private String getVariableValue(@NonNull final VariableName variableName) {

        final VariableRepository variableRepository = this.mongoCollections.getVariableRepository();
        Variable variable = variableRepository.findByName(variableName.getTag());

        if (variable == null) {
            variable = new Variable();
            variable.setName(variableName.getTag());
            variable.setValue(this.getDefaultVariableValue(variableName));
            variable = variableRepository.insert(variable);
        }

        return variable.getValue();
    }

    private boolean isValidToken(final AuthorizationToken authorizationToken) {
        return authorizationToken != null && !StringUtils.isEmpty(authorizationToken.getToken());
    }

    private String getDefaultVariableValue(@NonNull final VariableName variableName) {
        final DefaultVariableMapper defaultVariableMapper = DefaultVariableMapper.from(variableName.getTag());
        return defaultVariableMapper.scan().get(0).getValue();
    }
}
