package com.kinandcarta.musicfriends.auth;

class TokenFailureException extends RuntimeException {
    TokenFailureException() {
        super("Tokens were not found to authenticate the user.");
    }
}
