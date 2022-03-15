package com.kinandcarta.musicfriends.artist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
class ArtistControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ArtistService artistService;

    @InjectMocks
    private ArtistController artistController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(artistController).build();
    }

    @Test
    void searchByArtistNameShouldReturnAnArtistResponseGivenAValidArtistName() throws Exception {
        given(artistService.searchByArtistName(anyString()))
                .willReturn(anArtistResponse());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/artist?name={}", "Queen"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("Queen")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.followers", is("20 million")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.image", is("https://cdn.com/image/555")));
    }

    private ArtistResponse anArtistResponse() {
        return new ArtistResponse("Queen", "20 million", "https://cdn.com/image/555");
    }
}
