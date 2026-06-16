package org.example.mockify.playlist.domain.playlist;

import org.example.mockify.shared.domain.ValueObject;

import java.util.Objects;
import java.util.UUID;

public record PlaylistId(UUID value) implements ValueObject {

    public PlaylistId {
        Objects.requireNonNull(value, "PlaylistId cannot be null");
    }

    public static PlaylistId generate() {
        return new PlaylistId(UUID.randomUUID());
    }

    public static PlaylistId from(UUID value) {
        return new PlaylistId(value);
    }

    public static PlaylistId from(String value) {
        return new PlaylistId(UUID.fromString(value));
    }
}
