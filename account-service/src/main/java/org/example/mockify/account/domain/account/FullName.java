package org.example.mockify.account.domain.account;

import org.example.mockify.shared.domain.DomainException;
import org.example.mockify.shared.domain.ValueObject;

import java.util.Objects;

public record FullName(String value) implements ValueObject {

    public FullName {
        Objects.requireNonNull(value, "Full name cannot be null");
        if (value.isBlank()) {
            throw new DomainException("full-name-cannot-be-blank");
        }
    }
}
