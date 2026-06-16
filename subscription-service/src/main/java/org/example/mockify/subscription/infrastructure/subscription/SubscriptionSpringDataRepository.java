package org.example.mockify.subscription.infrastructure.subscription;

import org.example.mockify.subscription.domain.subscription.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionSpringDataRepository extends JpaRepository<SubscriptionJpaEntity, UUID> {

    Optional<SubscriptionJpaEntity> findByAccountIdAndStatus(UUID accountId, SubscriptionStatus status);

    List<SubscriptionJpaEntity> findAllByAccountId(UUID accountId);
}
