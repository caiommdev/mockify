package org.example.mockify.transaction.application.transaction;

import java.math.BigDecimal;
import java.util.UUID;

public record AuthorizeTransactionCommand(
        UUID accountId,
        BigDecimal amount,
        String currency,
        String merchantId,
        String merchantName
) {}
