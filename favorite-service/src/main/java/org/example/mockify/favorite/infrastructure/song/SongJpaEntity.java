package org.example.mockify.favorite.infrastructure.song;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.mockify.favorite.domain.song.Duration;
import org.example.mockify.favorite.domain.song.Genre;
import org.example.mockify.favorite.domain.song.Song;
import org.example.mockify.favorite.domain.song.SongId;

import java.util.UUID;

@Entity
@Table(name = "songs")
@Getter
@NoArgsConstructor
public class SongJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String artist;

    @Column(name = "duration_ms", nullable = false)
    private long durationMs;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genre genre;

    public static SongJpaEntity fromDomain(Song song) {
        SongJpaEntity entity = new SongJpaEntity();
        entity.id = song.getId().value();
        entity.title = song.getTitle();
        entity.artist = song.getArtist();
        entity.durationMs = song.getDuration().milliseconds();
        entity.genre = song.getGenre();
        return entity;
    }

    public Song toDomain() {
        return new Song(SongId.from(id), title, artist, new Duration(durationMs), genre);
    }
}
