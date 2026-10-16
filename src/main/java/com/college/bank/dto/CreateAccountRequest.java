package com.college.bank.dto;

import com.college.bank.model.AccountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Data that comes from the "Create Account" form (JSON).
 * The annotations below are the VALIDATION rules. If a rule fails, Spring returns 400 Bad Request.
 */
public class CreateAccountRequest {

    @NotBlank(message = "Account holder name is required")
    @Size(min = 3, max = 50, message = "Name must be 3 to 50 characters long")
    @Pattern(regexp = "^[A-Za-z ]*$", message = "Name can contain only letters and spaces")
    private String holderName;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    @Size(max = 100, message = "Email is too long")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Phone must be 10 digits and start with 6, 7, 8 or 9")
    private String phone;

    @NotNull(message = "Account type is required (SAVINGS or CURRENT)")
    private AccountType accountType;

    @NotNull(message = "Initial deposit is required")
    @DecimalMin(value = "0.00", message = "Initial deposit cannot be negative")
    @Digits(integer = 10, fraction = 2, message = "Initial deposit can have up to 10 digits and 2 decimals")
    private BigDecimal initialDeposit;

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getInitialDeposit() {
        return initialDeposit;
    }

    public void setInitialDeposit(BigDecimal initialDeposit) {
        this.initialDeposit = initialDeposit;
    }
}
