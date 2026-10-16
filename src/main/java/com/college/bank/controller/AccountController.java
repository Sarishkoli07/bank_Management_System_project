package com.college.bank.controller;

import com.college.bank.dto.AccountResponse;
import com.college.bank.dto.AmountRequest;
import com.college.bank.dto.CreateAccountRequest;
import com.college.bank.dto.TransactionResponse;
import com.college.bank.model.Account;
import com.college.bank.service.AccountService;
import com.college.bank.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller = receives requests from the browser (JavaScript) and returns JSON.
 * Every URL here starts with /api/accounts.
 * @Valid = run the validation rules written in the DTO class.
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    public AccountController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    /** POST /api/accounts  -> Create Account */
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        Account account = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AccountResponse(account));
    }

    /** GET /api/accounts  -> list of all accounts */
    @GetMapping
    public List<AccountResponse> getAllAccounts() {
        return accountService.getAllAccounts().stream()
                .map(AccountResponse::new)
                .collect(Collectors.toList());
    }

    /** GET /api/accounts/search?keyword=amit  -> Search Account */
    @GetMapping("/search")
    public List<AccountResponse> search(@RequestParam(defaultValue = "") String keyword) {
        return accountService.searchAccounts(keyword).stream()
                .map(AccountResponse::new)
                .collect(Collectors.toList());
    }

    /** GET /api/accounts/1234567890  -> Check Balance (account details) */
    @GetMapping("/{accountNumber}")
    public AccountResponse getAccount(@PathVariable String accountNumber) {
        return new AccountResponse(accountService.getAccount(accountNumber));
    }

    /** POST /api/accounts/1234567890/deposit  -> Deposit */
    @PostMapping("/{accountNumber}/deposit")
    public TransactionResponse deposit(@PathVariable String accountNumber,
                                       @Valid @RequestBody AmountRequest request) {
        return new TransactionResponse(transactionService.deposit(accountNumber, request.getAmount()));
    }

    /** POST /api/accounts/1234567890/withdraw  -> Withdraw */
    @PostMapping("/{accountNumber}/withdraw")
    public TransactionResponse withdraw(@PathVariable String accountNumber,
                                        @Valid @RequestBody AmountRequest request) {
        return new TransactionResponse(transactionService.withdraw(accountNumber, request.getAmount()));
    }

    /** GET /api/accounts/1234567890/transactions  -> Transaction History */
    @GetMapping("/{accountNumber}/transactions")
    public List<TransactionResponse> getHistory(@PathVariable String accountNumber) {
        return transactionService.getHistory(accountNumber).stream()
                .map(TransactionResponse::new)
                .collect(Collectors.toList());
    }
}
