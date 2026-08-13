package com.expensetracker.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("FIXED")
public class FixedExpense extends Expense {

    private int dueDay;

    protected FixedExpense() {
    }

    public FixedExpense(String description, double amount, LocalDate date, int dueDay) throws InvalidExpenseException {
        super(description, amount, date);
        if (dueDay < 1 || dueDay > 31) {
            throw new InvalidExpenseException("Due day must be between 1 and 31.");
        }
        this.dueDay = dueDay;
    }

    public int getDueDay() {
        return dueDay;
    }

    public void setDueDay(int dueDay) throws InvalidExpenseException {
        if (dueDay < 1 || dueDay > 31) {
            throw new InvalidExpenseException("Due day must be between 1 and 31.");
        }
        this.dueDay = dueDay;
    }

    @Override
    public double calculateMonthlyImpact() {
        return getAmount();
    }

    @Override
    public String getCategoryLabel() {
        return "Fixed";
    }

    @Override
    public String getType() {
        return "FIXED";
    }
}
