package org.example.mockify.favorite.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.example.mockify.favorite.domain.song.Duration;
import org.example.mockify.favorite.domain.song.Genre;
import org.example.mockify.favorite.domain.song.Song;
import org.example.mockify.favorite.domain.song.SongId;
import org.example.mockify.favorite.domain.song.SongRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SongDataSeeder implements CommandLineRunner {

    private final SongRepository songRepository;

    @Override
    public void run(String... args) {
        songRepository.save(new Song(SongId.generate(), "Bohemian Rhapsody", "Queen", new Duration(354000), Genre.ROCK));
        songRepository.save(new Song(SongId.generate(), "Hotel California", "Eagles", new Duration(391000), Genre.ROCK));
        songRepository.save(new Song(SongId.generate(), "Shape of You", "Ed Sheeran", new Duration(234000), Genre.POP));
        songRepository.save(new Song(SongId.generate(), "Blinding Lights", "The Weeknd", new Duration(200000), Genre.POP));
        songRepository.save(new Song(SongId.generate(), "Lose Yourself", "Eminem", new Duration(326000), Genre.HIP_HOP));
    }
}
