package org.example.mockify.account.domain.account;

import java.util.Optional;

public interface AccountRepository {

    Account save(Account account);

    Optional<Account> findById(AccountId id);

    Optional<Account> findByEmail(Email email);

    boolean existsByEmail(Email email);
}
