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

class CardNotActiveRuleTest {

    private final CardNotActiveRule rule = new CardNotActiveRule();
    private final UUID accountId = UUID.randomUUID();
    private final TransactionAmount amount = new TransactionAmount(new BigDecimal("100.00"), "BRL");
    private final Merchant merchant = new Merchant("M001", "Spotify");

    private AuthorizationContext contextWith(boolean hasActiveCreditCard) {
        AccountSummary summary = new AccountSummary(accountId, hasActiveCreditCard, true);
        return new AuthorizationContext(accountId, summary, amount, merchant, Instant.now());
    }

    @Test
    void should_return_violation_when_card_not_active() {
        Optional<TransactionViolation> result = rule.evaluate(contextWith(false), List.of());
        assertThat(result).isPresent();
        assertThat(result.get().code()).isEqualTo("card-not-active");
    }

    @Test
    void should_return_empty_when_card_is_active() {
        Optional<TransactionViolation> result = rule.evaluate(contextWith(true), List.of());
        assertThat(result).isEmpty();
    }
}
