package org.example.mockify.favorite.domain.song;

import java.util.List;
import java.util.Optional;

public interface SongRepository {

    Song save(Song song);

    Optional<Song> findById(SongId id);

    List<Song> findAll();
}
