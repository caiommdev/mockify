package org.example.mockify.account.infrastructure.account;

import lombok.RequiredArgsConstructor;
import org.example.mockify.account.domain.account.Account;
import org.example.mockify.account.domain.account.AccountId;
import org.example.mockify.account.domain.account.AccountRepository;
import org.example.mockify.account.domain.account.Email;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaAccountRepository implements AccountRepository {

    private final AccountSpringDataRepository springData;

    @Override
    public Account save(Account account) {
        AccountJpaEntity saved = springData.save(AccountJpaEntity.fromDomain(account));
        return saved.toDomain();
    }

    @Override
    public Optional<Account> findById(AccountId id) {
        return springData.findById(id.value()).map(AccountJpaEntity::toDomain);
    }

    @Override
    public Optional<Account> findByEmail(Email email) {
        return springData.findByEmail(email.value()).map(AccountJpaEntity::toDomain);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return springData.existsByEmail(email.value());
    }
}
