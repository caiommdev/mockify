package org.example.mockify.transaction.domain.rule;

import org.example.mockify.shared.domain.ValueObject;
import org.example.mockify.transaction.domain.transaction.TransactionViolation;

import java.util.List;

public record AuthorizationResult(boolean isApproved, List<TransactionViolation> violations) implements ValueObject {

    public static AuthorizationResult approved() {
        return new AuthorizationResult(true, List.of());
    }

    public static AuthorizationResult denied(List<TransactionViolation> violations) {
        return new AuthorizationResult(false, List.copyOf(violations));
    }
}
