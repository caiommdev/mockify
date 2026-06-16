package org.example.mockify.subscription.infrastructure.subscription;

import org.example.mockify.subscription.domain.subscription.Plan;
import org.example.mockify.subscription.domain.subscription.Subscription;
import org.example.mockify.subscription.domain.subscription.SubscriptionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaSubscriptionRepository.class)
class JpaSubscriptionRepositoryTest {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Test
    void should_persist_and_find_active_subscription() {
        UUID accountId = UUID.randomUUID();
        Subscription subscription = new Subscription(accountId, Plan.PREMIUM);
        subscriptionRepository.save(subscription);

        Optional<Subscription> found = subscriptionRepository.findActiveByAccountId(accountId);

        assertThat(found).isPresent();
        assertThat(found.get().getPlan()).isEqualTo(Plan.PREMIUM);
        assertThat(found.get().isActive()).isTrue();
    }

    @Test
    void should_not_find_active_subscription_after_cancellation() {
        UUID accountId = UUID.randomUUID();
        Subscription subscription = new Subscription(accountId, Plan.FREE);
        subscriptionRepository.save(subscription);

        subscription.cancel();
        subscriptionRepository.save(subscription);

        Optional<Subscription> found = subscriptionRepository.findActiveByAccountId(accountId);
        assertThat(found).isEmpty();
    }
}
