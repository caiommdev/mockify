package org.example.mockify.playlist.domain.playlist;

import org.example.mockify.shared.domain.ValueObject;

import java.util.Objects;
import java.util.UUID;

public record PlaylistEntry(UUID songId, int position) implements ValueObject {

    public PlaylistEntry {
        Objects.requireNonNull(songId, "SongId cannot be null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlaylistEntry other)) return false;
        return Objects.equals(songId, other.songId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songId);
    }
}
