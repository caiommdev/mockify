package org.example.mockify.transaction.domain.rule;

import org.example.mockify.transaction.domain.transaction.Merchant;
import org.example.mockify.transaction.domain.transaction.Transaction;
import org.example.mockify.transaction.domain.transaction.TransactionAmount;
import org.example.mockify.transaction.domain.transaction.TransactionViolation;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class HighFrequencySmallIntervalRuleTest {

    private final HighFrequencySmallIntervalRule rule = new HighFrequencySmallIntervalRule();
    private final UUID accountId = UUID.randomUUID();
    private final TransactionAmount amount = new TransactionAmount(new BigDecimal("10.00"), "BRL");
    private final Merchant merchant = new Merchant("M001", "Spotify");
    private final Instant now = Instant.now();

    private AuthorizationContext context() {
        AccountSummary summary = new AccountSummary(accountId, true, true);
        return new AuthorizationContext(accountId, summary, amount, merchant, now);
    }

    private Transaction transactionAt(Instant time) {
        Transaction tx = new Transaction(accountId, amount, merchant);
        return new org.example.mockify.transaction.domain.transaction.Transaction(
                tx.getId(), accountId, amount, merchant, time,
                org.example.mockify.transaction.domain.transaction.TransactionStatus.APPROVED, List.of()
        );
    }

    @Test
    void should_return_empty_when_fewer_than_three_transactions_in_window() {
        List<Transaction> recent = List.of(
                transactionAt(now.minusSeconds(30)),
                transactionAt(now.minusSeconds(60))
        );
        Optional<TransactionViolation> result = rule.evaluate(context(), recent);
        assertThat(result).isEmpty();
    }

    @Test
    void should_return_violation_when_three_or_more_transactions_in_two_minutes() {
        List<Transaction> recent = List.of(
                transactionAt(now.minusSeconds(30)),
                transactionAt(now.minusSeconds(60)),
                transactionAt(now.minusSeconds(90))
        );
        Optional<TransactionViolation> result = rule.evaluate(context(), recent);
        assertThat(result).isPresent();
        assertThat(result.get().code()).isEqualTo("high-frequency-small-interval");
    }

    @Test
    void should_not_count_transactions_older_than_two_minutes() {
        List<Transaction> recent = List.of(
                transactionAt(now.minusSeconds(30)),
                transactionAt(now.minusSeconds(60)),
                transactionAt(now.minusSeconds(150)) // outside 2-minute window
        );
        Optional<TransactionViolation> result = rule.evaluate(context(), recent);
        assertThat(result).isEmpty();
    }
}
