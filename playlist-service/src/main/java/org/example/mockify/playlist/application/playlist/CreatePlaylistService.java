package org.example.mockify.playlist.application.playlist;

import lombok.RequiredArgsConstructor;
import org.example.mockify.playlist.domain.playlist.Playlist;
import org.example.mockify.playlist.domain.playlist.PlaylistRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatePlaylistService implements CreatePlaylistUseCase {

    private final PlaylistRepository playlistRepository;

    @Override
    public String createPlaylist(CreatePlaylistCommand command) {
        Playlist playlist = new Playlist(command.accountId(), command.name());
        Playlist saved = playlistRepository.save(playlist);
        return saved.getId().value().toString();
    }
}
