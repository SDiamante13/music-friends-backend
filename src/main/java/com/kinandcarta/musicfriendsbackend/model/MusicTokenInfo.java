package com.kinandcarta.musicfriendsbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MusicTokenInfo {
    @JsonProperty("access_token")
    String authToken;

    @JsonProperty("refresh_token")
    String refreshToken;

    @JsonProperty("expires_in")
    int expiresIn;
}
