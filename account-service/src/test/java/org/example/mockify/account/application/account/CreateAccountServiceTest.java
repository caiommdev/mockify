package org.example.mockify.account.application.account;

import org.example.mockify.account.domain.account.Account;
import org.example.mockify.account.domain.account.AccountId;
import org.example.mockify.account.domain.account.AccountRepository;
import org.example.mockify.account.domain.account.Email;
import org.example.mockify.shared.domain.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateAccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    private CreateAccountService service;

    @BeforeEach
    void setUp() {
        service = new CreateAccountService(accountRepository);
    }

    @Test
    void should_create_and_persist_account() {
        given(accountRepository.existsByEmail(any(Email.class))).willReturn(false);
        given(accountRepository.save(any(Account.class))).willAnswer(inv -> inv.getArgument(0));

        AccountId id = service.createAccount(new CreateAccountCommand("user@example.com", "John Doe"));

        assertThat(id).isNotNull();
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void should_throw_when_email_already_registered() {
        given(accountRepository.existsByEmail(any(Email.class))).willReturn(true);

        assertThatThrownBy(() -> service.createAccount(new CreateAccountCommand("user@example.com", "John Doe")))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("email-already-registered");
    }
}
