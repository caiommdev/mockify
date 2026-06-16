package org.example.mockify.subscription.controllers.subscription.response;

import org.example.mockify.subscription.domain.subscription.Subscription;

import java.time.LocalDate;

public record SubscriptionResponse(
        String id,
        String accountId,
        String plan,
        String status,
        LocalDate startDate,
        boolean active
) {
    public static SubscriptionResponse from(Subscription sub) {
        return new SubscriptionResponse(
                sub.getId().value().toString(),
                sub.getAccountId().toString(),
                sub.getPlan().name(),
                sub.getStatus().name(),
                sub.getStartDate(),
                sub.isActive()
        );
    }
}
