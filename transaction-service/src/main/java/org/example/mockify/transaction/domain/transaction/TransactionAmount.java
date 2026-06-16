package org.example.mockify.transaction.domain.transaction;

import org.example.mockify.shared.domain.DomainException;
import org.example.mockify.shared.domain.ValueObject;

import java.math.BigDecimal;
import java.util.Objects;

public record TransactionAmount(BigDecimal value, String currency) implements ValueObject {

    public TransactionAmount {
        Objects.requireNonNull(value, "Amount value cannot be null");
        Objects.requireNonNull(currency, "Currency cannot be null");
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("amount-must-be-positive");
        }
        if (currency.isBlank()) {
            throw new DomainException("currency-cannot-be-blank");
        }
    }

    // BigDecimal.equals() compara escala além do valor numérico (49.90 != 49.9000),
    // o que quebra a detecção de duplicatas após round-trip pela JPA com scale=4.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransactionAmount other)) return false;
        return this.value.compareTo(other.value) == 0
                && this.currency.equals(other.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value.stripTrailingZeros(), currency);
    }
}
