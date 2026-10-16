package com.college.bank.dto;

import com.college.bank.model.BankTransaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** One transaction row sent to the browser. */
public class TransactionResponse {

    private final Long id;
    private final String accountNumber;
    private final String type;
    private final BigDecimal amount;
    private final BigDecimal balanceAfter;
    private final String description;
    private final LocalDateTime transactionTime;

    public TransactionResponse(BankTransaction t) {
        this.id = t.getId();
        this.accountNumber = t.getAccountNumber();
        this.type = t.getTransactionType().name();
        this.amount = t.getAmount();
        this.balanceAfter = t.getBalanceAfter();
        this.description = t.getDescription();
        this.transactionTime = t.getTransactionTime();
    }

    public Long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }
}
