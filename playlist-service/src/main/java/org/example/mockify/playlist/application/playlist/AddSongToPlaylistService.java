package org.example.mockify.playlist.application.playlist;

import lombok.RequiredArgsConstructor;
import org.example.mockify.playlist.domain.playlist.Playlist;
import org.example.mockify.playlist.domain.playlist.PlaylistId;
import org.example.mockify.playlist.domain.playlist.PlaylistRepository;
import org.example.mockify.shared.domain.DomainException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddSongToPlaylistService implements AddSongToPlaylistUseCase {

    private final PlaylistRepository playlistRepository;

    @Override
    public void addSong(AddSongToPlaylistCommand command) {
        Playlist playlist = playlistRepository.findById(PlaylistId.from(command.playlistId()))
                .orElseThrow(() -> new DomainException("playlist-not-found"));
        playlist.addSong(command.songId(), command.position());
        playlistRepository.save(playlist);
    }
}
