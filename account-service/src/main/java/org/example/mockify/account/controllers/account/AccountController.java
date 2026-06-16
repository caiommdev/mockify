package org.example.mockify.account.controllers.account;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.mockify.account.application.account.CreateAccountCommand;
import org.example.mockify.account.application.account.CreateAccountUseCase;
import org.example.mockify.account.application.creditcard.RegisterCreditCardCommand;
import org.example.mockify.account.application.creditcard.RegisterCreditCardUseCase;
import org.example.mockify.account.controllers.account.request.CreateAccountRequest;
import org.example.mockify.account.controllers.account.request.RegisterCreditCardRequest;
import org.example.mockify.account.controllers.account.response.AccountResponse;
import org.example.mockify.account.controllers.account.response.AccountStatusResponse;
import org.example.mockify.account.domain.account.Account;
import org.example.mockify.account.domain.account.AccountId;
import org.example.mockify.account.domain.account.AccountRepository;
import org.example.mockify.shared.domain.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final CreateAccountUseCase createAccountUseCase;
    private final RegisterCreditCardUseCase registerCreditCardUseCase;
    private final AccountRepository accountRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse createAccount(@Valid @RequestBody CreateAccountRequest request) {
        AccountId id = createAccountUseCase.createAccount(
                new CreateAccountCommand(request.email(), request.fullName())
        );
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new DomainException("account-not-found"));
        return AccountResponse.from(account);
    }

    @GetMapping("/{accountId}")
    public AccountResponse getAccount(@PathVariable String accountId) {
        Account account = accountRepository.findById(AccountId.from(accountId))
                .orElseThrow(() -> new DomainException("account-not-found"));
        return AccountResponse.from(account);
    }

    @PostMapping("/{accountId}/credit-card")
    public AccountResponse registerCreditCard(
            @PathVariable String accountId,
            @Valid @RequestBody RegisterCreditCardRequest request
    ) {
        registerCreditCardUseCase.registerCreditCard(new RegisterCreditCardCommand(
                accountId,
                request.cardNumber(),
                request.expiryMonth(),
                request.expiryYear(),
                request.holderName()
        ));
        Account account = accountRepository.findById(AccountId.from(accountId))
                .orElseThrow(() -> new DomainException("account-not-found"));
        return AccountResponse.from(account);
    }

    @GetMapping("/{accountId}/status")
    public AccountStatusResponse getAccountStatus(@PathVariable String accountId) {
        Account account = accountRepository.findById(AccountId.from(accountId))
                .orElseThrow(() -> new DomainException("account-not-found"));
        return new AccountStatusResponse(accountId, account.hasActiveCreditCard());
    }
}
