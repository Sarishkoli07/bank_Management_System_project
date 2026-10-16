package com.college.bank.repository;

import com.college.bank.model.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<BankTransaction, Long> {

    /** Newest transaction first. */
    List<BankTransaction> findByAccountNumberOrderByTransactionTimeDescIdDesc(String accountNumber);
}
