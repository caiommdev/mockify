package org.example.mockify.account.application.account;

import org.example.mockify.account.domain.account.AccountId;

public interface CreateAccountUseCase {

    AccountId createAccount(CreateAccountCommand command);
}
