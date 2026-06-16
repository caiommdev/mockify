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

class FraudRuleChainTest {

    private final UUID accountId = UUID.randomUUID();
    private final TransactionAmount amount = new TransactionAmount(new BigDecimal("100.00"), "BRL");
    private final Merchant merchant = new Merchant("M001", "Spotify");

    private AuthorizationContext cleanContext() {
        AccountSummary summary = new AccountSummary(accountId, true, true);
        return new AuthorizationContext(accountId, summary, amount, merchant, Instant.now());
    }

    @Test
    void should_approve_when_all_rules_pass() {
        FraudRuleChain chain = new FraudRuleChain(List.of(
                (ctx, recent) -> Optional.empty(),
                (ctx, recent) -> Optional.empty()
        ));
        AuthorizationResult result = chain.evaluate(cleanContext(), List.of());
        assertThat(result.isApproved()).isTrue();
        assertThat(result.violations()).isEmpty();
    }

    @Test
    void should_deny_when_one_rule_fails() {
        FraudRuleChain chain = new FraudRuleChain(List.of(
                (ctx, recent) -> Optional.of(new TransactionViolation("card-not-active")),
                (ctx, recent) -> Optional.empty()
        ));
        AuthorizationResult result = chain.evaluate(cleanContext(), List.of());
        assertThat(result.isApproved()).isFalse();
        assertThat(result.violations()).hasSize(1);
        assertThat(result.violations().get(0).code()).isEqualTo("card-not-active");
    }

    @Test
    void should_collect_all_violations_when_multiple_rules_fail() {
        FraudRuleChain chain = new FraudRuleChain(List.of(
                (ctx, recent) -> Optional.of(new TransactionViolation("card-not-active")),
                (ctx, recent) -> Optional.of(new TransactionViolation("high-frequency-small-interval"))
        ));
        AuthorizationResult result = chain.evaluate(cleanContext(), List.of());
        assertThat(result.isApproved()).isFalse();
        assertThat(result.violations()).hasSize(2);
    }
}
