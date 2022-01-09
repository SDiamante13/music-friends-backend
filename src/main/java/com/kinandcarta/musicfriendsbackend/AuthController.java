package com.kinandcarta.musicfriendsbackend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class CallbackController {

    @GetMapping(value = "/api/v1/callback")
    public String callback(@RequestParam("code") String code) {
        WebClient client = WebClient.builder()
                .baseUrl("https://accounts.spotify.com/api/token")
                .build();


        return "token";
    }
}
