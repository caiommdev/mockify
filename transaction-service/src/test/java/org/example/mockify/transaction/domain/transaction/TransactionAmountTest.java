package org.example.mockify.transaction.domain.transaction;

import org.example.mockify.shared.domain.DomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TransactionAmountTest {

    @Test
    void should_accept_positive_amount() {
        TransactionAmount amount = new TransactionAmount(new BigDecimal("100.00"), "BRL");
        assertThat(amount.value()).isEqualByComparingTo("100.00");
        assertThat(amount.currency()).isEqualTo("BRL");
    }

    @Test
    void should_reject_zero_amount() {
        assertThatThrownBy(() -> new TransactionAmount(BigDecimal.ZERO, "BRL"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("amount-must-be-positive");
    }

    @Test
    void should_reject_negative_amount() {
        assertThatThrownBy(() -> new TransactionAmount(new BigDecimal("-1.00"), "BRL"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("amount-must-be-positive");
    }

    @Test
    void should_be_equal_when_value_and_currency_match() {
        TransactionAmount a = new TransactionAmount(new BigDecimal("50.00"), "USD");
        TransactionAmount b = new TransactionAmount(new BigDecimal("50.00"), "USD");
        assertThat(a).isEqualTo(b);
    }

    @Test
    void should_not_be_equal_when_currency_differs() {
        TransactionAmount a = new TransactionAmount(new BigDecimal("50.00"), "USD");
        TransactionAmount b = new TransactionAmount(new BigDecimal("50.00"), "BRL");
        assertThat(a).isNotEqualTo(b);
    }
}
