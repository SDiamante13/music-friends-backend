package com.kinandcarta.musicfriends.auth;

import io.netty.resolver.DefaultAddressResolverGroup;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;

import static com.kinandcarta.musicfriends.auth.Constants.SPOTIFY_TOKEN_URL;

@Configuration
@EnableConfigurationProperties(SpotifyAuthProperties.class)
class AuthConfiguration {

    @Bean
    WebClient authClient(HttpClient httpclient) {
        return WebClient.builder()
                .baseUrl(SPOTIFY_TOKEN_URL)
                .clientConnector(new ReactorClientHttpConnector(httpclient))
                .build();
    }

    @Bean
    HttpClient buildHttpClient() {
        return HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE);
    }

    @Bean
    SpotifyAuthClient spotifyClient(WebClient webClient, SpotifyAuthProperties spotifyAuthProperties) {
        return new SpotifyAuthClient(webClient, spotifyAuthProperties);
    }
}
