package com.kinandcarta.musicfriends.auth;

class EmptyTokenInfoException extends RuntimeException {
    EmptyTokenInfoException() {
        super("Tokens were not found to authenticate the user.");
    }
}
