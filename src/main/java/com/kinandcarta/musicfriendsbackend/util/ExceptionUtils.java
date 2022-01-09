package com.kinandcarta.musicfriendsbackend.util;

import com.kinandcarta.musicfriendsbackend.model.exception.ClientException;
import com.kinandcarta.musicfriendsbackend.model.exception.ServerException;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class ExceptionUtils {
    public static Function<ClientResponse, Mono<? extends Throwable>> throwClientException() {
        return error -> Mono.error(new ClientException());
    }

    public static Function<ClientResponse, Mono<? extends Throwable>> throwServerException() {
        return error -> Mono.error(new ServerException());
    }
}