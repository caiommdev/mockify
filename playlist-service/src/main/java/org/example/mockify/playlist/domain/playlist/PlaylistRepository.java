package org.example.mockify.playlist.domain.playlist;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlaylistRepository {

    Playlist save(Playlist playlist);

    Optional<Playlist> findById(PlaylistId id);

    List<Playlist> findByAccountId(UUID accountId);

    void deleteById(PlaylistId id);
}
