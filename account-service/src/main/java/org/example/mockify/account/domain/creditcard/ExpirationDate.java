package org.example.mockify.account.domain.creditcard;

import org.example.mockify.shared.domain.DomainException;
import org.example.mockify.shared.domain.ValueObject;

import java.time.YearMonth;

public record ExpirationDate(int month, int year) implements ValueObject {

    public ExpirationDate {
        if (month < 1 || month > 12) {
            throw new DomainException("invalid-expiration-month");
        }
        if (year < 2000 || year > 2100) {
            throw new DomainException("invalid-expiration-year");
        }
    }

    public boolean isExpired() {
        return YearMonth.of(year, month).isBefore(YearMonth.now());
    }
}
