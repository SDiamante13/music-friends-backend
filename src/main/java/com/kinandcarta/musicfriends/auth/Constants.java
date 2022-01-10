package com.kinandcarta.musicfriends.auth;

class Constants {

    private Constants() {

    }

    static final String SPOTIFY_TOKEN_URL = "https://accounts.spotify.com/api/token";
    static final String BASIC = "Basic ";
    static final String AUTHORIZE_PARAMETERS = "?client_id=%s&response_type=code&redirect_uri=%s";
    static final String SPOTIFY_AUTHORIZE_URL = "https://accounts.spotify.com/authorize";
}
