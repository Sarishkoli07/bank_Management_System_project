package com.college.bank.model;

import com.college.bank.exception.InsufficientBalanceException;
import com.college.bank.exception.InvalidRequestException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Simple unit tests for the OOP classes. No database or server is needed. */
class AccountTest {

    private Account savings(String balance) {
        return new SavingsAccount("1111111111", "Amit Patil", "amit@mail.com", "9876543210", new BigDecimal(balance));
    }

    private Account current(String balance) {
        return new CurrentAccount("2222222222", "Sneha Kulkarni", "sneha@mail.com", "9123456780", new BigDecimal(balance));
    }

    @Test
    void depositIncreasesBalance() {
        Account a = savings("1000");
        a.deposit(new BigDecimal("500"));
        assertEquals(new BigDecimal("1500.00"), a.getBalance());
    }

    @Test
    void withdrawDecreasesBalance() {
        Account a = savings("1000");
        a.withdraw(new BigDecimal("200"));
        assertEquals(new BigDecimal("800.00"), a.getBalance());
    }

    @Test
    void savingsCannotGoBelowMinimumBalance() {
        Account a = savings("1000");
        assertThrows(InsufficientBalanceException.class, () -> a.withdraw(new BigDecimal("600")));
        assertEquals(new BigDecimal("1000.00"), a.getBalance()); // balance unchanged
    }

    @Test
    void currentAllowsOverdraftUpToLimit() {
        Account a = current("1000");
        a.withdraw(new BigDecimal("6000"));      // balance becomes -5000
        assertEquals(new BigDecimal("-5000.00"), a.getBalance());
        assertThrows(InsufficientBalanceException.class, () -> a.withdraw(new BigDecimal("1")));
    }

    @Test
    void zeroOrNegativeAmountIsRejected() {
        Account a = savings("1000");
        assertThrows(InvalidRequestException.class, () -> a.deposit(BigDecimal.ZERO));
        assertThrows(InvalidRequestException.class, () -> a.withdraw(new BigDecimal("-10")));
    }

    @Test
    void polymorphismGivesDifferentInterest() {
        List<Account> accounts = List.of(savings("12000"), current("12000"));
        assertEquals(new BigDecimal("40.00"), accounts.get(0).calculateMonthlyInterest());
        assertEquals(new BigDecimal("0.00"), accounts.get(1).calculateMonthlyInterest());
    }
}
