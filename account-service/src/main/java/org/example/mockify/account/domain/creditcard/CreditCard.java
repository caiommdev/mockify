package org.example.mockify.account.domain.creditcard;

import lombok.Getter;
import org.example.mockify.shared.domain.DomainEntity;

@Getter
public class CreditCard implements DomainEntity {

    private final CardNumber number;
    private final ExpirationDate expirationDate;
    private final String holderName;
    private CardStatus status;

    public CreditCard(CardNumber number, ExpirationDate expirationDate, String holderName) {
        this.number = number;
        this.expirationDate = expirationDate;
        this.holderName = holderName;
        this.status = CardStatus.ACTIVE;
    }

    public CreditCard(CardNumber number, ExpirationDate expirationDate, String holderName, CardStatus status) {
        this.number = number;
        this.expirationDate = expirationDate;
        this.holderName = holderName;
        this.status = status;
    }

    public boolean isActive() {
        return status == CardStatus.ACTIVE && !expirationDate.isExpired();
    }

    public void deactivate() {
        this.status = CardStatus.INACTIVE;
    }
}
