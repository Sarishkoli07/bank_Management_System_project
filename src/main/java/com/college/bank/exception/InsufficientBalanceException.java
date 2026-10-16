package com.college.bank.exception;

/** Thrown when a withdrawal would take the balance below the allowed minimum. */
public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
