package org.example.mockify.favorite.domain.favorite;

import lombok.Getter;
import org.example.mockify.favorite.domain.song.SongId;
import org.example.mockify.shared.domain.AggregateRoot;
import org.example.mockify.shared.domain.DomainException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
public class FavoriteList implements AggregateRoot {

    private final FavoriteListId id;
    private final UUID accountId;
    private final List<FavoriteSong> favorites;

    public FavoriteList(UUID accountId) {
        this.id = FavoriteListId.generate();
        this.accountId = accountId;
        this.favorites = new ArrayList<>();
    }

    public FavoriteList(FavoriteListId id, UUID accountId, List<FavoriteSong> favorites) {
        this.id = id;
        this.accountId = accountId;
        this.favorites = new ArrayList<>(favorites);
    }

    public void addFavorite(SongId songId) {
        boolean alreadyFavorited = favorites.stream()
                .anyMatch(f -> f.songId().equals(songId));
        if (alreadyFavorited) {
            throw new DomainException("already-favorited");
        }
        favorites.add(FavoriteSong.of(songId));
    }

    public void removeFavorite(SongId songId) {
        boolean removed = favorites.removeIf(f -> f.songId().equals(songId));
        if (!removed) {
            throw new DomainException("song-not-in-favorites");
        }
    }

    public List<FavoriteSong> getFavorites() {
        return Collections.unmodifiableList(favorites);
    }
}
