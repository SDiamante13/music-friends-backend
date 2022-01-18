package com.kinandcarta.musicfriends.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private SpotifyAuthClient mockSpotifyAuthClient;

    private MockMvc mockMvc;

    private static final String CODE = "CODE";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(mockSpotifyAuthClient))
                .setControllerAdvice(new AuthExceptionHandler())
                .build();
    }

    @Test
    void login_redirectsToSpotifyAuthUrl() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/login"));

        then(mockSpotifyAuthClient)
                .should()
                .redirectToAuth(any(CallbackResponse.class));
    }

    @Test
    void callback_retrievesSpotifyAuthTokenUsingCode() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/callback?code=" + CODE));

        then(mockSpotifyAuthClient)
                .should()
                .retrieveSpotifyToken(CODE);
    }

    @Test
    void callback_returnsAClientExceptionErrorMessage_whenAClientExceptionOccurs() throws Exception {
        willThrow(new ClientException())
                .given(mockSpotifyAuthClient)
                .retrieveSpotifyToken(CODE);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/callback?code=" + CODE))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.type", is("ClientException")))
                .andExpect(jsonPath("$.error.message", is("An error occurred has occurred client side.")));
    }

    @Test
    void callback_returnsAServerExceptionErrorMessage_whenAServerExceptionOccurs() throws Exception {
        willThrow(new ServerException())
                .given(mockSpotifyAuthClient)
                .retrieveSpotifyToken(CODE);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/callback?code=" + CODE))
                .andDo(print())
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.error.type", is("ServerException")))
                .andExpect(jsonPath("$.error.message", is("An error occurred has occurred server side.")));
    }

    @Test
    void callback_returnsATokenFailureExceptionErrorMessage_whenATokenFailureExceptionOccurs() throws Exception {
        willThrow(new TokenFailureException())
                .given(mockSpotifyAuthClient)
                .retrieveSpotifyToken(CODE);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/callback?code=" + CODE))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error.type", is("TokenFailureException")))
                .andExpect(jsonPath("$.error.message", is("Tokens were not found to authenticate the user.")));
    }
}