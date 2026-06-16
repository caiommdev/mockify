package org.example.mockify.favorite.controllers.favorite;

import lombok.RequiredArgsConstructor;
import org.example.mockify.favorite.controllers.favorite.response.SongResponse;
import org.example.mockify.favorite.domain.song.SongId;
import org.example.mockify.favorite.domain.song.SongRepository;
import org.example.mockify.shared.domain.DomainException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/songs")
@RequiredArgsConstructor
public class SongController {

    private final SongRepository songRepository;

    @GetMapping
    public List<SongResponse> listSongs() {
        return songRepository.findAll().stream().map(SongResponse::from).toList();
    }

    @GetMapping("/{songId}")
    public SongResponse getSong(@PathVariable UUID songId) {
        return songRepository.findById(SongId.from(songId))
                .map(SongResponse::from)
                .orElseThrow(() -> new DomainException("song-not-found"));
    }
}
