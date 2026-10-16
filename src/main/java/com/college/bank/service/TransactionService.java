package com.college.bank.service;

import com.college.bank.model.Account;
import com.college.bank.model.BankTransaction;
import com.college.bank.model.TransactionType;
import com.college.bank.repository.AccountRepository;
import com.college.bank.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/** Business logic for Deposit, Withdraw and Transaction History. */
@Service
public class TransactionService {

    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(AccountService accountService,
                              AccountRepository accountRepository,
                              TransactionRepository transactionRepository) {
        this.accountService = accountService;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * DEPOSIT.
     * @Transactional = if anything fails in the middle, ALL database changes are cancelled (rollback).
     */
    @Transactional
    public BankTransaction deposit(String accountNumber, BigDecimal amount) {
        Account account = accountService.getAccount(accountNumber);
        account.deposit(amount);
        accountRepository.save(account);
        return transactionRepository.save(new BankTransaction(accountNumber, TransactionType.DEPOSIT,
                amount, account.getBalance(), "Cash deposit"));
    }

    /** WITHDRAW. The minimum balance rule is checked inside account.withdraw(). */
    @Transactional
    public BankTransaction withdraw(String accountNumber, BigDecimal amount) {
        Account account = accountService.getAccount(accountNumber);
        account.withdraw(amount);
        accountRepository.save(account);
        return transactionRepository.save(new BankTransaction(accountNumber, TransactionType.WITHDRAW,
                amount, account.getBalance(), "Cash withdrawal"));
    }

    /** TRANSACTION HISTORY (newest first). */
    @Transactional(readOnly = true)
    public List<BankTransaction> getHistory(String accountNumber) {
        accountService.getAccount(accountNumber); // throws 404 if the account does not exist
        return transactionRepository.findByAccountNumberOrderByTransactionTimeDescIdDesc(accountNumber);
    }
}
