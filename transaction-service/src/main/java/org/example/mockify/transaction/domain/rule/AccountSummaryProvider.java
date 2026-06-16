package org.example.mockify.transaction.domain.rule;

import java.util.UUID;

public interface AccountSummaryProvider {

    AccountSummary summaryFor(UUID accountId);
}
