package org.example.mockify.transaction.domain.transaction;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {

    Transaction save(Transaction transaction);

    Optional<Transaction> findById(TransactionId id);

    List<Transaction> findRecentTransactionsFor(UUID accountId, Instant after);

    List<Transaction> findByAccountId(UUID accountId);
}
