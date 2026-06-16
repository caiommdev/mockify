package org.example.mockify.playlist.controllers.playlist;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.mockify.playlist.application.playlist.*;
import org.example.mockify.playlist.domain.playlist.Playlist;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/accounts/{accountId}/playlists")
@RequiredArgsConstructor
public class PlaylistController {

    private final CreatePlaylistUseCase createPlaylistUseCase;
    private final AddSongToPlaylistUseCase addSongToPlaylistUseCase;
    private final RemoveSongFromPlaylistUseCase removeSongFromPlaylistUseCase;
    private final DeletePlaylistUseCase deletePlaylistUseCase;
    private final GetPlaylistsUseCase getPlaylistsUseCase;

    @GetMapping
    public ResponseEntity<List<PlaylistResponse>> getPlaylists(@PathVariable UUID accountId) {
        List<PlaylistResponse> responses = getPlaylistsUseCase.getByAccountId(accountId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<Void> createPlaylist(
            @PathVariable UUID accountId,
            @RequestBody @Valid CreatePlaylistRequest request) {
        String id = createPlaylistUseCase.createPlaylist(
                new CreatePlaylistCommand(accountId, request.name()));
        return ResponseEntity.created(URI.create("/api/v1/accounts/" + accountId + "/playlists/" + id)).build();
    }

    @PostMapping("/{playlistId}/songs")
    public ResponseEntity<Void> addSong(
            @PathVariable UUID accountId,
            @PathVariable UUID playlistId,
            @RequestBody @Valid AddSongRequest request) {
        addSongToPlaylistUseCase.addSong(
                new AddSongToPlaylistCommand(playlistId, request.songId(), request.position()));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<Void> removeSong(
            @PathVariable UUID accountId,
            @PathVariable UUID playlistId,
            @PathVariable UUID songId) {
        removeSongFromPlaylistUseCase.removeSong(playlistId, songId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{playlistId}")
    public ResponseEntity<Void> deletePlaylist(
            @PathVariable UUID accountId,
            @PathVariable UUID playlistId) {
        deletePlaylistUseCase.deletePlaylist(playlistId);
        return ResponseEntity.noContent().build();
    }

    private PlaylistResponse toResponse(Playlist playlist) {
        List<PlaylistEntryResponse> entries = playlist.getEntries().stream()
                .map(e -> new PlaylistEntryResponse(e.songId(), e.position()))
                .collect(Collectors.toList());
        return new PlaylistResponse(
                playlist.getId().value(),
                playlist.getAccountId(),
                playlist.getName(),
                entries);
    }

    record CreatePlaylistRequest(@NotNull String name) {}

    record AddSongRequest(@NotNull UUID songId, int position) {}

    record PlaylistResponse(UUID id, UUID accountId, String name, List<PlaylistEntryResponse> entries) {}

    record PlaylistEntryResponse(UUID songId, int position) {}
}
