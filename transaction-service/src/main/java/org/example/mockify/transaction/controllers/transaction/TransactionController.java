package org.example.mockify.transaction.controllers.transaction;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.mockify.shared.domain.DomainException;
import org.example.mockify.transaction.application.transaction.AuthorizeTransactionCommand;
import org.example.mockify.transaction.application.transaction.AuthorizeTransactionUseCase;
import org.example.mockify.transaction.application.transaction.TransactionAuthorizationResult;
import org.example.mockify.transaction.controllers.transaction.request.AuthorizeTransactionRequest;
import org.example.mockify.transaction.controllers.transaction.response.TransactionResponse;
import org.example.mockify.transaction.domain.rule.AuthorizationResult;
import org.example.mockify.transaction.domain.transaction.Transaction;
import org.example.mockify.transaction.domain.transaction.TransactionId;
import org.example.mockify.transaction.domain.transaction.TransactionRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final AuthorizeTransactionUseCase authorizeTransactionUseCase;
    private final TransactionRepository transactionRepository;

    @PostMapping
    public TransactionResponse authorize(@Valid @RequestBody AuthorizeTransactionRequest request) {
        AuthorizeTransactionCommand command = new AuthorizeTransactionCommand(
                request.accountId(), request.amount(), request.currency(),
                request.merchantId(), request.merchantName()
        );
        TransactionAuthorizationResult result = authorizeTransactionUseCase.authorize(command);
        return TransactionResponse.from(result.transaction(), result.authorizationResult());
    }

    @GetMapping("/{transactionId}")
    public TransactionResponse getTransaction(@PathVariable UUID transactionId) {
        Transaction tx = transactionRepository.findById(TransactionId.from(transactionId))
                .orElseThrow(() -> new DomainException("transaction-not-found"));
        AuthorizationResult result = tx.getStatus().name().equals("APPROVED")
                ? AuthorizationResult.approved()
                : AuthorizationResult.denied(tx.getViolations());
        return TransactionResponse.from(tx, result);
    }

    @GetMapping
    public List<TransactionResponse> listByAccount(@RequestParam UUID accountId) {
        return transactionRepository.findByAccountId(accountId).stream()
                .map(tx -> {
                    AuthorizationResult r = tx.getStatus().name().equals("APPROVED")
                            ? AuthorizationResult.approved()
                            : AuthorizationResult.denied(tx.getViolations());
                    return TransactionResponse.from(tx, r);
                })
                .toList();
    }
}
