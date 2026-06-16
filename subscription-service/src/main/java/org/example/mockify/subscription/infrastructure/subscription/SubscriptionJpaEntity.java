package org.example.mockify.subscription.infrastructure.subscription;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.mockify.subscription.domain.subscription.Plan;
import org.example.mockify.subscription.domain.subscription.Subscription;
import org.example.mockify.subscription.domain.subscription.SubscriptionId;
import org.example.mockify.subscription.domain.subscription.SubscriptionStatus;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "subscriptions")
@Getter
@NoArgsConstructor
public class SubscriptionJpaEntity {

    @Id
    private UUID id;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Plan plan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    public static SubscriptionJpaEntity fromDomain(Subscription sub) {
        SubscriptionJpaEntity entity = new SubscriptionJpaEntity();
        entity.id = sub.getId().value();
        entity.accountId = sub.getAccountId();
        entity.plan = sub.getPlan();
        entity.status = sub.getStatus();
        entity.startDate = sub.getStartDate();
        return entity;
    }

    public Subscription toDomain() {
        return new Subscription(
                SubscriptionId.from(id),
                accountId,
                plan,
                status,
                startDate
        );
    }
}
