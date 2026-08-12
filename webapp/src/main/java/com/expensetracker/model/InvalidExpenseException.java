package com.expensetracker.model;

/**
 * Custom checked exception thrown whenever an Expense (or its subclasses)
 * is given invalid data - by a constructor or a setter.
 */
public class InvalidExpenseException extends Exception {
    public InvalidExpenseException(String message) {
        super(message);
    }
}
