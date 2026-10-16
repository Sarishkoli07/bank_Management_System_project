package com.college.bank.service;

import com.college.bank.dto.CreateAccountRequest;
import com.college.bank.exception.AccountNotFoundException;
import com.college.bank.exception.DuplicateAccountException;
import com.college.bank.exception.InvalidRequestException;
import com.college.bank.model.Account;
import com.college.bank.model.AccountType;
import com.college.bank.model.BankTransaction;
import com.college.bank.model.CurrentAccount;
import com.college.bank.model.SavingsAccount;
import com.college.bank.model.TransactionType;
import com.college.bank.repository.AccountRepository;
import com.college.bank.repository.TransactionRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Service = business logic (the rules of the bank).
 * Controller calls Service, Service calls Repository.
 */
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    // Constructor injection: Spring gives us the repository objects automatically
    public AccountService(AccountRepository accountRepository,
                          TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    /** CREATE ACCOUNT */
    @Transactional
    public Account createAccount(CreateAccountRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        if (accountRepository.existsByEmail(email)) {
            throw new DuplicateAccountException("An account already exists with this email: " + email);
        }

        String accountNumber = generateAccountNumber();
        String name = request.getHolderName().trim();
        BigDecimal opening = request.getInitialDeposit();

        // POLYMORPHISM: the variable type is Account (parent), the object is Savings or Current (child)
        Account account;
        if (request.getAccountType() == AccountType.SAVINGS) {
            account = new SavingsAccount(accountNumber, name, email, request.getPhone(), opening);
        } else {
            account = new CurrentAccount(accountNumber, name, email, request.getPhone(), opening);
        }

        // Savings account needs a minimum opening deposit (500)
        if (account.getMinimumBalance().signum() > 0
                && opening.compareTo(account.getMinimumBalance()) < 0) {
            throw new InvalidRequestException("Opening deposit for " + account.getAccountTypeName()
                    + " account must be at least Rs. " + account.getMinimumBalance());
        }

        Account saved = accountRepository.save(account);

        if (opening.signum() > 0) {
            transactionRepository.save(new BankTransaction(accountNumber, TransactionType.DEPOSIT,
                    opening, saved.getBalance(), "Opening deposit"));
        }
        return saved;
    }

    /** CHECK BALANCE / account details */
    @Transactional(readOnly = true)
    public Account getAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }

    /** All accounts, newest first */
    @Transactional(readOnly = true)
    public List<Account> getAllAccounts() {
        return accountRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    /** SEARCH ACCOUNT */
    @Transactional(readOnly = true)
    public List<Account> searchAccounts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new InvalidRequestException("Enter a name, account number or phone to search");
        }
        return accountRepository.search(keyword.trim());
    }

    /** Creates a random 10 digit number that is not used by any other account. */
    private String generateAccountNumber() {
        String number;
        do {
            number = String.valueOf(ThreadLocalRandom.current().nextLong(1_000_000_000L, 10_000_000_000L));
        } while (accountRepository.existsByAccountNumber(number));
        return number;
    }
}
