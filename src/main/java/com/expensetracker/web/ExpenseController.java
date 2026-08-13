package com.expensetracker.web;

import com.expensetracker.model.Expense;
import com.expensetracker.model.FixedExpense;
import com.expensetracker.model.InvalidExpenseException;
import com.expensetracker.service.ExpenseManager;
import com.expensetracker.web.dto.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseManager manager;

    public ExpenseController(ExpenseManager manager) {
        this.manager = manager;
    }

    @GetMapping
    public List<ExpenseDto> getAll(@RequestParam(required = false) String keyword) {
        List<Expense> source;
        if (keyword == null || keyword.trim().isEmpty()) {
            source = manager.getAllExpenses();
        } else {
            source = manager.searchByKeyword(keyword);
        }
        List<ExpenseDto> result = new ArrayList<ExpenseDto>();
        for (int i = 0; i < source.size(); i++) {
            result.add(toDto(source.get(i)));
        }
        return result;
    }

    @PostMapping
    public ExpenseDto add(@RequestBody AddExpenseRequest request) throws InvalidExpenseException {
        LocalDate date;
        try {
            date = LocalDate.parse(request.date);
        } catch (DateTimeParseException ex) {
            throw new InvalidExpenseException("Date must be in YYYY-MM-DD format.");
        }

        Expense created;
        if ("FIXED".equalsIgnoreCase(request.type)) {
            if (request.dueDay == null) {
                throw new InvalidExpenseException("Due day is required for a Fixed expense.");
            }
            created = manager.addFixedExpense(request.description, request.amount, date, request.dueDay);
        } else if ("VARIABLE".equalsIgnoreCase(request.type)) {
            created = manager.addVariableExpense(request.description, request.amount, date, request.category);
        } else {
            throw new InvalidExpenseException("Type must be FIXED or VARIABLE.");
        }
        return toDto(created);
    }

    @PutMapping("/{id}")
    public ExpenseDto update(@PathVariable Long id, @RequestBody UpdateExpenseRequest request) throws InvalidExpenseException {
        boolean success = manager.updateExpense(id, request.amount, request.description);
        if (!success) {
            throw new NoSuchElementFoundException("No expense found with id " + id);
        }
        return toDto(manager.searchById(id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        boolean success = manager.deleteExpense(id);
        if (!success) {
            throw new NoSuchElementFoundException("No expense found with id " + id);
        }
    }

    private ExpenseDto toDto(Expense e) {
        ExpenseDto dto = new ExpenseDto();
        dto.id = e.getId();
        dto.description = e.getDescription();
        dto.amount = e.getAmount();
        dto.date = e.getDate().toString();
        dto.type = e.getType();
        dto.categoryLabel = e.getCategoryLabel();
        dto.monthlyImpact = e.calculateMonthlyImpact();
        if (e instanceof FixedExpense) {
            dto.dueDay = ((FixedExpense) e).getDueDay();
        } else {
            dto.category = e.getCategoryLabel();
        }
        return dto;
    }
}
