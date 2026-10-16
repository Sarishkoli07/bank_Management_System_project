package com.college.bank.repository;

import com.college.bank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository = the class that talks to the database.
 * We only write the method names; Spring Data JPA writes the SQL for us.
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);

    boolean existsByAccountNumber(String accountNumber);

    boolean existsByEmail(String email);

    /** Search by name (any part), account number (any part) or phone (any part). */
    @Query("SELECT a FROM Account a WHERE LOWER(a.holderName) LIKE LOWER(CONCAT('%', :keyword, '%')) "
            + "OR a.accountNumber LIKE CONCAT('%', :keyword, '%') "
            + "OR a.phone LIKE CONCAT('%', :keyword, '%') ORDER BY a.id DESC")
    List<Account> search(@Param("keyword") String keyword);
}
