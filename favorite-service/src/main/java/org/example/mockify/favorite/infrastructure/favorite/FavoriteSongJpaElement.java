package org.example.mockify.favorite.infrastructure.favorite;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteSongJpaElement {

    @Column(name = "song_id", nullable = false)
    private UUID songId;

    @Column(name = "added_at", nullable = false)
    private Instant addedAt;
}
