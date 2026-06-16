package org.example.mockify.favorite.domain.favorite;

import org.example.mockify.shared.domain.ValueObject;

import java.util.Objects;
import java.util.UUID;

public record FavoriteListId(UUID value) implements ValueObject {

    public FavoriteListId {
        Objects.requireNonNull(value, "FavoriteListId cannot be null");
    }

    public static FavoriteListId generate() {
        return new FavoriteListId(UUID.randomUUID());
    }

    public static FavoriteListId from(UUID value) {
        return new FavoriteListId(value);
    }
}
