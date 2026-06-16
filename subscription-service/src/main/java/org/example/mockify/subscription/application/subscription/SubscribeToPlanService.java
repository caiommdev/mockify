package org.example.mockify.subscription.application.subscription;

import lombok.RequiredArgsConstructor;
import org.example.mockify.shared.domain.DomainException;
import org.example.mockify.subscription.domain.subscription.Plan;
import org.example.mockify.subscription.domain.subscription.Subscription;
import org.example.mockify.subscription.domain.subscription.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscribeToPlanService implements SubscribeToPlanUseCase {

    private final SubscriptionRepository subscriptionRepository;

    @Override
    public String subscribeToPlan(SubscribeToPlanCommand command) {
        UUID accountId = UUID.fromString(command.accountId());

        subscriptionRepository.findActiveByAccountId(accountId).ifPresent(existing -> {
            throw new DomainException("account-already-subscribed");
        });

        Plan plan = parsePlan(command.plan());
        Subscription subscription = new Subscription(accountId, plan);
        subscriptionRepository.save(subscription);
        return subscription.getId().value().toString();
    }

    private Plan parsePlan(String plan) {
        try {
            return Plan.valueOf(plan.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new DomainException("invalid-plan");
        }
    }
}
