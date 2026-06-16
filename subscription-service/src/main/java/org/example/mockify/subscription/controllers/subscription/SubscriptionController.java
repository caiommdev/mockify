package org.example.mockify.subscription.controllers.subscription;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.mockify.shared.domain.DomainException;
import org.example.mockify.subscription.application.subscription.CancelSubscriptionUseCase;
import org.example.mockify.subscription.application.subscription.SubscribeToPlanCommand;
import org.example.mockify.subscription.application.subscription.SubscribeToPlanUseCase;
import org.example.mockify.subscription.controllers.subscription.request.SubscribeToPlanRequest;
import org.example.mockify.subscription.controllers.subscription.response.SubscriptionResponse;
import org.example.mockify.subscription.controllers.subscription.response.SubscriptionStatusResponse;
import org.example.mockify.subscription.domain.subscription.SubscriptionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts/{accountId}/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscribeToPlanUseCase subscribeToPlanUseCase;
    private final CancelSubscriptionUseCase cancelSubscriptionUseCase;
    private final SubscriptionRepository subscriptionRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionResponse subscribe(
            @PathVariable String accountId,
            @Valid @RequestBody SubscribeToPlanRequest request
    ) {
        subscribeToPlanUseCase.subscribeToPlan(new SubscribeToPlanCommand(accountId, request.plan()));
        return subscriptionRepository.findActiveByAccountId(UUID.fromString(accountId))
                .map(SubscriptionResponse::from)
                .orElseThrow(() -> new DomainException("subscription-not-found"));
    }

    @GetMapping("/active")
    public SubscriptionResponse getActiveSubscription(@PathVariable String accountId) {
        return subscriptionRepository.findActiveByAccountId(UUID.fromString(accountId))
                .map(SubscriptionResponse::from)
                .orElseThrow(() -> new DomainException("no-active-subscription"));
    }

    @DeleteMapping("/active")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelSubscription(@PathVariable String accountId) {
        cancelSubscriptionUseCase.cancelSubscription(accountId);
    }

    @GetMapping("/status")
    public SubscriptionStatusResponse getSubscriptionStatus(@PathVariable String accountId) {
        boolean hasActive = subscriptionRepository.findActiveByAccountId(UUID.fromString(accountId)).isPresent();
        return new SubscriptionStatusResponse(accountId, hasActive);
    }
}
