package com.kinandcarta.musicfriends.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
@RequiredArgsConstructor
class AuthController {

    private final SpotifyAuthClient spotifyClient;

    @GetMapping(value = "/api/v1/login")
    void login(HttpServletResponse response) {
        CallbackResponse callbackResponse = new CallbackResponse(response);
        spotifyClient.redirectToAuth(callbackResponse);
    }

    @GetMapping(value = "/api/v1/callback")
    void callback(@RequestParam("code") String code, HttpServletResponse response) {
        CallbackResponse callbackResponse = new CallbackResponse(response);
        SpotifyToken musicTokenInfo = spotifyClient.retrieveSpotifyToken(code);
        callbackResponse.addCookies(musicTokenInfo);
    }
}
