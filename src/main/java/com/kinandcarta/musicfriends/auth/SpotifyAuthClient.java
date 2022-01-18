package com.kinandcarta.musicfriends.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

@Slf4j
class SpotifyAuthClient implements AuthClient {

    private final WebClient authClient;
    private final SpotifyAuthProperties spotifyAuthProperties;

    SpotifyAuthClient(WebClient authClient, SpotifyAuthProperties spotifyAuthProperties) {
        this.authClient = authClient;
        this.spotifyAuthProperties = spotifyAuthProperties;
    }

    @Override
    public SpotifyToken retrieveSpotifyToken(String code) {
        SpotifyToken spotifyToken = authClient.post()
                .bodyValue(buildFormData(code))
                .header(AUTHORIZATION, spotifyAuthProperties.encodeAuthToken())
                .header(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, ExceptionUtils.throwClientException())
                .onStatus(HttpStatus::is5xxServerError, ExceptionUtils.throwServerException())
                .bodyToMono(SpotifyToken.class)
                .block();

        if (spotifyToken == null || spotifyToken.authTokenIsMissing()) throw new TokenFailureException();

        return spotifyToken;
    }

    private MultiValueMap<String, String> buildFormData(String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("redirect_uri", spotifyAuthProperties.getCallbackUrl());
        formData.add("grant_type", "authorization_code");

        return formData;
    }

    @Override
    public void redirectToAuth(CallbackResponse response) {
        try {
            response.sendRedirect(spotifyAuthProperties.buildAuthRedirectUrl());
        } catch (IOException e) {
            throw new RedirectException();
        }
    }
}
