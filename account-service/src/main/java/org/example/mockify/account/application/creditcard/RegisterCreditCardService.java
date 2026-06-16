package org.example.mockify.account.application.creditcard;

import lombok.RequiredArgsConstructor;
import org.example.mockify.account.domain.account.Account;
import org.example.mockify.account.domain.account.AccountId;
import org.example.mockify.account.domain.account.AccountRepository;
import org.example.mockify.account.domain.creditcard.CardNumber;
import org.example.mockify.account.domain.creditcard.ExpirationDate;
import org.example.mockify.shared.domain.DomainException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterCreditCardService implements RegisterCreditCardUseCase {

    private final AccountRepository accountRepository;

    @Override
    public void registerCreditCard(RegisterCreditCardCommand command) {
        AccountId accountId = AccountId.from(command.accountId());
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new DomainException("account-not-found"));

        ExpirationDate expirationDate = new ExpirationDate(command.expiryMonth(), command.expiryYear());
        if (expirationDate.isExpired()) {
            throw new DomainException("card-already-expired");
        }

        CardNumber cardNumber = new CardNumber(command.cardNumber());
        account.registerCreditCard(cardNumber, expirationDate, command.holderName());
        accountRepository.save(account);
    }
}
