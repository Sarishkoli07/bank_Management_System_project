package com.college.bank.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

/**
 * INHERITANCE: CurrentAccount is also a child of Account.
 * It allows an overdraft up to Rs. 5000 (balance can go to -5000) and gives no interest.
 */
@Entity
@DiscriminatorValue("CURRENT")
public class CurrentAccount extends Account {

    private static final BigDecimal OVERDRAFT_LIMIT = new BigDecimal("-5000.00");

    protected CurrentAccount() {
    }

    public CurrentAccount(String accountNumber, String holderName, String email,
                          String phone, BigDecimal openingBalance) {
        super(accountNumber, holderName, email, phone, openingBalance);
    }

    @Override
    public BigDecimal getMinimumBalance() {
        return OVERDRAFT_LIMIT;
    }

    @Override
    public String getAccountTypeName() {
        return "CURRENT";
    }

    @Override
    public BigDecimal calculateMonthlyInterest() {
        return BigDecimal.ZERO.setScale(2);
    }
}
