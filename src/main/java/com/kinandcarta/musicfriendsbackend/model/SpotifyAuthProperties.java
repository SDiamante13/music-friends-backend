package com.kinandcarta.musicfriendsbackend.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Base64;

import static com.kinandcarta.musicfriendsbackend.util.Constants.*;
import static java.lang.String.format;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "client")
public class SpotifyAuthProperties {
    private String username;
    private String secret;
    private String callbackUrl;

    public String encodeAuthToken() {
        return BASIC + Base64.getEncoder().encodeToString((username + ":" + secret).getBytes());
    }

    public String buildAuthRedirectUrl() {
        return format(SPOTIFY_AUTHORIZE_URL + AUTHORIZE_PARAMETERS, username, callbackUrl);
    }
}
