package org.example.mockify.playlist.domain.playlist;

import org.example.mockify.shared.domain.DomainException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class PlaylistTest {

    private final UUID accountId = UUID.randomUUID();

    @Test
    void should_create_playlist_with_empty_entries() {
        Playlist playlist = new Playlist(accountId, "Chill Vibes");
        assertThat(playlist.getName()).isEqualTo("Chill Vibes");
        assertThat(playlist.getAccountId()).isEqualTo(accountId);
        assertThat(playlist.getEntries()).isEmpty();
        assertThat(playlist.getId()).isNotNull();
    }

    @Test
    void should_add_song_to_playlist() {
        Playlist playlist = new Playlist(accountId, "My Playlist");
        UUID songId = UUID.randomUUID();
        playlist.addSong(songId, 1);
        assertThat(playlist.getEntries()).hasSize(1);
        assertThat(playlist.getEntries().get(0).songId()).isEqualTo(songId);
    }

    @Test
    void should_sort_entries_by_position_after_add() {
        Playlist playlist = new Playlist(accountId, "My Playlist");
        UUID songA = UUID.randomUUID();
        UUID songB = UUID.randomUUID();
        playlist.addSong(songA, 2);
        playlist.addSong(songB, 1);
        assertThat(playlist.getEntries().get(0).songId()).isEqualTo(songB);
        assertThat(playlist.getEntries().get(1).songId()).isEqualTo(songA);
    }

    @Test
    void should_throw_when_adding_duplicate_song() {
        Playlist playlist = new Playlist(accountId, "My Playlist");
        UUID songId = UUID.randomUUID();
        playlist.addSong(songId, 1);
        assertThatThrownBy(() -> playlist.addSong(songId, 2))
                .isInstanceOf(DomainException.class)
                .satisfies(e -> assertThat(((DomainException) e).getViolations())
                        .contains("song-already-in-playlist"));
    }

    @Test
    void should_remove_song_from_playlist() {
        Playlist playlist = new Playlist(accountId, "My Playlist");
        UUID songId = UUID.randomUUID();
        playlist.addSong(songId, 1);
        playlist.removeSong(songId);
        assertThat(playlist.getEntries()).isEmpty();
    }

    @Test
    void should_throw_when_removing_song_not_in_playlist() {
        Playlist playlist = new Playlist(accountId, "My Playlist");
        assertThatThrownBy(() -> playlist.removeSong(UUID.randomUUID()))
                .isInstanceOf(DomainException.class)
                .satisfies(e -> assertThat(((DomainException) e).getViolations())
                        .contains("song-not-in-playlist"));
    }

    @Test
    void should_rename_playlist() {
        Playlist playlist = new Playlist(accountId, "Old Name");
        playlist.rename("New Name");
        assertThat(playlist.getName()).isEqualTo("New Name");
    }
}
