package org.example.mockify.transaction.infrastructure.transaction;

import lombok.RequiredArgsConstructor;
import org.example.mockify.transaction.domain.transaction.Transaction;
import org.example.mockify.transaction.domain.transaction.TransactionId;
import org.example.mockify.transaction.domain.transaction.TransactionRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaTransactionRepository implements TransactionRepository {

    private final TransactionSpringDataRepository springData;

    @Override
    public Transaction save(Transaction transaction) {
        TransactionJpaEntity saved = springData.save(TransactionJpaEntity.fromDomain(transaction));
        return saved.toDomain();
    }

    @Override
    public Optional<Transaction> findById(TransactionId id) {
        return springData.findById(id.value()).map(TransactionJpaEntity::toDomain);
    }

    @Override
    public List<Transaction> findRecentTransactionsFor(UUID accountId, Instant after) {
        return springData.findByAccountIdAndOccurredAtAfter(accountId, after).stream()
                .map(TransactionJpaEntity::toDomain)
                .toList();
    }

    @Override
    public List<Transaction> findByAccountId(UUID accountId) {
        return springData.findByAccountId(accountId).stream()
                .map(TransactionJpaEntity::toDomain)
                .toList();
    }
}
