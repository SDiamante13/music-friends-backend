package com.kinandcarta.musicfriends.auth;

class ServerException extends RuntimeException {
    ServerException() {
        super("An error occurred has occurred server side.");
    }
}
