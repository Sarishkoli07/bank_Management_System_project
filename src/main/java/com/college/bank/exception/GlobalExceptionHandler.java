package com.college.bank.exception;

import com.college.bank.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * One central place that converts every exception into a clean JSON error message.
 * Without this, the user would see an ugly error page.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Validation failed (@NotBlank, @Email, @Pattern ...) */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .distinct()
                .collect(Collectors.toList());
        return build(HttpStatus.BAD_REQUEST, "Validation failed", errors);
    }

    /** Broken JSON or wrong value, e.g. accountType = "XYZ" or amount = "abc" */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(HttpMessageNotReadableException ex) {
        return build(HttpStatus.BAD_REQUEST,
                "Invalid request data. Check the account type (SAVINGS/CURRENT) and the number format.",
                Collections.emptyList());
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(AccountNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), Collections.emptyList());
    }

    @ExceptionHandler(DuplicateAccountException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateAccountException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), Collections.emptyList());
    }

    @ExceptionHandler({InsufficientBalanceException.class, InvalidRequestException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), Collections.emptyList());
    }

    /** Unknown URL */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResource(NoResourceFoundException ex) {
        return build(HttpStatus.NOT_FOUND, "Page or URL not found", Collections.emptyList());
    }

    /** Wrong HTTP method, e.g. GET used instead of POST */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethod(HttpRequestMethodNotSupportedException ex) {
        return build(HttpStatus.METHOD_NOT_ALLOWED, "This HTTP method is not allowed for this URL",
                Collections.emptyList());
    }

    /** Any other unexpected error */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOther(Exception ex) {
        ex.printStackTrace(); // shows the real error in the console for debugging
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong on the server",
                Collections.emptyList());
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message, List<String> errors) {
        return ResponseEntity.status(status).body(new ErrorResponse(status.value(), message, errors));
    }
}
