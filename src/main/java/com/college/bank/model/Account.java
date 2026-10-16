package com.college.bank.model;

import com.college.bank.exception.InsufficientBalanceException;
import com.college.bank.exception.InvalidRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * Account - the MAIN class of the project.
 *
 * OOP concepts used here:
 *  - CLASS / OBJECT : Account is a class; SavingsAccount and CurrentAccount objects are created from its children.
 *  - ABSTRACTION    : the class is "abstract", so "new Account()" is not allowed.
 *                     Abstract methods (getMinimumBalance ...) must be written by child classes.
 *  - ENCAPSULATION  : all fields are "private". Balance cannot be changed directly;
 *                     it changes only through deposit() / withdraw(). There is no setBalance().
 *  - INHERITANCE    : SavingsAccount and CurrentAccount "extend" this class.
 *  - POLYMORPHISM   : withdraw() calls getMinimumBalance(). The same code gives 500 for Savings
 *                     and -5000 for Current, because each child overrides the method.
 *
 * @Entity means this class is mapped to a database table ("accounts").
 * SINGLE_TABLE means Savings and Current accounts are both stored in the same table.
 */
@Entity
@Table(name = "accounts")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "account_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Account implements Transactable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", nullable = false, unique = true, length = 20)
    private String accountNumber;

    @Column(name = "holder_name", nullable = false, length = 100)
    private String holderName;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 15)
    private String phone;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /** Empty constructor required by JPA (Hibernate). */
    protected Account() {
    }

    /** Constructor: sets the values when a new object is created. */
    protected Account(String accountNumber, String holderName, String email,
                      String phone, BigDecimal openingBalance) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.email = email;
        this.phone = phone;
        this.balance = openingBalance.setScale(2, RoundingMode.HALF_UP);
        this.createdAt = LocalDateTime.now();
    }

    // ---------- Abstract methods: every child class must write these ----------

    /** Lowest balance allowed (negative for Current account = overdraft limit). */
    public abstract BigDecimal getMinimumBalance();

    /** "SAVINGS" or "CURRENT". */
    public abstract String getAccountTypeName();

    /** Interest for one month. */
    public abstract BigDecimal calculateMonthlyInterest();

    // ---------- Common behaviour (same for all accounts) ----------

    @Override
    public void deposit(BigDecimal amount) {
        validateAmount(amount);
        this.balance = this.balance.add(amount);
    }

    @Override
    public void withdraw(BigDecimal amount) {
        validateAmount(amount);
        BigDecimal newBalance = this.balance.subtract(amount);
        // POLYMORPHISM: getMinimumBalance() gives a different value for each child class
        if (newBalance.compareTo(getMinimumBalance()) < 0) {
            BigDecimal maxWithdraw = this.balance.subtract(getMinimumBalance());
            throw new InsufficientBalanceException(
                    "Insufficient balance. Minimum balance for " + getAccountTypeName()
                            + " account is Rs. " + getMinimumBalance()
                            + ". Maximum you can withdraw now: Rs. " + maxWithdraw);
        }
        this.balance = newBalance;
    }

    /** private helper: checks that the amount is valid. */
    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidRequestException("Amount must be greater than zero");
        }
    }

    // ---------- Getters (to read the values) ----------

    public Long getId() {
        return id;
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

    public BigDecimal getBalance() {
        return balance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // ---------- Setters (only for fields that are allowed to change) ----------

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // No setBalance() on purpose = ENCAPSULATION

    @Override
    public String toString() {
        return getAccountTypeName() + "[" + accountNumber + ", " + holderName + ", balance=" + balance + "]";
    }
}
