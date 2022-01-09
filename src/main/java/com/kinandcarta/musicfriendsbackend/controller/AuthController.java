package com.kinandcarta.musicfriendsbackend.controller;

import com.kinandcarta.musicfriendsbackend.client.SpotifyClient;
import com.kinandcarta.musicfriendsbackend.model.CallbackResponse;
import com.kinandcarta.musicfriendsbackend.model.MusicTokenInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final SpotifyClient spotifyClient;

    @GetMapping(value = "/api/v1/login")
    public void login(HttpServletResponse response) {
        CallbackResponse callbackResponse = new CallbackResponse(response);
        spotifyClient.redirectToAuth(callbackResponse);
    }

    @GetMapping(value = "/api/v1/callback")
    public void callback(@RequestParam("code") String code, HttpServletResponse response) {
        CallbackResponse callbackResponse = new CallbackResponse(response);
        MusicTokenInfo musicTokenInfo = spotifyClient.getMusicTokenInfo(code);
        callbackResponse.addCookies(musicTokenInfo);
    }
}
