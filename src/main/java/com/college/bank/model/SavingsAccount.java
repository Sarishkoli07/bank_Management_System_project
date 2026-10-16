package com.college.bank.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * INHERITANCE: SavingsAccount is a child of Account ("extends").
 * It needs a minimum balance of Rs. 500 and gives 4% yearly interest.
 */
@Entity
@DiscriminatorValue("SAVINGS")
public class SavingsAccount extends Account {

    private static final BigDecimal MINIMUM_BALANCE = new BigDecimal("500.00");
    private static final BigDecimal ANNUAL_INTEREST_RATE = new BigDecimal("0.04");

    protected SavingsAccount() {
    }

    public SavingsAccount(String accountNumber, String holderName, String email,
                          String phone, BigDecimal openingBalance) {
        super(accountNumber, holderName, email, phone, openingBalance);
    }

    @Override
    public BigDecimal getMinimumBalance() {
        return MINIMUM_BALANCE;
    }

    @Override
    public String getAccountTypeName() {
        return "SAVINGS";
    }

    @Override
    public BigDecimal calculateMonthlyInterest() {
        return getBalance().multiply(ANNUAL_INTEREST_RATE)
                .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
    }
}
