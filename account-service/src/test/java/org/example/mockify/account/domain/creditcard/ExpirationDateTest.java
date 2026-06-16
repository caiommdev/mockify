package org.example.mockify.account.domain.creditcard;

import org.example.mockify.shared.domain.DomainException;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExpirationDateTest {

    @Test
    void should_accept_valid_future_date() {
        ExpirationDate date = new ExpirationDate(12, 2030);
        assertThat(date.isExpired()).isFalse();
    }

    @Test
    void should_reject_month_zero() {
        assertThatThrownBy(() -> new ExpirationDate(0, 2030))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("invalid-expiration-month");
    }

    @Test
    void should_reject_month_thirteen() {
        assertThatThrownBy(() -> new ExpirationDate(13, 2030))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("invalid-expiration-month");
    }

    @Test
    void should_detect_expired_card() {
        ExpirationDate pastDate = new ExpirationDate(1, 2020);
        assertThat(pastDate.isExpired()).isTrue();
    }

    @Test
    void should_detect_current_month_as_not_expired() {
        YearMonth now = YearMonth.now();
        ExpirationDate date = new ExpirationDate(now.getMonthValue(), now.getYear());
        assertThat(date.isExpired()).isFalse();
    }
}
