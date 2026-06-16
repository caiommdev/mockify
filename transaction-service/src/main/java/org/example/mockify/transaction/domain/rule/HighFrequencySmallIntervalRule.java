package org.example.mockify.transaction.domain.rule;

import org.example.mockify.transaction.domain.transaction.Transaction;
import org.example.mockify.transaction.domain.transaction.TransactionViolation;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
public class HighFrequencySmallIntervalRule implements FraudRule {

    private static final long INTERVAL_SECONDS = 120;
    private static final int MAX_TRANSACTIONS = 3;
    private static final TransactionViolation VIOLATION =
            new TransactionViolation("high-frequency-small-interval");

    @Override
    public Optional<TransactionViolation> evaluate(AuthorizationContext context, List<Transaction> recentTransactions) {
        Instant windowStart = context.occurredAt().minusSeconds(INTERVAL_SECONDS);

        long count = recentTransactions.stream()
                .filter(t -> t.getAccountId().equals(context.accountId()))
                .filter(t -> !t.getOccurredAt().isBefore(windowStart))
                .count();

        if (count >= MAX_TRANSACTIONS) {
            return Optional.of(VIOLATION);
        }
        return Optional.empty();
    }
}
