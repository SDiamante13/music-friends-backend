package com.kinandcarta.musicfriends.auth;

class ClientException extends RuntimeException {
    ClientException() {
        super("An error occurred has occurred client side.");
    }
}
