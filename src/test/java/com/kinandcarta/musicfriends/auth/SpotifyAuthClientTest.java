package com.kinandcarta.musicfriends.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class SpotifyAuthClientTest {

    private static MockWebServer mockWebServer;

    private SpotifyAuthClient spotifyAuthClient;

    @Mock
    private SpotifyAuthProperties mockSpotifyAuthProperties;

    @Mock
    private CallbackResponse mockCallbackResponse;

    @BeforeAll
    static void beforeAll() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @BeforeEach
    void setUp() {
        String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
        WebClient webClient = WebClient.builder().baseUrl(baseUrl).build();

        spotifyAuthClient = new SpotifyAuthClient(webClient, mockSpotifyAuthProperties);
    }

    @AfterAll
    static void afterAll() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    void redirectToAuth_shouldRedirectToTheRedirectURL() throws Exception {
        spotifyAuthClient.redirectToAuth(mockCallbackResponse);

        then(mockCallbackResponse)
                .should()
                .sendRedirect(null);
    }

    @Test
    void redirectToAuth_shouldThrowARedirectException_whenTheRedirectFails() throws Exception {
        willThrow(IOException.class)
                .given(mockCallbackResponse)
                .sendRedirect(any());

        assertThrows(RedirectException.class,
                () -> spotifyAuthClient.redirectToAuth(mockCallbackResponse)
        );
    }

    @Test
    void retrieveSpotifyToken_returnsTokenSuccessfully() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        SpotifyToken spotifyToken = new SpotifyToken("authToken", "refreshToken", 36000);
        mockWebServer.enqueue(
                new MockResponse()
                        .setBody(objectMapper.writeValueAsString(spotifyToken))
                        .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        );

        SpotifyToken actualToken = spotifyAuthClient.retrieveSpotifyToken("authCode");

        assertThat(actualToken).isEqualTo(spotifyToken);

    }

    @Test
    void retrieveSpotifyToken_throwsATokenFailureException_whenNoTokenIsReturned() throws Exception {
        mockWebServer.enqueue(
                new MockResponse()
                        .setBody("{" +
                                "\"error\": \"invalid_grant\"," +
                                "\"error_description\": \"Authorization code expired\"" +
                                "}")
                        .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        );

        assertThrows(TokenFailureException.class, () -> spotifyAuthClient.retrieveSpotifyToken("authCode"));
    }

    @Test
    void retrieveSpotifyToken_throwsAClientException_whenClientReturns4xxCode() throws Exception {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(404)
                        .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        );

        assertThrows(ClientException.class, () -> spotifyAuthClient.retrieveSpotifyToken("authCode"));
    }

    @Test
    void retrieveSpotifyToken_throwsAServerException_whenClientReturns5xxCode() throws Exception {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(500)
                        .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        );

        assertThrows(ServerException.class, () -> spotifyAuthClient.retrieveSpotifyToken("authCode"));
    }
}
