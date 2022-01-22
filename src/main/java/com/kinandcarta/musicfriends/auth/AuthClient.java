package com.kinandcarta.musicfriends.auth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth", url = "https://accounts.spotify.com")
public interface AuthClient {

    @PostMapping("/api/token")
    SpotifyToken retrieveSpotifyToken(@RequestBody MultiValueMap<String, String> formData);

}
