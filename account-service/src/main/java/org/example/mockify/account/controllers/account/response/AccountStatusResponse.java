package org.example.mockify.account.controllers.account.response;

public record AccountStatusResponse(
        String accountId,
        boolean hasActiveCreditCard
) {}
