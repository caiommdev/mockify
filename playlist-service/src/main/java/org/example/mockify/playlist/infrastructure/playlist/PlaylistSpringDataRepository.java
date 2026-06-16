package org.example.mockify.playlist.infrastructure.playlist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PlaylistSpringDataRepository extends JpaRepository<PlaylistJpaEntity, UUID> {

    List<PlaylistJpaEntity> findByAccountId(UUID accountId);
}
