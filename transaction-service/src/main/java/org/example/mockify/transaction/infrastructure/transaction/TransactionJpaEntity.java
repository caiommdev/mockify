package org.example.mockify.transaction.infrastructure.transaction;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.mockify.transaction.domain.transaction.Merchant;
import org.example.mockify.transaction.domain.transaction.Transaction;
import org.example.mockify.transaction.domain.transaction.TransactionAmount;
import org.example.mockify.transaction.domain.transaction.TransactionId;
import org.example.mockify.transaction.domain.transaction.TransactionStatus;
import org.example.mockify.transaction.domain.transaction.TransactionViolation;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@NoArgsConstructor
public class TransactionJpaEntity {

    @Id
    private UUID id;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(name = "merchant_id", nullable = false)
    private String merchantId;

    @Column(name = "merchant_name", nullable = false)
    private String merchantName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "transaction_violations", joinColumns = @JoinColumn(name = "transaction_id"))
    @Column(name = "violation_code")
    private List<String> violationCodes;

    public static TransactionJpaEntity fromDomain(Transaction tx) {
        TransactionJpaEntity entity = new TransactionJpaEntity();
        entity.id = tx.getId().value();
        entity.accountId = tx.getAccountId();
        entity.amount = tx.getAmount().value();
        entity.currency = tx.getAmount().currency();
        entity.merchantId = tx.getMerchant().merchantId();
        entity.merchantName = tx.getMerchant().merchantName();
        entity.status = tx.getStatus();
        entity.occurredAt = tx.getOccurredAt();
        entity.violationCodes = tx.getViolations().stream()
                .map(TransactionViolation::code)
                .toList();
        return entity;
    }

    public Transaction toDomain() {
        List<TransactionViolation> violations = violationCodes.stream()
                .map(TransactionViolation::new)
                .toList();
        return new Transaction(
                TransactionId.from(id),
                accountId,
                new TransactionAmount(amount, currency),
                new Merchant(merchantId, merchantName),
                occurredAt,
                status,
                violations
        );
    }
}
