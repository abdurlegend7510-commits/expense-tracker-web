package com.expensetracker.model;

import java.time.LocalDate;

/**
 * Abstract parent class for every expense in the system.
 * Identical logic to the Swing version - the model layer has no
 * dependency on any GUI toolkit, which is exactly what lets it be
 * reused unchanged in a web backend.
 */
public abstract class Expense {
    private int id;
    private String description;
    private double amount;
    private LocalDate date;

    public Expense(int id, String description, double amount, LocalDate date) throws InvalidExpenseException {
        if (description == null || description.trim().isEmpty()) {
            throw new InvalidExpenseException("Description cannot be empty.");
        }
        if (amount <= 0) {
            throw new InvalidExpenseException("Amount must be positive.");
        }
        if (date == null) {
            throw new InvalidExpenseException("Date cannot be empty.");
        }
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public int getId() {
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
    public abstract String toFileLine();
}
