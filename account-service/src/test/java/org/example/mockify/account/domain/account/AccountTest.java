package org.example.mockify.account.domain.account;

import org.example.mockify.account.domain.creditcard.CardNumber;
import org.example.mockify.account.domain.creditcard.ExpirationDate;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountTest {

    private static final String VALID_CARD = "4532015112830366";

    private Account newAccount() {
        return new Account(AccountId.generate(), new Email("user@example.com"), new FullName("John Doe"));
    }

    @Test
    void should_create_account_with_valid_data() {
        Account account = newAccount();
        assertThat(account.getId()).isNotNull();
        assertThat(account.getEmail().value()).isEqualTo("user@example.com");
        assertThat(account.getName().value()).isEqualTo("John Doe");
    }

    @Test
    void should_not_have_active_credit_card_when_none_registered() {
        Account account = newAccount();
        assertThat(account.hasActiveCreditCard()).isFalse();
    }

    @Test
    void should_have_active_credit_card_after_registration() {
        Account account = newAccount();
        account.registerCreditCard(
                new CardNumber(VALID_CARD),
                new ExpirationDate(12, 2030),
                "John Doe"
        );
        assertThat(account.hasActiveCreditCard()).isTrue();
    }

    @Test
    void should_replace_credit_card_when_new_one_registered() {
        Account account = newAccount();
        account.registerCreditCard(
                new CardNumber(VALID_CARD),
                new ExpirationDate(12, 2030),
                "John Doe"
        );
        // Register a different valid card (Mastercard test number)
        account.registerCreditCard(
                new CardNumber("5425233430109903"),
                new ExpirationDate(6, 2031),
                "John Doe"
        );
        assertThat(account.getCreditCard().getNumber().value()).isEqualTo("5425233430109903");
        assertThat(account.hasActiveCreditCard()).isTrue();
    }

    @Test
    void should_not_have_active_card_when_expired_card_registered() {
        Account account = newAccount();
        // ExpirationDate allows past dates for reconstitution; check via isExpired
        account.registerCreditCard(
                new CardNumber(VALID_CARD),
                new ExpirationDate(1, 2020),
                "John Doe"
        );
        assertThat(account.hasActiveCreditCard()).isFalse();
    }
}
