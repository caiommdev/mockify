package org.example.mockify.playlist.application.playlist;

import lombok.RequiredArgsConstructor;
import org.example.mockify.playlist.domain.playlist.PlaylistId;
import org.example.mockify.playlist.domain.playlist.PlaylistRepository;
import org.example.mockify.shared.domain.DomainException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeletePlaylistService implements DeletePlaylistUseCase {

    private final PlaylistRepository playlistRepository;

    @Override
    public void deletePlaylist(UUID playlistId) {
        PlaylistId id = PlaylistId.from(playlistId);
        playlistRepository.findById(id)
                .orElseThrow(() -> new DomainException("playlist-not-found"));
        playlistRepository.deleteById(id);
    }
}
