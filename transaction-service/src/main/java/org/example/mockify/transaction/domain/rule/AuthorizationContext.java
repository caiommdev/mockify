package org.example.mockify.transaction.domain.rule;

import org.example.mockify.transaction.domain.transaction.Merchant;
import org.example.mockify.transaction.domain.transaction.TransactionAmount;
import org.example.mockify.shared.domain.ValueObject;

import java.time.Instant;
import java.util.UUID;

public record AuthorizationContext(
        UUID accountId,
        AccountSummary accountSummary,
        TransactionAmount amount,
        Merchant merchant,
        Instant occurredAt
) implements ValueObject {}
