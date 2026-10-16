package com.college.bank.exception;

/** Thrown for wrong input values, e.g. a negative amount or a too small opening deposit. */
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}
