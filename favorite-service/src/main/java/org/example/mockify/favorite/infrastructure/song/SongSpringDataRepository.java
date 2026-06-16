package org.example.mockify.favorite.infrastructure.song;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SongSpringDataRepository extends JpaRepository<SongJpaEntity, UUID> {}
