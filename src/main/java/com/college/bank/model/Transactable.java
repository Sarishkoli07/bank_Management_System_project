package com.college.bank.model;

import java.math.BigDecimal;

/**
 * ABSTRACTION (interface): it only says WHAT must be done, not HOW.
 * Any class that implements this interface must write deposit() and withdraw().
 */
public interface Transactable {

    void deposit(BigDecimal amount);

    void withdraw(BigDecimal amount);
}
