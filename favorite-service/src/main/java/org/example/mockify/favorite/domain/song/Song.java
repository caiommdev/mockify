package org.example.mockify.favorite.domain.song;

import lombok.Getter;
import org.example.mockify.shared.domain.AggregateRoot;

@Getter
public class Song implements AggregateRoot {

    private final SongId id;
    private final String title;
    private final String artist;
    private final Duration duration;
    private final Genre genre;

    public Song(SongId id, String title, String artist, Duration duration, Genre genre) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.genre = genre;
    }
}
