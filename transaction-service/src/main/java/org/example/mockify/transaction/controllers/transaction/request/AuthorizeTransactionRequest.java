package org.example.mockify.transaction.controllers.transaction.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record AuthorizeTransactionRequest(
        @NotNull UUID accountId,
        @NotNull @DecimalMin("0.01") BigDecimal amount,
        @NotBlank String currency,
        @NotBlank String merchantId,
        @NotBlank String merchantName
) {}
