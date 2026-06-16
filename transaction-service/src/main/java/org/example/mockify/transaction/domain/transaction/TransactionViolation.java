package org.example.mockify.transaction.domain.transaction;

import org.example.mockify.shared.domain.ValueObject;

import java.util.Objects;

public record TransactionViolation(String code) implements ValueObject {

    public TransactionViolation {
        Objects.requireNonNull(code, "Violation code cannot be null");
    }
}
