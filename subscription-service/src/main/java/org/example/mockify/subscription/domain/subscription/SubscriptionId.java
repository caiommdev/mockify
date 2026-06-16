package org.example.mockify.subscription.domain.subscription;

import org.example.mockify.shared.domain.ValueObject;

import java.util.Objects;
import java.util.UUID;

public record SubscriptionId(UUID value) implements ValueObject {

    public SubscriptionId {
        Objects.requireNonNull(value, "SubscriptionId value cannot be null");
    }

    public static SubscriptionId generate() {
        return new SubscriptionId(UUID.randomUUID());
    }

    public static SubscriptionId from(UUID value) {
        return new SubscriptionId(value);
    }
}
