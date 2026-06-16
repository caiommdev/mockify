package org.example.mockify.transaction.application.transaction;

import org.example.mockify.transaction.domain.rule.AuthorizationResult;
import org.example.mockify.transaction.domain.transaction.Transaction;

public record TransactionAuthorizationResult(Transaction transaction, AuthorizationResult authorizationResult) {}
