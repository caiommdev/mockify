package org.example.mockify.playlist.infrastructure.playlist;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "playlists")
@Getter
@Setter
@NoArgsConstructor
public class PlaylistJpaEntity {

    @Id
    private UUID id;

    private UUID accountId;

    private String name;

    @ElementCollection
    @CollectionTable(name = "playlist_entries", joinColumns = @JoinColumn(name = "playlist_id"))
    private List<PlaylistEntryJpaElement> entries = new ArrayList<>();

    public PlaylistJpaEntity(UUID id, UUID accountId, String name) {
        this.id = id;
        this.accountId = accountId;
        this.name = name;
    }
}
