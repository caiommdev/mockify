package org.example.mockify.transaction.domain.rule;

import org.example.mockify.transaction.domain.transaction.Transaction;
import org.example.mockify.transaction.domain.transaction.TransactionViolation;

import java.util.List;
import java.util.Optional;

public class FraudRuleChain {

    private final List<FraudRule> rules;

    public FraudRuleChain(List<FraudRule> rules) {
        this.rules = List.copyOf(rules);
    }

    public AuthorizationResult evaluate(AuthorizationContext context, List<Transaction> recentTransactions) {
        List<TransactionViolation> violations = rules.stream()
                .map(rule -> rule.evaluate(context, recentTransactions))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        return violations.isEmpty()
                ? AuthorizationResult.approved()
                : AuthorizationResult.denied(violations);
    }
}
