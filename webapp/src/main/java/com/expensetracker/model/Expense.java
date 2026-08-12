package com.expensetracker.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Abstract parent entity for every expense. @Inheritance(SINGLE_TABLE)
 * tells JPA to store FixedExpense and VariableExpense rows in one table
 * (expense), with a discriminator column telling them apart - this is
 * the database equivalent of the Java inheritance you already had.
 */
@Entity
@Table(name = "expense")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "expense_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private double amount;
    private LocalDate date;

    // JPA needs a no-arg constructor to rebuild objects from the database.
    // It's protected, not public, so application code still has to go
    // through the validating constructor below.
    protected Expense() {
    }

    public Expense(String description, double amount, LocalDate date) throws InvalidExpenseException {
        if (description == null || description.trim().isEmpty()) {
            throw new InvalidExpenseException("Description cannot be empty.");
        }
        if (amount <= 0) {
            throw new InvalidExpenseException("Amount must be positive.");
        }
        if (date == null) {
            throw new InvalidExpenseException("Date cannot be empty.");
        }
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) throws InvalidExpenseException {
        if (description == null || description.trim().isEmpty()) {
            throw new InvalidExpenseException("Description cannot be empty.");
        }
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) throws InvalidExpenseException {
        if (amount <= 0) {
            throw new InvalidExpenseException("Amount must be positive.");
        }
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public abstract double calculateMonthlyImpact();
    public abstract String getCategoryLabel();
    public abstract String getType();
}
