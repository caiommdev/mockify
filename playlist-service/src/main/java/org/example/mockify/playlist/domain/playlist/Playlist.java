package org.example.mockify.playlist.domain.playlist;

import lombok.Getter;
import org.example.mockify.shared.domain.AggregateRoot;
import org.example.mockify.shared.domain.DomainException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
public class Playlist implements AggregateRoot {

    private final PlaylistId id;
    private final UUID accountId;
    private String name;
    private final List<PlaylistEntry> entries;

    public Playlist(UUID accountId, String name) {
        this.id = PlaylistId.generate();
        this.accountId = accountId;
        this.name = name;
        this.entries = new ArrayList<>();
    }

    public Playlist(PlaylistId id, UUID accountId, String name, List<PlaylistEntry> entries) {
        this.id = id;
        this.accountId = accountId;
        this.name = name;
        this.entries = new ArrayList<>(entries);
    }

    public void addSong(UUID songId, int position) {
        boolean alreadyPresent = entries.stream().anyMatch(e -> e.songId().equals(songId));
        if (alreadyPresent) {
            throw new DomainException("song-already-in-playlist");
        }
        entries.add(new PlaylistEntry(songId, position));
        entries.sort((a, b) -> Integer.compare(a.position(), b.position()));
    }

    public void removeSong(UUID songId) {
        boolean removed = entries.removeIf(e -> e.songId().equals(songId));
        if (!removed) {
            throw new DomainException("song-not-in-playlist");
        }
    }

    public void rename(String newName) {
        this.name = newName;
    }

    public List<PlaylistEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }
}
