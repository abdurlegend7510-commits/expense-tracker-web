package com.expensetracker.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("VARIABLE")
public class VariableExpense extends Expense {

    private String category;

    protected VariableExpense() {
    }

    public VariableExpense(String description, double amount, LocalDate date, String category) throws InvalidExpenseException {
        super(description, amount, date);
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
}
