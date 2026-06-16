package org.example.mockify.subscription.infrastructure.subscription;

import lombok.RequiredArgsConstructor;
import org.example.mockify.subscription.domain.subscription.Subscription;
import org.example.mockify.subscription.domain.subscription.SubscriptionId;
import org.example.mockify.subscription.domain.subscription.SubscriptionRepository;
import org.example.mockify.subscription.domain.subscription.SubscriptionStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaSubscriptionRepository implements SubscriptionRepository {

    private final SubscriptionSpringDataRepository springData;

    @Override
    public Subscription save(Subscription subscription) {
        SubscriptionJpaEntity saved = springData.save(SubscriptionJpaEntity.fromDomain(subscription));
        return saved.toDomain();
    }

    @Override
    public Optional<Subscription> findById(SubscriptionId id) {
        return springData.findById(id.value()).map(SubscriptionJpaEntity::toDomain);
    }

    @Override
    public Optional<Subscription> findActiveByAccountId(UUID accountId) {
        return springData.findByAccountIdAndStatus(accountId, SubscriptionStatus.ACTIVE)
                .map(SubscriptionJpaEntity::toDomain);
    }

    @Override
    public List<Subscription> findAllByAccountId(UUID accountId) {
        return springData.findAllByAccountId(accountId).stream()
                .map(SubscriptionJpaEntity::toDomain)
                .toList();
    }
}
