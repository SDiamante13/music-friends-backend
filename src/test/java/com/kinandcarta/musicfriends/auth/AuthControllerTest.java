package com.kinandcarta.musicfriends.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthClient mockAuthClient;

    @Mock
    private AuthProperties mockAuthProperties;

    private MockMvc mockMvc;

    private static final String CODE = "CODE";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(mockAuthClient, mockAuthProperties))
                .setControllerAdvice(new AuthExceptionHandler())
                .build();
    }

    @Test
    void login_redirectsToSpotifyAuthUrl() throws Exception {
        String redirectUrl = "https://accounts.spotify.com/authorize";
        given(mockAuthProperties.buildAuthRedirectUrl())
                .willReturn(redirectUrl);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/login"))
                .andExpect(redirectedUrl(redirectUrl));
    }

    @Test
    void callback_retrievesSpotifyAuthTokenUsingCode() throws Exception {
        given(mockAuthClient.retrieveSpotifyToken(any(MultiValueMap.class)))
                .willReturn(new SpotifyToken("authToken", "refreshToken", 20000));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/callback?code=" + CODE))
                .andDo(print())
                .andReturn();

        assertThat(result.getResponse().getCookies())
                .extracting("value")
                        .contains("authToken", "refreshToken");
    }

//    @Test
//    void callback_returnsAClientExceptionErrorMessage_whenAClientExceptionOccurs() throws Exception {
//        willThrow(new ClientException())
//                .given(mockAuthClient)
//                .retrieveSpotifyToken(CODE);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/callback?code=" + CODE))
//                .andDo(print())
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.error.type", is("ClientException")))
//                .andExpect(jsonPath("$.error.message", is("An error occurred has occurred client side.")));
//    }
//
//    @Test
//    void callback_returnsAServerExceptionErrorMessage_whenAServerExceptionOccurs() throws Exception {
//        willThrow(new ServerException())
//                .given(mockAuthClient)
//                .retrieveSpotifyToken(CODE);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/callback?code=" + CODE))
//                .andDo(print())
//                .andExpect(status().isServiceUnavailable())
//                .andExpect(jsonPath("$.error.type", is("ServerException")))
//                .andExpect(jsonPath("$.error.message", is("An error occurred has occurred server side.")));
//    }
//
//    @Test
//    void callback_returnsATokenFailureExceptionErrorMessage_whenATokenFailureExceptionOccurs() throws Exception {
//        willThrow(new TokenFailureException())
//                .given(mockAuthClient)
//                .retrieveSpotifyToken(CODE);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/callback?code=" + CODE))
//                .andDo(print())
//                .andExpect(status().isInternalServerError())
//                .andExpect(jsonPath("$.error.type", is("TokenFailureException")))
//                .andExpect(jsonPath("$.error.message", is("Tokens were not found to authenticate the user.")));
//    }
}