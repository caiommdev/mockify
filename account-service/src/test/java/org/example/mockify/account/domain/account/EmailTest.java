package org.example.mockify.account.domain.account;

import org.example.mockify.shared.domain.DomainException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {

    @Test
    void should_accept_valid_email() {
        Email email = new Email("user@example.com");
        assertThat(email.value()).isEqualTo("user@example.com");
    }

    @Test
    void should_reject_email_without_at_sign() {
        assertThatThrownBy(() -> new Email("userexample.com"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("invalid-email-format");
    }

    @Test
    void should_reject_email_without_domain() {
        assertThatThrownBy(() -> new Email("user@"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("invalid-email-format");
    }

    @Test
    void should_reject_null_email() {
        assertThatThrownBy(() -> new Email(null))
                .isInstanceOf(NullPointerException.class);
    }
}
