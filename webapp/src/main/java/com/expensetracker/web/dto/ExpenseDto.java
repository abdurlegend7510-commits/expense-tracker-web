package com.expensetracker.web.dto;

/** What the frontend receives when it asks for expenses. */
public class ExpenseDto {
    public Long id;
    public String description;
    public double amount;
    public String date;
    public String type;          // "FIXED" or "VARIABLE"
    public String categoryLabel; // "Fixed" or the variable category
    public Integer dueDay;       // only set for FIXED
    public String category;      // only set for VARIABLE
    public double monthlyImpact;
}
