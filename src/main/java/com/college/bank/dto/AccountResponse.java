package com.college.bank.dto;

import com.college.bank.model.Account;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data sent back to the browser. We do not send the Account entity directly,
 * so the database structure stays hidden and only the needed fields are shown.
 */
public class AccountResponse {

    private final String accountNumber;
    private final String holderName;
    private final String email;
    private final String phone;
    private final String accountType;
    private final BigDecimal balance;
    private final BigDecimal minimumBalance;
    private final BigDecimal monthlyInterest;
    private final LocalDateTime createdAt;

    public AccountResponse(Account account) {
        this.accountNumber = account.getAccountNumber();
        this.holderName = account.getHolderName();
        this.email = account.getEmail();
        this.phone = account.getPhone();
        // POLYMORPHISM: the correct child class method is called automatically
        this.accountType = account.getAccountTypeName();
        this.balance = account.getBalance();
        this.minimumBalance = account.getMinimumBalance();
        this.monthlyInterest = account.calculateMonthlyInterest();
        this.createdAt = account.getCreatedAt();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAccountType() {
        return accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }

    public BigDecimal getMonthlyInterest() {
        return monthlyInterest;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
