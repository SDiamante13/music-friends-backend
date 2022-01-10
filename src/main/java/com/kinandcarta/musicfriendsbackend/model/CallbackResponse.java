package com.kinandcarta.musicfriendsbackend.model;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

@Slf4j
public class CallbackResponse extends HttpServletResponseWrapper {

    public CallbackResponse(HttpServletResponse response) {
        super(response);
    }

    @Override
    public HttpServletResponse getResponse() {
        ServletResponse servletResponse = super.getResponse();
        return (HttpServletResponse) servletResponse;
    }

    public void addCookies(MusicTokenInfo musicTokenInfo) {
        if (musicTokenInfo == null) {
            log.error("Failed to add tokens as cookies. The tokens are null");
            return;
        }
        String spotifyAuthToken = musicTokenInfo.getAuthToken();
        String spotifyRefreshToken = musicTokenInfo.getRefreshToken();
        log.info("Adding tokens to cookies. spotify-auth-token= " + spotifyAuthToken + "\nspotify-refresh-token= " + spotifyRefreshToken);

        HttpServletResponse response = getResponse();
        response.addCookie(new Cookie("spotify-auth-token", spotifyAuthToken));
        response.addCookie(new Cookie("spotify-refresh-token", spotifyRefreshToken));
    }
}
