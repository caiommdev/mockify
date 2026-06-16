package org.example.mockify.transaction.domain.rule;

import org.example.mockify.shared.domain.ValueObject;

import java.util.UUID;

public record AccountSummary(
        UUID accountId,
        boolean hasActiveCreditCard,
        boolean hasActiveSubscription
) implements ValueObject {}
