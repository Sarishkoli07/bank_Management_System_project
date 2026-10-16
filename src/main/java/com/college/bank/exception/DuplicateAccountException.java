package com.college.bank.exception;

/** Thrown when an account already exists with the same email. */
public class DuplicateAccountException extends RuntimeException {
    public DuplicateAccountException(String message) {
        super(message);
    }
}
