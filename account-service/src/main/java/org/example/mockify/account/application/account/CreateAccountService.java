package org.example.mockify.account.application.account;

import lombok.RequiredArgsConstructor;
import org.example.mockify.account.domain.account.Account;
import org.example.mockify.account.domain.account.AccountId;
import org.example.mockify.account.domain.account.AccountRepository;
import org.example.mockify.account.domain.account.Email;
import org.example.mockify.account.domain.account.FullName;
import org.example.mockify.shared.domain.DomainException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateAccountService implements CreateAccountUseCase {

    private final AccountRepository accountRepository;

    @Override
    public AccountId createAccount(CreateAccountCommand command) {
        Email email = new Email(command.email());

        if (accountRepository.existsByEmail(email)) {
            throw new DomainException("email-already-registered");
        }

        AccountId id = AccountId.generate();
        Account account = new Account(id, email, new FullName(command.fullName()));
        accountRepository.save(account);
        return id;
    }
}
