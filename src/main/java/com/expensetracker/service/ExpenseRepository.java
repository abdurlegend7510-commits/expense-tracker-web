package com.expensetracker.service;

import com.expensetracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA generates the implementation of this interface at
 * startup - findAll(), findById(), save(), deleteById() all come for
 * free. This single interface replaces everything saveToFile() and
 * loadFromFile() used to do by hand.
 */
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
