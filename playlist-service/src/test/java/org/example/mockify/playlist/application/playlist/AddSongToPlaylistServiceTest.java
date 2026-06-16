package org.example.mockify.playlist.application.playlist;

import org.example.mockify.playlist.domain.playlist.Playlist;
import org.example.mockify.playlist.domain.playlist.PlaylistId;
import org.example.mockify.playlist.domain.playlist.PlaylistRepository;
import org.example.mockify.shared.domain.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddSongToPlaylistServiceTest {

    @Mock
    private PlaylistRepository playlistRepository;

    @InjectMocks
    private AddSongToPlaylistService addSongToPlaylistService;

    @Test
    void should_add_song_to_existing_playlist() {
        UUID accountId = UUID.randomUUID();
        Playlist playlist = new Playlist(accountId, "My Playlist");
        UUID playlistId = playlist.getId().value();
        UUID songId = UUID.randomUUID();
        when(playlistRepository.findById(PlaylistId.from(playlistId))).thenReturn(Optional.of(playlist));
        when(playlistRepository.save(playlist)).thenReturn(playlist);

        addSongToPlaylistService.addSong(new AddSongToPlaylistCommand(playlistId, songId, 1));

        assertThat(playlist.getEntries()).hasSize(1);
        verify(playlistRepository).save(playlist);
    }

    @Test
    void should_throw_when_playlist_not_found() {
        UUID playlistId = UUID.randomUUID();
        when(playlistRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> addSongToPlaylistService.addSong(
                new AddSongToPlaylistCommand(playlistId, UUID.randomUUID(), 1)))
                .isInstanceOf(DomainException.class)
                .satisfies(e -> assertThat(((DomainException) e).getViolations())
                        .contains("playlist-not-found"));
    }
}
