package com.expensetracker.service;

import com.expensetracker.model.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Same public API as before (addFixedExpense, searchByKeyword,
 * updateExpense, deleteExpense, getTotal...) - only the storage
 * underneath changed, from an ArrayList + text file to a database via
 * ExpenseRepository. Controllers don't need to know the difference.
 */
@Service
public class ExpenseManager {

    private final ExpenseRepository repository;

    public ExpenseManager(ExpenseRepository repository) {
        this.repository = repository;
    }

    public Expense addFixedExpense(String description, double amount, LocalDate date, int dueDay) throws InvalidExpenseException {
        FixedExpense fe = new FixedExpense(description, amount, date, dueDay);
        return repository.save(fe);
    }

    public Expense addVariableExpense(String description, double amount, LocalDate date, String category) throws InvalidExpenseException {
        VariableExpense ve = new VariableExpense(description, amount, date, category);
        return repository.save(ve);
    }

    public Expense searchById(Long id) {
        Optional<Expense> found = repository.findById(id);
        return found.orElse(null);
    }

    public List<Expense> searchByKeyword(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        List<Expense> results = new ArrayList<Expense>();
        List<Expense> all = repository.findAll();
        for (int i = 0; i < all.size(); i++) {
            Expense e = all.get(i);
            if (e.getDescription().toLowerCase().contains(lowerKeyword)
                    || e.getCategoryLabel().toLowerCase().contains(lowerKeyword)) {
                results.add(e);
            }
        }
        return results;
    }

    public boolean updateExpense(Long id, double newAmount, String newDescription) throws InvalidExpenseException {
        Expense e = searchById(id);
        if (e == null) {
            return false;
        }
        e.setAmount(newAmount);
        e.setDescription(newDescription);
        repository.save(e);
        return true;
    }

    public boolean deleteExpense(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    public List<Expense> getAllExpenses() {
        return repository.findAll();
    }

    public double getTotalAmount() {
        double total = 0;
        List<Expense> all = repository.findAll();
        for (int i = 0; i < all.size(); i++) {
            total = total + all.get(i).getAmount();
        }
        return total;
    }

    public double getTotalByType(String type) {
        double total = 0;
        List<Expense> all = repository.findAll();
        for (int i = 0; i < all.size(); i++) {
            Expense e = all.get(i);
            if (type.equals(e.getType())) {
                total = total + e.getAmount();
            }
        }
        return total;
    }

    public double getTotalMonthlyImpact() {
        double total = 0;
        List<Expense> all = repository.findAll();
        for (int i = 0; i < all.size(); i++) {
            total = total + all.get(i).calculateMonthlyImpact();
        }
        return total;
    }
}
