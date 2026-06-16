package org.example.mockify.transaction.application.transaction;

import lombok.RequiredArgsConstructor;
import org.example.mockify.transaction.domain.rule.AccountSummary;
import org.example.mockify.transaction.domain.rule.AccountSummaryProvider;
import org.example.mockify.transaction.domain.rule.AuthorizationContext;
import org.example.mockify.transaction.domain.rule.AuthorizationResult;
import org.example.mockify.transaction.domain.rule.FraudRuleChain;
import org.example.mockify.transaction.domain.transaction.Merchant;
import org.example.mockify.transaction.domain.transaction.Transaction;
import org.example.mockify.transaction.domain.transaction.TransactionAmount;
import org.example.mockify.transaction.domain.transaction.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorizeTransactionService implements AuthorizeTransactionUseCase {

    private final TransactionRepository transactionRepository;
    private final FraudRuleChain fraudRuleChain;
    private final AccountSummaryProvider accountSummaryProvider;

    @Override
    public TransactionAuthorizationResult authorize(AuthorizeTransactionCommand command) {
        AccountSummary accountSummary = accountSummaryProvider.summaryFor(command.accountId());

        TransactionAmount amount = new TransactionAmount(command.amount(), command.currency());
        Merchant merchant = new Merchant(command.merchantId(), command.merchantName());
        Instant now = Instant.now();

        AuthorizationContext context = new AuthorizationContext(
                command.accountId(), accountSummary, amount, merchant, now
        );

        Instant windowStart = now.minusSeconds(120);
        List<Transaction> recentTransactions = transactionRepository.findRecentTransactionsFor(
                command.accountId(), windowStart
        );

        AuthorizationResult result = fraudRuleChain.evaluate(context, recentTransactions);

        Transaction transaction = new Transaction(command.accountId(), amount, merchant);
        if (result.isApproved()) {
            transaction.approve();
        } else {
            transaction.deny(result.violations());
        }
        Transaction saved = transactionRepository.save(transaction);

        return new TransactionAuthorizationResult(saved, result);
    }
}
