package com.expensetracker.model;

import java.time.LocalDate;

/**
 * A one-off or irregular expense (e.g. groceries, entertainment) tagged
 * with a spending category.
 */
public class VariableExpense extends Expense {
    private String category;

    public VariableExpense(int id, String description, double amount, LocalDate date, String category) throws InvalidExpenseException {
        super(id, description, amount, date);
        if (category == null || category.trim().isEmpty()) {
            throw new InvalidExpenseException("Category cannot be empty.");
        }
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) throws InvalidExpenseException {
        if (category == null || category.trim().isEmpty()) {
            throw new InvalidExpenseException("Category cannot be empty.");
        }
        this.category = category;
    }

    @Override
    public double calculateMonthlyImpact() {
        return getAmount();
    }

    @Override
    public String getCategoryLabel() {
        return category;
    }

    @Override
    public String getType() {
        return "VARIABLE";
    }

    @Override
    public String toFileLine() {
        return "VARIABLE|" + getId() + "|" + getDescription() + "|" + getAmount() + "|" + getDate() + "|" + category;
    }
}
