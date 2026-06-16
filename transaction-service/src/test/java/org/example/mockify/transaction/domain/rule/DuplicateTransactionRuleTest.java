package org.example.mockify.transaction.domain.rule;

import org.example.mockify.transaction.domain.transaction.Merchant;
import org.example.mockify.transaction.domain.transaction.Transaction;
import org.example.mockify.transaction.domain.transaction.TransactionAmount;
import org.example.mockify.transaction.domain.transaction.TransactionId;
import org.example.mockify.transaction.domain.transaction.TransactionStatus;
import org.example.mockify.transaction.domain.transaction.TransactionViolation;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DuplicateTransactionRuleTest {

    private final DuplicateTransactionRule rule = new DuplicateTransactionRule();
    private final UUID accountId = UUID.randomUUID();
    private final TransactionAmount amount = new TransactionAmount(new BigDecimal("50.00"), "BRL");
    private final Merchant merchant = new Merchant("M001", "Spotify");
    private final Instant now = Instant.now();

    private AuthorizationContext context() {
        AccountSummary summary = new AccountSummary(accountId, true, true);
        return new AuthorizationContext(accountId, summary, amount, merchant, now);
    }

    private Transaction similarTransactionAt(Instant time) {
        return new Transaction(TransactionId.generate(), accountId, amount, merchant, time, TransactionStatus.APPROVED, List.of());
    }

    private Transaction differentAmountAt(Instant time) {
        return new Transaction(TransactionId.generate(), accountId,
                new TransactionAmount(new BigDecimal("99.00"), "BRL"), merchant, time, TransactionStatus.APPROVED, List.of());
    }

    private Transaction differentMerchantAt(Instant time) {
        return new Transaction(TransactionId.generate(), accountId, amount,
                new Merchant("M999", "Apple Music"), time, TransactionStatus.APPROVED, List.of());
    }

    @Test
    void should_return_empty_when_only_one_similar_transaction_in_window() {
        List<Transaction> recent = List.of(similarTransactionAt(now.minusSeconds(30)));
        assertThat(rule.evaluate(context(), recent)).isEmpty();
    }

    @Test
    void should_return_violation_when_two_similar_transactions_in_two_minutes() {
        List<Transaction> recent = List.of(
                similarTransactionAt(now.minusSeconds(30)),
                similarTransactionAt(now.minusSeconds(60))
        );
        Optional<TransactionViolation> result = rule.evaluate(context(), recent);
        assertThat(result).isPresent();
        assertThat(result.get().code()).isEqualTo("duplicate-transaction");
    }

    @Test
    void should_not_flag_same_amount_with_different_merchant() {
        List<Transaction> recent = List.of(
                differentMerchantAt(now.minusSeconds(30)),
                differentMerchantAt(now.minusSeconds(60))
        );
        assertThat(rule.evaluate(context(), recent)).isEmpty();
    }

    @Test
    void should_not_flag_same_merchant_with_different_amount() {
        List<Transaction> recent = List.of(
                differentAmountAt(now.minusSeconds(30)),
                differentAmountAt(now.minusSeconds(60))
        );
        assertThat(rule.evaluate(context(), recent)).isEmpty();
    }

    @Test
    void should_not_count_duplicates_older_than_two_minutes() {
        List<Transaction> recent = List.of(
                similarTransactionAt(now.minusSeconds(30)),
                similarTransactionAt(now.minusSeconds(150)) // outside window
        );
        assertThat(rule.evaluate(context(), recent)).isEmpty();
    }
}
