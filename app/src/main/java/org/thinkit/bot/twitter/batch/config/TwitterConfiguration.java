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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
     * Registers the instance of twitter configuration as bean.
     *
     * @return The instance of twitter configuration
     */
    @Bean
    public twitter4j.conf.Configuration twitterConfiguration() {
        return new ConfigurationBuilder().setDebugEnabled(true).setOAuthConsumerKey("9aPckMy8qPphXICvYCsb7QEaN")
                .setOAuthConsumerSecret("S0CGYNybnpdDSAAvzoNvHJbSb1ZCwMMdAB573y27xOsTQnQw5e")
                .setOAuthAccessToken("1392337296997851139-0EuGSV0LV4W37Hi400thLr3C7WnZyr")
                .setOAuthAccessTokenSecret("h0bJ4VPgnR41mJyo7QAebaC0SLYTy4K84D8A5HTOwBfq2").build();
    }
}
