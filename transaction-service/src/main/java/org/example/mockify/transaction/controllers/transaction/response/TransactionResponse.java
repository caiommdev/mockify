package org.example.mockify.transaction.controllers.transaction.response;

import org.example.mockify.transaction.domain.rule.AuthorizationResult;
import org.example.mockify.transaction.domain.transaction.Transaction;
import org.example.mockify.transaction.domain.transaction.TransactionViolation;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record TransactionResponse(
        String id,
        UUID accountId,
        BigDecimal amount,
        String currency,
        String merchantId,
        String merchantName,
        String status,
        List<String> violations,
        Instant occurredAt
) {
    public static TransactionResponse from(Transaction tx, AuthorizationResult result) {
        return new TransactionResponse(
                tx.getId().value().toString(),
                tx.getAccountId(),
                tx.getAmount().value(),
                tx.getAmount().currency(),
                tx.getMerchant().merchantId(),
                tx.getMerchant().merchantName(),
                tx.getStatus().name(),
                result.violations().stream().map(TransactionViolation::code).toList(),
                tx.getOccurredAt()
        );
    }
}
