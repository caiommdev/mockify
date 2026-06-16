package org.example.mockify.account.controllers.account.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record RegisterCreditCardRequest(
        @NotBlank String cardNumber,
        @Min(1) @Max(12) int expiryMonth,
        @Min(2000) int expiryYear,
        @NotBlank String holderName
) {}
