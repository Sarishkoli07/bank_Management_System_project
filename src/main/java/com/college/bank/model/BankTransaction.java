package com.college.bank.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * One record for every deposit / withdraw. The Transaction History screen shows these.
 * (Named "BankTransaction" to avoid confusion with other "Transaction" classes in Spring.)
 */
@Entity
@Table(name = "bank_transactions")
public class BankTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", nullable = false, length = 20)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 20)
    private TransactionType transactionType;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "balance_after", nullable = false, precision = 15, scale = 2)
    private BigDecimal balanceAfter;

    @Column(length = 200)
    private String description;

    @Column(name = "transaction_time", nullable = false)
    private LocalDateTime transactionTime;

    protected BankTransaction() {
    }

    public BankTransaction(String accountNumber, TransactionType transactionType,
                           BigDecimal amount, BigDecimal balanceAfter, String description) {
        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.transactionTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public TransactionType getTransactionType() {
        return transactionType;
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
