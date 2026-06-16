package org.example.mockify.favorite.domain.song;

import org.example.mockify.shared.domain.DomainException;
import org.example.mockify.shared.domain.ValueObject;

public record Duration(long milliseconds) implements ValueObject {

    public Duration {
        if (milliseconds <= 0) {
            throw new DomainException("duration-must-be-positive");
        }
    }
}
