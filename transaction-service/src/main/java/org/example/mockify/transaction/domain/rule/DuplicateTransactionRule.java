package org.example.mockify.transaction.domain.rule;

import org.example.mockify.transaction.domain.transaction.Transaction;
import org.example.mockify.transaction.domain.transaction.TransactionViolation;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
public class DuplicateTransactionRule implements FraudRule {

    private static final long INTERVAL_SECONDS = 120;
    private static final int MAX_DUPLICATES = 2;
    private static final TransactionViolation VIOLATION =
            new TransactionViolation("duplicate-transaction");

    @Override
    public Optional<TransactionViolation> evaluate(AuthorizationContext context, List<Transaction> recentTransactions) {
        Instant windowStart = context.occurredAt().minusSeconds(INTERVAL_SECONDS);

        long duplicateCount = recentTransactions.stream()
                .filter(t -> t.getAccountId().equals(context.accountId()))
                .filter(t -> !t.getOccurredAt().isBefore(windowStart))
                .filter(t -> t.getAmount().equals(context.amount()))
                .filter(t -> t.getMerchant().equals(context.merchant()))
                .count();

        if (duplicateCount >= MAX_DUPLICATES) {
            return Optional.of(VIOLATION);
        }
        return Optional.empty();
    }
}
