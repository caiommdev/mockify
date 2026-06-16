package org.example.mockify.favorite.infrastructure.song;

import lombok.RequiredArgsConstructor;
import org.example.mockify.favorite.domain.song.Song;
import org.example.mockify.favorite.domain.song.SongId;
import org.example.mockify.favorite.domain.song.SongRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaSongRepository implements SongRepository {

    private final SongSpringDataRepository springData;

    @Override
    public Song save(Song song) {
        return springData.save(SongJpaEntity.fromDomain(song)).toDomain();
    }

    @Override
    public Optional<Song> findById(SongId id) {
        return springData.findById(id.value()).map(SongJpaEntity::toDomain);
    }

    @Override
    public List<Song> findAll() {
        return springData.findAll().stream().map(SongJpaEntity::toDomain).toList();
    }
}
