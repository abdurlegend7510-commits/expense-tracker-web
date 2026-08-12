package com.expensetracker.web.dto;

/** What the frontend sends when updating an expense's amount/description. */
public class UpdateExpenseRequest {
    public double amount;
    public String description;
}
