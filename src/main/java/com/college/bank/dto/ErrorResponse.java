package com.college.bank.dto;

import java.time.LocalDateTime;
import java.util.List;

/** The JSON that is sent when something goes wrong. */
public class ErrorResponse {

    private final int status;
    private final String message;
    private final List<String> errors;
    private final LocalDateTime timestamp;

    public ErrorResponse(int status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
