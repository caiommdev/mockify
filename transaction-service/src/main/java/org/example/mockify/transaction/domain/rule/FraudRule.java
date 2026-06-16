package org.example.mockify.transaction.domain.rule;

import org.example.mockify.transaction.domain.transaction.Transaction;
import org.example.mockify.transaction.domain.transaction.TransactionViolation;

import java.util.List;
import java.util.Optional;

public interface FraudRule {

    Optional<TransactionViolation> evaluate(
            AuthorizationContext context,
            List<Transaction> recentTransactions
    );
}
