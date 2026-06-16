package org.example.mockify.transaction.domain.transaction;

import org.example.mockify.shared.domain.ValueObject;

import java.util.Objects;
import java.util.UUID;

public record TransactionId(UUID value) implements ValueObject {

    public TransactionId {
        Objects.requireNonNull(value, "TransactionId cannot be null");
    }

    public static TransactionId generate() {
        return new TransactionId(UUID.randomUUID());
    }

    public static TransactionId from(UUID value) {
        return new TransactionId(value);
    }
}
