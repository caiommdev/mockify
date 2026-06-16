package org.example.mockify.favorite.controllers.favorite.response;

import org.example.mockify.favorite.domain.song.Song;

public record SongResponse(String id, String title, String artist, long durationMs, String genre) {

    public static SongResponse from(Song song) {
        return new SongResponse(
                song.getId().value().toString(),
                song.getTitle(),
                song.getArtist(),
                song.getDuration().milliseconds(),
                song.getGenre().name()
        );
    }
}
