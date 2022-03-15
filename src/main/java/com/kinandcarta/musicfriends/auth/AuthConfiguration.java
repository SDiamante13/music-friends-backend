package com.kinandcarta.musicfriends.auth;

import feign.codec.ErrorDecoder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AuthProperties.class)
class AuthConfiguration {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new AuthErrorDecoder();
    }

}
