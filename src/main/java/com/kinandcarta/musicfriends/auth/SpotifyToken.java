package com.kinandcarta.musicfriends.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class SpotifyToken {
    @JsonProperty("access_token")
    String authToken;

    @JsonProperty("refresh_token")
    String refreshToken;

    @JsonProperty("expires_in")
    int expiresIn;
}
