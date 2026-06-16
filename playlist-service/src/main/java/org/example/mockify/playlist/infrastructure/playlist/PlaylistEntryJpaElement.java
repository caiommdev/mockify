package org.example.mockify.playlist.infrastructure.playlist;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistEntryJpaElement {

    private UUID songId;
    private int position;
}
