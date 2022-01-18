package com.kinandcarta.musicfriends.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Slf4j
class ExceptionUtils {
    static Function<ClientResponse, Mono<? extends Throwable>> throwClientException() {
        return error -> Mono.error(new ClientException());
    }

    static Function<ClientResponse, Mono<? extends Throwable>> throwServerException() {
        return error -> Mono.error(new ServerException());
    }
}