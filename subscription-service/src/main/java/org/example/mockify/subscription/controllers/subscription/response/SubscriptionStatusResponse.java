package org.example.mockify.subscription.controllers.subscription.response;

public record SubscriptionStatusResponse(
        String accountId,
        boolean hasActiveSubscription
) {}
