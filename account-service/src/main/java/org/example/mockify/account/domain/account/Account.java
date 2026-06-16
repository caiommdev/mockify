package org.example.mockify.account.domain.account;

import lombok.Getter;
import org.example.mockify.account.domain.creditcard.CardNumber;
import org.example.mockify.account.domain.creditcard.CardStatus;
import org.example.mockify.account.domain.creditcard.CreditCard;
import org.example.mockify.account.domain.creditcard.ExpirationDate;
import org.example.mockify.shared.domain.AggregateRoot;

@Getter
public class Account implements AggregateRoot {

    private final AccountId id;
    private final Email email;
    private final FullName name;
    private CreditCard creditCard;

    public Account(AccountId id, Email email, FullName name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public Account(AccountId id, Email email, FullName name, CreditCard creditCard) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.creditCard = creditCard;
    }

    public void registerCreditCard(CardNumber number, ExpirationDate expirationDate, String holderName) {
        this.creditCard = new CreditCard(number, expirationDate, holderName);
    }

    public boolean hasActiveCreditCard() {
        return creditCard != null && creditCard.isActive();
    }
}
