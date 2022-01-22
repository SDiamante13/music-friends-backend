package com.kinandcarta.musicfriends.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Slf4j
class AuthController {

    private final AuthClient authClient;
    private final AuthProperties authProperties;

    public AuthController(AuthClient authClient, AuthProperties authProperties) {
        this.authClient = authClient;
        this.authProperties = authProperties;
    }

    @GetMapping(value = "/api/v1/login")
    void login(HttpServletResponse response) {
        try {
            response.sendRedirect(authProperties.buildAuthRedirectUrl());
        } catch (IOException e) {
            throw new RedirectException();
        }
    }

    @GetMapping(value = "/api/v1/callback")
    void callback(@RequestParam("code") String code, HttpServletResponse response) {
        CallbackResponse callbackResponse = new CallbackResponse(response);
        SpotifyToken musicTokenInfo = authClient.retrieveSpotifyToken(buildFormData(code));
        callbackResponse.addCookies(musicTokenInfo);
    }

    private MultiValueMap<String, String> buildFormData(String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("redirect_uri", authProperties.getCallbackUrl());
        formData.add("grant_type", "authorization_code");

        return formData;
    }
}
