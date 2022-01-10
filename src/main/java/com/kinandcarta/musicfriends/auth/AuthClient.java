package com.kinandcarta.musicfriends.auth;

interface AuthClient {
    SpotifyToken retrieveSpotifyToken(String code);
    void redirectToAuth(CallbackResponse response);
}
