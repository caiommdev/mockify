package org.example.mockify.account.domain.creditcard;

import org.example.mockify.shared.domain.DomainException;
import org.example.mockify.shared.domain.ValueObject;

import java.util.Objects;

public record CardNumber(String value) implements ValueObject {

    public CardNumber {
        Objects.requireNonNull(value, "Card number cannot be null");
        value = value.replaceAll("\\s+", "");
        if (!value.matches("\\d{13,19}")) {
            throw new DomainException("invalid-card-number-format");
        }
        if (!isValidLuhn(value)) {
            throw new DomainException("invalid-card-number-luhn");
        }
    }

    private static boolean isValidLuhn(String number) {
        int sum = 0;
        boolean alternate = false;
        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Character.getNumericValue(number.charAt(i));
            if (alternate) {
                n *= 2;
                if (n > 9) n -= 9;
            }
            sum += n;
            alternate = !alternate;
        }
        return sum % 10 == 0;
    }

    @Override
    public String toString() {
        String cleaned = value.replaceAll("\\s+", "");
        return "**** **** **** " + cleaned.substring(cleaned.length() - 4);
    }
}
