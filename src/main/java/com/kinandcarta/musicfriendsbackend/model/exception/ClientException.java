package com.kinandcarta.musicfriendsbackend.model.exception;

public class ClientException extends RuntimeException {
    public ClientException() {
        super("An error occurred has occurred client side.");
    }
}
