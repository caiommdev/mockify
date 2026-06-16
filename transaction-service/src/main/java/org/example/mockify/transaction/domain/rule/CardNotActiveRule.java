package org.example.mockify.transaction.domain.rule;

import org.example.mockify.transaction.domain.transaction.Transaction;
import org.example.mockify.transaction.domain.transaction.TransactionViolation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CardNotActiveRule implements FraudRule {

    private static final TransactionViolation VIOLATION = new TransactionViolation("card-not-active");

    @Override
    public Optional<TransactionViolation> evaluate(AuthorizationContext context, List<Transaction> recentTransactions) {
        if (!context.accountSummary().hasActiveCreditCard()) {
            return Optional.of(VIOLATION);
        }
        return Optional.empty();
    }
}
