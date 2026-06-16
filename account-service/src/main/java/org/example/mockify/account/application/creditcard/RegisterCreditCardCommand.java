package org.example.mockify.account.application.creditcard;

public record RegisterCreditCardCommand(
        String accountId,
        String cardNumber,
        int expiryMonth,
        int expiryYear,
        String holderName
) {}
