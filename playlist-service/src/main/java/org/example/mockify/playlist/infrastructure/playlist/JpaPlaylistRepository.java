package org.example.mockify.playlist.infrastructure.playlist;

import lombok.RequiredArgsConstructor;
import org.example.mockify.playlist.domain.playlist.Playlist;
import org.example.mockify.playlist.domain.playlist.PlaylistEntry;
import org.example.mockify.playlist.domain.playlist.PlaylistId;
import org.example.mockify.playlist.domain.playlist.PlaylistRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JpaPlaylistRepository implements PlaylistRepository {

    private final PlaylistSpringDataRepository springData;

    @Override
    public Playlist save(Playlist playlist) {
        PlaylistJpaEntity entity = toJpa(playlist);
        PlaylistJpaEntity saved = springData.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Playlist> findById(PlaylistId id) {
        return springData.findById(id.value()).map(this::toDomain);
    }

    @Override
    public List<Playlist> findByAccountId(UUID accountId) {
        return springData.findByAccountId(accountId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(PlaylistId id) {
        springData.deleteById(id.value());
    }

    private PlaylistJpaEntity toJpa(Playlist playlist) {
        PlaylistJpaEntity entity = new PlaylistJpaEntity(
                playlist.getId().value(),
                playlist.getAccountId(),
                playlist.getName()
        );
        List<PlaylistEntryJpaElement> elements = playlist.getEntries().stream()
                .map(e -> new PlaylistEntryJpaElement(e.songId(), e.position()))
                .collect(Collectors.toList());
        entity.setEntries(elements);
        return entity;
    }

    private Playlist toDomain(PlaylistJpaEntity entity) {
        List<PlaylistEntry> entries = entity.getEntries().stream()
                .map(e -> new PlaylistEntry(e.getSongId(), e.getPosition()))
                .collect(Collectors.toList());
        return new Playlist(
                PlaylistId.from(entity.getId()),
                entity.getAccountId(),
                entity.getName(),
                entries
        );
    }
}
