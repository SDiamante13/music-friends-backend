package com.kinandcarta.musicfriends.auth;

public class RedirectException extends RuntimeException {

    public RedirectException() {
        super("Unable to redirect to the requested URL.");
    }
}
