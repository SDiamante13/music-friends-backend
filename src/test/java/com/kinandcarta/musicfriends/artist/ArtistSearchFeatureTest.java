package com.kinandcarta.musicfriends.artist;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.kinandcarta.musicfriends.common.WireMockInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.kinandcarta.musicfriends.common.TestUtils.readFile;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {WireMockInitializer.class})
class ArtistSearchFeatureTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private WireMockServer wireMockServer;

    private static final String NAME = "Wiz Khalifa";

    @Test
    void canSearchForArtistByName() throws Exception {
        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/api/v1/artist")).willReturn(
                        WireMock.aResponse()
                                .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                                .withBody(readFile("artistSearchResponse.json"))
                )
        );

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/artist?name={}", NAME))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("Wiz Khalifa")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.followers", is("10.3 million")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.image", is("https://cdn.com/image/123")));
    }

}
