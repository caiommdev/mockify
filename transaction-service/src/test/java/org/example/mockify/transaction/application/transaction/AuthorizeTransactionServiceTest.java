package org.example.mockify.transaction.application.transaction;

import org.example.mockify.transaction.domain.rule.AccountSummary;
import org.example.mockify.transaction.domain.rule.AccountSummaryProvider;
import org.example.mockify.transaction.domain.rule.AuthorizationResult;
import org.example.mockify.transaction.domain.rule.CardNotActiveRule;
import org.example.mockify.transaction.domain.rule.FraudRuleChain;
import org.example.mockify.transaction.domain.transaction.Transaction;
import org.example.mockify.transaction.domain.transaction.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthorizeTransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountSummaryProvider accountSummaryProvider;

    private AuthorizeTransactionService service;
    private final UUID accountId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        FraudRuleChain chain = new FraudRuleChain(List.of(new CardNotActiveRule()));
        service = new AuthorizeTransactionService(transactionRepository, chain, accountSummaryProvider);
    }

    @Test
    void should_approve_clean_transaction() {
        AccountSummary summary = new AccountSummary(accountId, true, true);
        given(accountSummaryProvider.summaryFor(accountId)).willReturn(summary);
        given(transactionRepository.findRecentTransactionsFor(any(), any())).willReturn(List.of());
        given(transactionRepository.save(any(Transaction.class))).willAnswer(inv -> inv.getArgument(0));

        AuthorizeTransactionCommand command = new AuthorizeTransactionCommand(
                accountId, new BigDecimal("100.00"), "BRL", "M001", "Spotify"
        );
        TransactionAuthorizationResult result = service.authorize(command);

        assertThat(result.authorizationResult().isApproved()).isTrue();
        assertThat(result.authorizationResult().violations()).isEmpty();
    }

    @Test
    void should_deny_transaction_with_inactive_card() {
        AccountSummary summary = new AccountSummary(accountId, false, true);
        given(accountSummaryProvider.summaryFor(accountId)).willReturn(summary);
        given(transactionRepository.findRecentTransactionsFor(any(), any())).willReturn(List.of());
        given(transactionRepository.save(any(Transaction.class))).willAnswer(inv -> inv.getArgument(0));

        AuthorizeTransactionCommand command = new AuthorizeTransactionCommand(
                accountId, new BigDecimal("100.00"), "BRL", "M001", "Spotify"
        );
        TransactionAuthorizationResult result = service.authorize(command);

        assertThat(result.authorizationResult().isApproved()).isFalse();
        assertThat(result.authorizationResult().violations())
                .anyMatch(v -> v.code().equals("card-not-active"));
    }
}
