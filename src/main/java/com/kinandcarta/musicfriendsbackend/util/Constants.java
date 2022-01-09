package com.kinandcarta.musicfriendsbackend.util;

public class Constants {

    private Constants() {

    }

    public static final String SPOTIFY_TOKEN_URL = "https://accounts.spotify.com/api/token";
    public static final String BASIC = "Basic ";
    public static final String AUTHORIZE_PARAMETERS = "?client_id=%s&response_type=code&redirect_uri=%s";
    public static final String SPOTIFY_AUTHORIZE_URL = "https://accounts.spotify.com/authorize";
}
