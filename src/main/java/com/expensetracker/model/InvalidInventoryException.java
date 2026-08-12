package com.expensetracker.model;

/**
 * Custom checked exception thrown whenever an InventoryItem is given
 * invalid data. Mirrors InvalidExpenseException so both models follow
 * the same validation style.
 */
public class InvalidInventoryException extends Exception {
    public InvalidInventoryException(String message) {
        super(message);
    }
}
