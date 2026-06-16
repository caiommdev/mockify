package org.example.mockify.transaction.infrastructure.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface TransactionSpringDataRepository extends JpaRepository<TransactionJpaEntity, UUID> {

    @Query("SELECT t FROM TransactionJpaEntity t WHERE t.accountId = :accountId AND t.occurredAt >= :after")
    List<TransactionJpaEntity> findByAccountIdAndOccurredAtAfter(
            @Param("accountId") UUID accountId,
            @Param("after") Instant after
    );

    List<TransactionJpaEntity> findByAccountId(UUID accountId);
}
