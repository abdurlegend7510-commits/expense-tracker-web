package com.expensetracker.web.dto;

/** What the frontend sends when adding a new expense. */
public class AddExpenseRequest {
    public String description;
    public double amount;
    public String date;     // "YYYY-MM-DD"
    public String type;     // "FIXED" or "VARIABLE"
    public Integer dueDay;  // required when type = FIXED
    public String category; // required when type = VARIABLE
}
