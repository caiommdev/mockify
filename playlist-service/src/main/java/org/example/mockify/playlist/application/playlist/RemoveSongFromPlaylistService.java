package org.example.mockify.playlist.application.playlist;

import lombok.RequiredArgsConstructor;
import org.example.mockify.playlist.domain.playlist.Playlist;
import org.example.mockify.playlist.domain.playlist.PlaylistId;
import org.example.mockify.playlist.domain.playlist.PlaylistRepository;
import org.example.mockify.shared.domain.DomainException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RemoveSongFromPlaylistService implements RemoveSongFromPlaylistUseCase {

    private final PlaylistRepository playlistRepository;

    @Override
    public void removeSong(UUID playlistId, UUID songId) {
        Playlist playlist = playlistRepository.findById(PlaylistId.from(playlistId))
                .orElseThrow(() -> new DomainException("playlist-not-found"));
        playlist.removeSong(songId);
        playlistRepository.save(playlist);
    }
}
