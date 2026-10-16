package com.college.bank.exception;

/** Thrown when the account number is not found in the database. */
public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String accountNumber) {
        super("Account not found with number: " + accountNumber);
    }
}
