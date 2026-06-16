package org.example.mockify.favorite.domain.song;

import org.example.mockify.shared.domain.ValueObject;

import java.util.Objects;
import java.util.UUID;

public record SongId(UUID value) implements ValueObject {

    public SongId {
        Objects.requireNonNull(value, "SongId cannot be null");
    }

    public static SongId generate() {
        return new SongId(UUID.randomUUID());
    }

    public static SongId from(UUID value) {
        return new SongId(value);
    }

    public static SongId from(String value) {
        return new SongId(UUID.fromString(value));
    }
}
