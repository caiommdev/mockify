package org.example.mockify.account.infrastructure.account;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.mockify.account.domain.account.Account;
import org.example.mockify.account.domain.account.AccountId;
import org.example.mockify.account.domain.account.Email;
import org.example.mockify.account.domain.account.FullName;
import org.example.mockify.account.domain.creditcard.CardNumber;
import org.example.mockify.account.domain.creditcard.CardStatus;
import org.example.mockify.account.domain.creditcard.CreditCard;
import org.example.mockify.account.domain.creditcard.ExpirationDate;

import java.util.UUID;

@Entity
@Table(name = "accounts")
@Getter
@NoArgsConstructor
public class AccountJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "card_holder_name")
    private String cardHolderName;

    @Column(name = "card_expiry_month")
    private Integer cardExpiryMonth;

    @Column(name = "card_expiry_year")
    private Integer cardExpiryYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_status")
    private CardStatus cardStatus;

    public static AccountJpaEntity fromDomain(Account account) {
        AccountJpaEntity entity = new AccountJpaEntity();
        entity.id = account.getId().value();
        entity.email = account.getEmail().value();
        entity.fullName = account.getName().value();

        if (account.getCreditCard() != null) {
            CreditCard card = account.getCreditCard();
            entity.cardNumber = card.getNumber().value();
            entity.cardHolderName = card.getHolderName();
            entity.cardExpiryMonth = card.getExpirationDate().month();
            entity.cardExpiryYear = card.getExpirationDate().year();
            entity.cardStatus = card.getStatus();
        }
        return entity;
    }

    public Account toDomain() {
        CreditCard creditCard = null;
        if (cardNumber != null) {
            creditCard = new CreditCard(
                    new CardNumber(cardNumber),
                    new ExpirationDate(cardExpiryMonth, cardExpiryYear),
                    cardHolderName,
                    cardStatus
            );
        }
        return new Account(
                AccountId.from(id),
                new Email(email),
                new FullName(fullName),
                creditCard
        );
    }
}
