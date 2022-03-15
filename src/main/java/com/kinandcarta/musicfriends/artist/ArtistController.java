package com.kinandcarta.musicfriends.artist;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping("/api/v1/artist")
    public ResponseEntity<ArtistResponse> searchByArtistName(@RequestParam String name) {
        return ResponseEntity.ok(artistService.searchByArtistName(name));
    }
}
