package com.kinandcarta.musicfriendsbackend.config;

import com.kinandcarta.musicfriendsbackend.client.SpotifyClient;
import com.kinandcarta.musicfriendsbackend.model.SpotifyAuthProperties;
import io.netty.resolver.DefaultAddressResolverGroup;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;

import static com.kinandcarta.musicfriendsbackend.util.Constants.SPOTIFY_TOKEN_URL;

@Configuration
@EnableConfigurationProperties(SpotifyAuthProperties.class)
public class WebConfiguration {

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
    SpotifyClient spotifyClient(WebClient webClient, SpotifyAuthProperties spotifyAuthProperties) {
        return new SpotifyClient(webClient, spotifyAuthProperties);
    }
}
