package org.example.mockify.playlist.application.playlist;

import lombok.RequiredArgsConstructor;
import org.example.mockify.playlist.domain.playlist.Playlist;
import org.example.mockify.playlist.domain.playlist.PlaylistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetPlaylistsService implements GetPlaylistsUseCase {

    private final PlaylistRepository playlistRepository;

    @Override
    public List<Playlist> getByAccountId(UUID accountId) {
        return playlistRepository.findByAccountId(accountId);
    }
}
