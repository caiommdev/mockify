package org.example.mockify.subscription.domain.subscription;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository {

    Subscription save(Subscription subscription);

    Optional<Subscription> findById(SubscriptionId id);

    Optional<Subscription> findActiveByAccountId(UUID accountId);

    List<Subscription> findAllByAccountId(UUID accountId);
}
