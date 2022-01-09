package com.kinandcarta.musicfriendsbackend.client;

import com.kinandcarta.musicfriendsbackend.model.CallbackResponse;
import com.kinandcarta.musicfriendsbackend.model.MusicTokenInfo;
import com.kinandcarta.musicfriendsbackend.model.SpotifyAuthProperties;
import com.kinandcarta.musicfriendsbackend.model.exception.ClientException;
import com.kinandcarta.musicfriendsbackend.model.exception.EmptyTokenInfoException;
import com.kinandcarta.musicfriendsbackend.model.exception.ServerException;
import com.kinandcarta.musicfriendsbackend.util.ExceptionUtils;
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
public class SpotifyClient implements MusicClient {

    private final WebClient authClient;

    private final SpotifyAuthProperties spotifyAuthProperties;

    public SpotifyClient(WebClient authClient, SpotifyAuthProperties spotifyAuthProperties) {
        this.authClient = authClient;
        this.spotifyAuthProperties = spotifyAuthProperties;
    }

    @Override
    public MusicTokenInfo getMusicTokenInfo(String code) {
        String encodedAuthToken = spotifyAuthProperties.encodeAuthToken();
        Optional<MusicTokenInfo> optionalTokenInfo = Optional.empty();

        try {
            MusicTokenInfo tokenInfo = retrieveTokenInfo(code, encodedAuthToken);
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

    private MusicTokenInfo retrieveTokenInfo(String code, String basicAuthToken) {
        return authClient.post()
                .bodyValue(buildFormData(code))
                .header(AUTHORIZATION, basicAuthToken)
                .header(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, ExceptionUtils.throwClientException())
                .onStatus(HttpStatus::is5xxServerError, ExceptionUtils.throwServerException())
                .bodyToMono(MusicTokenInfo.class)
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
