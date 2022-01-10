package com.kinandcarta.musicfriends.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Optional;

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
        String encodedAuthToken = spotifyAuthProperties.encodeAuthToken();
        Optional<SpotifyToken> optionalTokenInfo = Optional.empty();

        try {
            SpotifyToken tokenInfo = retrieveTokenInfo(code, encodedAuthToken);
            optionalTokenInfo = Optional.ofNullable(tokenInfo);
            optionalTokenInfo.orElseThrow(EmptyTokenInfoException::new);
        } catch (EmptyTokenInfoException | ClientException | ServerException e) {
            log.error("Error - ", e);
        }

        return optionalTokenInfo.get();
    }

    @Override
    public void redirectToAuth(CallbackResponse response) {
        try {
            response.sendRedirect(spotifyAuthProperties.buildAuthRedirectUrl());
        } catch (IOException e) {
            log.error("IOException: ", e);
        }
    }

    private SpotifyToken retrieveTokenInfo(String code, String basicAuthToken) {
        return authClient.post()
                .bodyValue(buildFormData(code))
                .header(AUTHORIZATION, basicAuthToken)
                .header(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, ExceptionUtils.throwClientException())
                .onStatus(HttpStatus::is5xxServerError, ExceptionUtils.throwServerException())
                .bodyToMono(SpotifyToken.class)
                .block();
    }

    private MultiValueMap<String, String> buildFormData(String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("redirect_uri", spotifyAuthProperties.getCallbackUrl());
        formData.add("grant_type", "authorization_code");

        return formData;
    }

}
