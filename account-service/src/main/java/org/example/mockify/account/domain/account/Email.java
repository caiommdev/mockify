package org.example.mockify.account.domain.account;

import org.example.mockify.shared.domain.DomainException;
import org.example.mockify.shared.domain.ValueObject;

import java.util.Objects;
import java.util.regex.Pattern;

public record Email(String value) implements ValueObject {

    private static final Pattern PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public Email {
        Objects.requireNonNull(value, "Email cannot be null");
        if (!PATTERN.matcher(value).matches()) {
            throw new DomainException("invalid-email-format");
        }
    }
}
