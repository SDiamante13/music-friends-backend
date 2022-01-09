package com.kinandcarta.musicfriendsbackend.model.exception;

public class EmptyTokenInfoException extends RuntimeException {
    public EmptyTokenInfoException() {
        super("Tokens were not found to authenticate the user.");
    }
}
