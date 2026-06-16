package org.example.mockify.playlist.infrastructure.playlist;

import org.example.mockify.playlist.domain.playlist.Playlist;
import org.example.mockify.playlist.domain.playlist.PlaylistId;
import org.example.mockify.playlist.domain.playlist.PlaylistRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaPlaylistRepository.class)
class JpaPlaylistRepositoryTest {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Test
    void should_save_and_find_playlist_by_id() {
        UUID accountId = UUID.randomUUID();
        Playlist playlist = new Playlist(accountId, "Road Trip");
        Playlist saved = playlistRepository.save(playlist);

        Optional<Playlist> found = playlistRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Road Trip");
        assertThat(found.get().getAccountId()).isEqualTo(accountId);
    }

    @Test
    void should_persist_playlist_entries() {
        UUID accountId = UUID.randomUUID();
        Playlist playlist = new Playlist(accountId, "Workout");
        UUID song1 = UUID.randomUUID();
        UUID song2 = UUID.randomUUID();
        playlist.addSong(song1, 1);
        playlist.addSong(song2, 2);

        Playlist saved = playlistRepository.save(playlist);
        Playlist reloaded = playlistRepository.findById(saved.getId()).orElseThrow();

        assertThat(reloaded.getEntries()).hasSize(2);
    }

    @Test
    void should_find_playlists_by_account_id() {
        UUID accountId = UUID.randomUUID();
        playlistRepository.save(new Playlist(accountId, "Playlist A"));
        playlistRepository.save(new Playlist(accountId, "Playlist B"));
        playlistRepository.save(new Playlist(UUID.randomUUID(), "Other"));

        List<Playlist> results = playlistRepository.findByAccountId(accountId);
        assertThat(results).hasSize(2);
    }

    @Test
    void should_delete_playlist_by_id() {
        UUID accountId = UUID.randomUUID();
        Playlist playlist = playlistRepository.save(new Playlist(accountId, "Temp"));
        PlaylistId id = playlist.getId();

        playlistRepository.deleteById(id);

        assertThat(playlistRepository.findById(id)).isEmpty();
    }
}
