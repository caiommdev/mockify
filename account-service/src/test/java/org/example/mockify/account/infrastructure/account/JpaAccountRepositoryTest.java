package org.example.mockify.account.infrastructure.account;

import org.example.mockify.account.domain.account.Account;
import org.example.mockify.account.domain.account.AccountId;
import org.example.mockify.account.domain.account.AccountRepository;
import org.example.mockify.account.domain.account.Email;
import org.example.mockify.account.domain.account.FullName;
import org.example.mockify.account.domain.creditcard.CardNumber;
import org.example.mockify.account.domain.creditcard.ExpirationDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaAccountRepository.class)
class JpaAccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void should_persist_and_reload_account() {
        Account account = new Account(AccountId.generate(), new Email("user@example.com"), new FullName("John Doe"));
        accountRepository.save(account);

        Optional<Account> found = accountRepository.findById(account.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getEmail().value()).isEqualTo("user@example.com");
        assertThat(found.get().getName().value()).isEqualTo("John Doe");
    }

    @Test
    void should_persist_account_with_credit_card() {
        Account account = new Account(AccountId.generate(), new Email("card@example.com"), new FullName("Card User"));
        account.registerCreditCard(new CardNumber("4532015112830366"), new ExpirationDate(12, 2030), "Card User");
        accountRepository.save(account);

        Optional<Account> found = accountRepository.findById(account.getId());

        assertThat(found).isPresent();
        assertThat(found.get().hasActiveCreditCard()).isTrue();
        assertThat(found.get().getCreditCard().getNumber().toString()).isEqualTo("**** **** **** 0366");
    }

    @Test
    void should_find_account_by_email() {
        Account account = new Account(AccountId.generate(), new Email("find@example.com"), new FullName("Find Me"));
        accountRepository.save(account);

        Optional<Account> found = accountRepository.findByEmail(new Email("find@example.com"));
        assertThat(found).isPresent();
    }

    @Test
    void should_confirm_email_exists() {
        Account account = new Account(AccountId.generate(), new Email("exists@example.com"), new FullName("Exists"));
        accountRepository.save(account);

        assertThat(accountRepository.existsByEmail(new Email("exists@example.com"))).isTrue();
        assertThat(accountRepository.existsByEmail(new Email("notfound@example.com"))).isFalse();
    }
}
