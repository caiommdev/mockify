package org.example.mockify.favorite.infrastructure.favorite;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FavoriteListSpringDataRepository extends JpaRepository<FavoriteListJpaEntity, UUID> {

    Optional<FavoriteListJpaEntity> findByAccountId(UUID accountId);
}
