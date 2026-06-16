package org.example.mockify.subscription.application.subscription;

import org.example.mockify.shared.domain.DomainException;
import org.example.mockify.subscription.domain.subscription.Plan;
import org.example.mockify.subscription.domain.subscription.Subscription;
import org.example.mockify.subscription.domain.subscription.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SubscribeToPlanServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    private SubscribeToPlanService service;
    private final UUID accountId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        service = new SubscribeToPlanService(subscriptionRepository);
    }

    @Test
    void should_subscribe_account_to_plan() {
        given(subscriptionRepository.findActiveByAccountId(accountId)).willReturn(Optional.empty());
        given(subscriptionRepository.save(any(Subscription.class))).willAnswer(inv -> inv.getArgument(0));

        service.subscribeToPlan(new SubscribeToPlanCommand(accountId.toString(), "PREMIUM"));

        verify(subscriptionRepository).save(any(Subscription.class));
    }

    @Test
    void should_throw_when_account_already_has_active_plan() {
        Subscription existing = new Subscription(accountId, Plan.FREE);
        given(subscriptionRepository.findActiveByAccountId(accountId)).willReturn(Optional.of(existing));

        assertThatThrownBy(() -> service.subscribeToPlan(new SubscribeToPlanCommand(accountId.toString(), "PREMIUM")))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("account-already-subscribed");
    }

    @Test
    void should_throw_for_invalid_plan_name() {
        given(subscriptionRepository.findActiveByAccountId(accountId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.subscribeToPlan(new SubscribeToPlanCommand(accountId.toString(), "PLATINUM")))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("invalid-plan");
    }
}
