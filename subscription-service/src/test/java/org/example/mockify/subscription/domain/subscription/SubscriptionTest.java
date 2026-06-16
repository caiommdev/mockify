package org.example.mockify.subscription.domain.subscription;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class SubscriptionTest {

    private final UUID accountId = UUID.randomUUID();

    @Test
    void should_create_active_subscription() {
        Subscription subscription = new Subscription(accountId, Plan.PREMIUM);
        assertThat(subscription.isActive()).isTrue();
        assertThat(subscription.getPlan()).isEqualTo(Plan.PREMIUM);
        assertThat(subscription.getAccountId()).isEqualTo(accountId);
    }

    @Test
    void should_cancel_subscription() {
        Subscription subscription = new Subscription(accountId, Plan.PREMIUM);
        subscription.cancel();
        assertThat(subscription.isActive()).isFalse();
        assertThat(subscription.getStatus()).isEqualTo(SubscriptionStatus.CANCELLED);
    }

    @Test
    void should_generate_unique_id_per_subscription() {
        Subscription s1 = new Subscription(accountId, Plan.FREE);
        Subscription s2 = new Subscription(accountId, Plan.PREMIUM);
        assertThat(s1.getId()).isNotEqualTo(s2.getId());
    }
}
