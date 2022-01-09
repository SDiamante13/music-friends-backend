package com.kinandcarta.musicfriendsbackend.model.exception;

public class ServerException extends RuntimeException {
    public ServerException() {
        super("An error occurred has occurred server side.");
    }
}
