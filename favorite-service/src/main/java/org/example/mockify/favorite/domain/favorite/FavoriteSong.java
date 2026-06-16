package org.example.mockify.favorite.domain.favorite;

import org.example.mockify.favorite.domain.song.SongId;
import org.example.mockify.shared.domain.ValueObject;

import java.time.Instant;
import java.util.Objects;

public record FavoriteSong(SongId songId, Instant addedAt) implements ValueObject {

    public FavoriteSong {
        Objects.requireNonNull(songId, "SongId cannot be null");
        Objects.requireNonNull(addedAt, "AddedAt cannot be null");
    }

    public static FavoriteSong of(SongId songId) {
        return new FavoriteSong(songId, Instant.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FavoriteSong other)) return false;
        return Objects.equals(songId, other.songId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songId);
    }
}
