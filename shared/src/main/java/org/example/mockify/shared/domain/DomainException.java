package org.example.mockify.shared.domain;

import java.util.List;

public class DomainException extends RuntimeException {

    private final List<String> violations;

    public DomainException(List<String> violations) {
        super(String.join(", ", violations));
        this.violations = List.copyOf(violations);
    }

    public DomainException(String violation) {
        this(List.of(violation));
    }

    public List<String> getViolations() {
        return violations;
    }
}
