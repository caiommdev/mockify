package org.example.mockify.transaction.infrastructure.client;

import lombok.RequiredArgsConstructor;
import org.example.mockify.transaction.domain.rule.AccountSummary;
import org.example.mockify.transaction.domain.rule.AccountSummaryProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AccountServiceClient implements AccountSummaryProvider {

    private final RestTemplate restTemplate;

    @Value("${services.account.url}")
    private String accountServiceUrl;

    @Value("${services.subscription.url}")
    private String subscriptionServiceUrl;

    @Override
    public AccountSummary summaryFor(UUID accountId) {
        boolean hasActiveCreditCard = fetchHasActiveCreditCard(accountId);
        boolean hasActiveSubscription = fetchHasActiveSubscription(accountId);
        return new AccountSummary(accountId, hasActiveCreditCard, hasActiveSubscription);
    }

    private boolean fetchHasActiveCreditCard(UUID accountId) {
        try {
            AccountStatusResponse response = restTemplate.getForObject(
                    accountServiceUrl + "/api/v1/accounts/" + accountId + "/status",
                    AccountStatusResponse.class
            );
            return response != null && response.hasActiveCreditCard();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean fetchHasActiveSubscription(UUID accountId) {
        try {
            SubscriptionStatusResponse response = restTemplate.getForObject(
                    subscriptionServiceUrl + "/api/v1/accounts/" + accountId + "/subscriptions/status",
                    SubscriptionStatusResponse.class
            );
            return response != null && response.hasActiveSubscription();
        } catch (Exception e) {
            return false;
        }
    }

    record AccountStatusResponse(String accountId, boolean hasActiveCreditCard) {}
    record SubscriptionStatusResponse(String accountId, boolean hasActiveSubscription) {}
}
