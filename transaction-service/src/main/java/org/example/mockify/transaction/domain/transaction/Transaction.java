package org.example.mockify.transaction.domain.transaction;

import lombok.Getter;
import org.example.mockify.shared.domain.AggregateRoot;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Transaction implements AggregateRoot {

    private final TransactionId id;
    private final UUID accountId;
    private final TransactionAmount amount;
    private final Merchant merchant;
    private final Instant occurredAt;
    private TransactionStatus status;
    private final List<TransactionViolation> violations;

    public Transaction(UUID accountId, TransactionAmount amount, Merchant merchant) {
        this.id = TransactionId.generate();
        this.accountId = accountId;
        this.amount = amount;
        this.merchant = merchant;
        this.occurredAt = Instant.now();
        this.status = TransactionStatus.APPROVED;
        this.violations = new ArrayList<>();
    }

    public Transaction(TransactionId id, UUID accountId, TransactionAmount amount, Merchant merchant,
                       Instant occurredAt, TransactionStatus status, List<TransactionViolation> violations) {
        this.id = id;
        this.accountId = accountId;
        this.amount = amount;
        this.merchant = merchant;
        this.occurredAt = occurredAt;
        this.status = status;
        this.violations = new ArrayList<>(violations);
    }

    public void approve() {
        this.status = TransactionStatus.APPROVED;
        this.violations.clear();
    }

    public void deny(List<TransactionViolation> foundViolations) {
        this.status = TransactionStatus.DENIED;
        this.violations.clear();
        this.violations.addAll(foundViolations);
    }
}
