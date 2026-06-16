package org.example.mockify.transaction.application.transaction;

public interface AuthorizeTransactionUseCase {

    TransactionAuthorizationResult authorize(AuthorizeTransactionCommand command);
}
