package org.example.mockify.transaction.infrastructure.transaction;

import org.example.mockify.transaction.domain.transaction.Merchant;
import org.example.mockify.transaction.domain.transaction.Transaction;
import org.example.mockify.transaction.domain.transaction.TransactionAmount;
import org.example.mockify.transaction.domain.transaction.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaTransactionRepository.class)
class JpaTransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    private final UUID accountId = UUID.randomUUID();
    private final TransactionAmount amount = new TransactionAmount(new BigDecimal("100.00"), "BRL");
    private final Merchant merchant = new Merchant("M001", "Spotify");

    @Test
    void should_persist_and_reload_transaction() {
        Transaction tx = new Transaction(accountId, amount, merchant);
        transactionRepository.save(tx);

        List<Transaction> found = transactionRepository.findByAccountId(accountId);
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getAccountId()).isEqualTo(accountId);
    }

    @Test
    void should_find_recent_transactions_within_time_window() {
        Transaction recent = new Transaction(accountId, amount, merchant);
        transactionRepository.save(recent);

        Instant twoMinutesAgo = Instant.now().minusSeconds(120);
        List<Transaction> found = transactionRepository.findRecentTransactionsFor(accountId, twoMinutesAgo);

        assertThat(found).hasSize(1);
    }

    @Test
    void should_not_return_transactions_for_different_account() {
        Transaction tx = new Transaction(accountId, amount, merchant);
        transactionRepository.save(tx);

        UUID otherAccount = UUID.randomUUID();
        List<Transaction> found = transactionRepository.findRecentTransactionsFor(
                otherAccount, Instant.now().minusSeconds(120)
        );
        assertThat(found).isEmpty();
    }
}
