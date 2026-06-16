package org.example.mockify.account.controllers.account.response;

import org.example.mockify.account.domain.account.Account;
import org.example.mockify.account.domain.creditcard.CreditCard;

public record AccountResponse(
        String id,
        String email,
        String fullName,
        CreditCardResponse creditCard
) {
    public static AccountResponse from(Account account) {
        CreditCardResponse cardResponse = null;
        if (account.getCreditCard() != null) {
            CreditCard card = account.getCreditCard();
            cardResponse = new CreditCardResponse(
                    card.getNumber().toString(),
                    card.getHolderName(),
                    card.getExpirationDate().month(),
                    card.getExpirationDate().year(),
                    card.getStatus().name(),
                    card.isActive()
            );
        }
        return new AccountResponse(
                account.getId().value().toString(),
                account.getEmail().value(),
                account.getName().value(),
                cardResponse
        );
    }

    public record CreditCardResponse(
            String maskedNumber,
            String holderName,
            int expiryMonth,
            int expiryYear,
            String status,
            boolean active
    ) {}
}
