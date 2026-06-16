package org.example.mockify.account.controllers.account;

import tools.jackson.databind.ObjectMapper;
import org.example.mockify.account.application.account.CreateAccountCommand;
import org.example.mockify.account.application.account.CreateAccountUseCase;
import org.example.mockify.account.application.creditcard.RegisterCreditCardUseCase;
import org.example.mockify.account.domain.account.Account;
import org.example.mockify.account.domain.account.AccountId;
import org.example.mockify.account.domain.account.AccountRepository;
import org.example.mockify.account.domain.account.Email;
import org.example.mockify.account.domain.account.FullName;
import org.example.mockify.shared.domain.DomainException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateAccountUseCase createAccountUseCase;

    @MockitoBean
    private RegisterCreditCardUseCase registerCreditCardUseCase;

    @MockitoBean
    private AccountRepository accountRepository;

    private final UUID accountId = UUID.randomUUID();

    @Test
    void should_create_account_and_return_201() throws Exception {
        AccountId id = AccountId.from(accountId);
        given(createAccountUseCase.createAccount(any(CreateAccountCommand.class))).willReturn(id);
        given(accountRepository.findById(id)).willReturn(Optional.of(
                new Account(id, new Email("user@example.com"), new FullName("John Doe"))
        ));

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email": "user@example.com", "fullName": "John Doe"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.fullName").value("John Doe"));
    }

    @Test
    void should_return_422_when_email_already_registered() throws Exception {
        given(createAccountUseCase.createAccount(any(CreateAccountCommand.class)))
                .willThrow(new DomainException("email-already-registered"));

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email": "user@example.com", "fullName": "John Doe"}
                                """))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.violations[0]").value("email-already-registered"));
    }

    @Test
    void should_return_400_when_request_is_invalid() throws Exception {
        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email": "not-an-email", "fullName": ""}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_get_account_by_id() throws Exception {
        AccountId id = AccountId.from(accountId);
        given(accountRepository.findById(id)).willReturn(Optional.of(
                new Account(id, new Email("user@example.com"), new FullName("John Doe"))
        ));

        mockMvc.perform(get("/api/v1/accounts/" + accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(accountId.toString()));
    }
}
