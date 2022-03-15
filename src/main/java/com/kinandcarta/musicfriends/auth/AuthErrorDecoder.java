package com.kinandcarta.musicfriends.auth;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("An error occurred while calling Spotify auth. Error: Status {}, Reason {}.", response.status(), response.reason());
        switch (response.status()) {
            case 400:
            case 404:
                return new ClientException();
            case 500:
                return new ServerException();
            default:
                return new RuntimeException();
        }
    }
}
