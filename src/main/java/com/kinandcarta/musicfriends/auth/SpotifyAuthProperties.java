package com.kinandcarta.musicfriends.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Base64;

import static com.kinandcarta.musicfriends.auth.Constants.*;
import static java.lang.String.format;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "client")
class SpotifyAuthProperties {
    private String username;
    private String secret;
    private String callbackUrl;

    String encodeAuthToken() {
        return BASIC + Base64.getEncoder().encodeToString((username + ":" + secret).getBytes());
    }

    String buildAuthRedirectUrl() {
        return format(SPOTIFY_AUTHORIZE_URL + AUTHORIZE_PARAMETERS, username, callbackUrl);
    }
}
