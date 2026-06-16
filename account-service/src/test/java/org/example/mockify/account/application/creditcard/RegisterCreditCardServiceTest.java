package org.example.mockify.account.application.creditcard;

import org.example.mockify.account.domain.account.Account;
import org.example.mockify.account.domain.account.AccountId;
import org.example.mockify.account.domain.account.AccountRepository;
import org.example.mockify.account.domain.account.Email;
import org.example.mockify.account.domain.account.FullName;
import org.example.mockify.shared.domain.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RegisterCreditCardServiceTest {

    @Mock
    private AccountRepository accountRepository;

    private RegisterCreditCardService service;
    private final UUID accountUuid = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        service = new RegisterCreditCardService(accountRepository);
    }

    private Account existingAccount() {
        return new Account(
                AccountId.from(accountUuid),
                new Email("user@example.com"),
                new FullName("John Doe")
        );
    }

    @Test
    void should_register_credit_card_for_existing_account() {
        given(accountRepository.findById(any(AccountId.class))).willReturn(Optional.of(existingAccount()));
        given(accountRepository.save(any(Account.class))).willAnswer(inv -> inv.getArgument(0));

        service.registerCreditCard(new RegisterCreditCardCommand(
                accountUuid.toString(), "4532015112830366", 12, 2030, "John Doe"
        ));

        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void should_throw_when_account_not_found() {
        given(accountRepository.findById(any(AccountId.class))).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.registerCreditCard(new RegisterCreditCardCommand(
                accountUuid.toString(), "4532015112830366", 12, 2030, "John Doe"
        )))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("account-not-found");
    }

    @Test
    void should_throw_when_card_is_expired() {
        given(accountRepository.findById(any(AccountId.class))).willReturn(Optional.of(existingAccount()));

        assertThatThrownBy(() -> service.registerCreditCard(new RegisterCreditCardCommand(
                accountUuid.toString(), "4532015112830366", 1, 2020, "John Doe"
        )))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("card-already-expired");
    }
}
