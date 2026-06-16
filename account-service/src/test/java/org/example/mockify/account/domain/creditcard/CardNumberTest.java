package org.example.mockify.account.domain.creditcard;

import org.example.mockify.shared.domain.DomainException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CardNumberTest {

    // Valid Visa test card (passes Luhn)
    private static final String VALID_NUMBER = "4532015112830366";

    @Test
    void should_accept_valid_card_number() {
        CardNumber card = new CardNumber(VALID_NUMBER);
        assertThat(card.value()).isEqualTo(VALID_NUMBER);
    }

    @Test
    void should_accept_card_number_with_spaces() {
        CardNumber card = new CardNumber("4532 0151 1283 0366");
        assertThat(card.value()).isEqualTo(VALID_NUMBER);
    }

    @Test
    void should_reject_non_numeric_card_number() {
        assertThatThrownBy(() -> new CardNumber("ABCD1234EFGH5678"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("invalid-card-number-format");
    }

    @Test
    void should_reject_card_number_that_is_too_short() {
        assertThatThrownBy(() -> new CardNumber("123456789012"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("invalid-card-number-format");
    }

    @Test
    void should_reject_card_number_failing_luhn_check() {
        assertThatThrownBy(() -> new CardNumber("4532015112830367"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("invalid-card-number-luhn");
    }

    @Test
    void should_mask_all_but_last_four_digits_in_toString() {
        CardNumber card = new CardNumber(VALID_NUMBER);
        assertThat(card.toString()).isEqualTo("**** **** **** 0366");
    }

    @Test
    void should_be_equal_when_number_is_the_same() {
        CardNumber a = new CardNumber(VALID_NUMBER);
        CardNumber b = new CardNumber(VALID_NUMBER);
        assertThat(a).isEqualTo(b);
    }
}
