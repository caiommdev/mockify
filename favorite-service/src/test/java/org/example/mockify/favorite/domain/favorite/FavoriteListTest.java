package org.example.mockify.favorite.domain.favorite;

import org.example.mockify.favorite.domain.song.SongId;
import org.example.mockify.shared.domain.DomainException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FavoriteListTest {

    private final UUID accountId = UUID.randomUUID();

    @Test
    void should_start_with_empty_favorites() {
        FavoriteList list = new FavoriteList(accountId);
        assertThat(list.getFavorites()).isEmpty();
    }

    @Test
    void should_add_favorite_song() {
        FavoriteList list = new FavoriteList(accountId);
        SongId songId = SongId.generate();
        list.addFavorite(songId);
        assertThat(list.getFavorites()).hasSize(1);
        assertThat(list.getFavorites().get(0).songId()).isEqualTo(songId);
    }

    @Test
    void should_throw_when_adding_duplicate_favorite() {
        FavoriteList list = new FavoriteList(accountId);
        SongId songId = SongId.generate();
        list.addFavorite(songId);

        assertThatThrownBy(() -> list.addFavorite(songId))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("already-favorited");
    }

    @Test
    void should_remove_favorite_song() {
        FavoriteList list = new FavoriteList(accountId);
        SongId songId = SongId.generate();
        list.addFavorite(songId);
        list.removeFavorite(songId);
        assertThat(list.getFavorites()).isEmpty();
    }

    @Test
    void should_throw_when_removing_song_not_in_favorites() {
        FavoriteList list = new FavoriteList(accountId);

        assertThatThrownBy(() -> list.removeFavorite(SongId.generate()))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("song-not-in-favorites");
    }

    @Test
    void should_support_multiple_different_favorites() {
        FavoriteList list = new FavoriteList(accountId);
        list.addFavorite(SongId.generate());
        list.addFavorite(SongId.generate());
        list.addFavorite(SongId.generate());
        assertThat(list.getFavorites()).hasSize(3);
    }
}
