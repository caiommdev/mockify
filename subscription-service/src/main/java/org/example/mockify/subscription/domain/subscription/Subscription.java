package org.example.mockify.subscription.domain.subscription;

import lombok.Getter;
import org.example.mockify.shared.domain.AggregateRoot;

import java.time.LocalDate;
import java.util.UUID;

@Getter
public class Subscription implements AggregateRoot {

    private final SubscriptionId id;
    private final UUID accountId;
    private final Plan plan;
    private SubscriptionStatus status;
    private final LocalDate startDate;

    public Subscription(UUID accountId, Plan plan) {
        this.id = SubscriptionId.generate();
        this.accountId = accountId;
        this.plan = plan;
        this.status = SubscriptionStatus.ACTIVE;
        this.startDate = LocalDate.now();
    }

    public Subscription(SubscriptionId id, UUID accountId, Plan plan, SubscriptionStatus status, LocalDate startDate) {
        this.id = id;
        this.accountId = accountId;
        this.plan = plan;
        this.status = status;
        this.startDate = startDate;
    }

    public void cancel() {
        this.status = SubscriptionStatus.CANCELLED;
    }

    public boolean isActive() {
        return this.status == SubscriptionStatus.ACTIVE;
    }
}
