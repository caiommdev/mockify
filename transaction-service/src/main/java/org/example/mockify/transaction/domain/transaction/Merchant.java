package org.example.mockify.transaction.domain.transaction;

import org.example.mockify.shared.domain.ValueObject;

import java.util.Objects;

public record Merchant(String merchantId, String merchantName) implements ValueObject {

    public Merchant {
        Objects.requireNonNull(merchantId, "Merchant ID cannot be null");
        Objects.requireNonNull(merchantName, "Merchant name cannot be null");
    }
}
