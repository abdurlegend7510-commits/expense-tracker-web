package com.expensetracker.model;

import java.time.LocalDate;

/**
 * A recurring fixed expense (e.g. rent, subscriptions) that repeats on the
 * same day every month.
 */
public class FixedExpense extends Expense {
    private int dueDay;

    public FixedExpense(int id, String description, double amount, LocalDate date, int dueDay) throws InvalidExpenseException {
        super(id, description, amount, date);
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

    @Override
    public String toFileLine() {
        return "FIXED|" + getId() + "|" + getDescription() + "|" + getAmount() + "|" + getDate() + "|" + dueDay;
    }
}
