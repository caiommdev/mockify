package org.example.mockify.subscription.application.subscription;

import lombok.RequiredArgsConstructor;
import org.example.mockify.shared.domain.DomainException;
import org.example.mockify.subscription.domain.subscription.Subscription;
import org.example.mockify.subscription.domain.subscription.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CancelSubscriptionService implements CancelSubscriptionUseCase {

    private final SubscriptionRepository subscriptionRepository;

    @Override
    public void cancelSubscription(String accountId) {
        Subscription subscription = subscriptionRepository.findActiveByAccountId(UUID.fromString(accountId))
                .orElseThrow(() -> new DomainException("no-active-subscription"));

        subscription.cancel();
        subscriptionRepository.save(subscription);
    }
}
