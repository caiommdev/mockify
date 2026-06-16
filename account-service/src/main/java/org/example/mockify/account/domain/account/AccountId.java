package org.example.mockify.account.domain.account;

import org.example.mockify.shared.domain.ValueObject;

import java.util.Objects;
import java.util.UUID;

public record AccountId(UUID value) implements ValueObject {

    public AccountId {
        Objects.requireNonNull(value, "AccountId value cannot be null");
    }

    public static AccountId generate() {
        return new AccountId(UUID.randomUUID());
    }

    public static AccountId from(String value) {
        return new AccountId(UUID.fromString(value));
    }

    public static AccountId from(UUID value) {
        return new AccountId(value);
    }
}
