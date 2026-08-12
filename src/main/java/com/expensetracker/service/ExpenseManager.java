package com.expensetracker.service;

import com.expensetracker.model.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Owns the lifecycle of every Expense object (composition).
 * @Service makes Spring create exactly one instance and hand it to
 * every controller that asks for it - the web equivalent of the single
 * ExpenseManager the Swing MainGUI used to hold.
 */
@Service
public class ExpenseManager {
    private static final String FILE_NAME = "data/expenses.txt";

    private ArrayList<Expense> expenses;
    private int nextId;

    public ExpenseManager() {
        expenses = new ArrayList<Expense>();
        nextId = 1;
    }

    @PostConstruct
    public void init() {
        loadFromFile(FILE_NAME);
    }

    public Expense addFixedExpense(String description, double amount, LocalDate date, int dueDay) throws InvalidExpenseException {
        FixedExpense fe = new FixedExpense(nextId, description, amount, date, dueDay);
        expenses.add(fe);
        nextId++;
        saveToFile(FILE_NAME);
        return fe;
    }

    public Expense addVariableExpense(String description, double amount, LocalDate date, String category) throws InvalidExpenseException {
        VariableExpense ve = new VariableExpense(nextId, description, amount, date, category);
        expenses.add(ve);
        nextId++;
        saveToFile(FILE_NAME);
        return ve;
    }

    public Expense searchById(int id) {
        for (int i = 0; i < expenses.size(); i++) {
            if (expenses.get(i).getId() == id) {
                return expenses.get(i);
            }
        }
        return null;
    }

    public ArrayList<Expense> searchByKeyword(String keyword) {
        ArrayList<Expense> results = new ArrayList<Expense>();
        String lowerKeyword = keyword.toLowerCase();
        for (int i = 0; i < expenses.size(); i++) {
            Expense e = expenses.get(i);
            if (e.getDescription().toLowerCase().contains(lowerKeyword)
                    || e.getCategoryLabel().toLowerCase().contains(lowerKeyword)) {
                results.add(e);
            }
        }
        return results;
    }

    public boolean updateExpense(int id, double newAmount, String newDescription) throws InvalidExpenseException {
        Expense e = searchById(id);
        if (e == null) {
            return false;
        }
        e.setAmount(newAmount);
        e.setDescription(newDescription);
        saveToFile(FILE_NAME);
        return true;
    }

    public boolean deleteExpense(int id) {
        Expense e = searchById(id);
        if (e == null) {
            return false;
        }
        expenses.remove(e);
        saveToFile(FILE_NAME);
        return true;
    }

    public ArrayList<Expense> getAllExpenses() {
        return expenses;
    }

    public double getTotalAmount() {
        double total = 0;
        for (int i = 0; i < expenses.size(); i++) {
            total = total + expenses.get(i).getAmount();
        }
        return total;
    }

    public double getTotalByType(String type) {
        double total = 0;
        for (int i = 0; i < expenses.size(); i++) {
            Expense e = expenses.get(i);
            if (type.equals("FIXED") && e instanceof FixedExpense) {
                total = total + e.getAmount();
            } else if (type.equals("VARIABLE") && e instanceof VariableExpense) {
                total = total + e.getAmount();
            }
        }
        return total;
    }

    public double getTotalMonthlyImpact() {
        double total = 0;
        for (int i = 0; i < expenses.size(); i++) {
            total = total + expenses.get(i).calculateMonthlyImpact();
        }
        return total;
    }

    public void saveToFile(String filename) {
        try {
            File file = new File(filename);
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            PrintWriter writer = new PrintWriter(new FileWriter(file));
            for (int i = 0; i < expenses.size(); i++) {
                writer.println(expenses.get(i).toFileLine());
            }
            writer.close();
        } catch (IOException ex) {
            System.out.println("Could not save expenses: " + ex.getMessage());
        }
    }

    public void loadFromFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            return;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            int highestId = 0;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|");
                try {
                    if (parts[0].equals("FIXED") && parts.length == 6) {
                        int id = Integer.parseInt(parts[1]);
                        String description = parts[2];
                        double amount = Double.parseDouble(parts[3]);
                        LocalDate date = LocalDate.parse(parts[4]);
                        int dueDay = Integer.parseInt(parts[5]);
                        expenses.add(new FixedExpense(id, description, amount, date, dueDay));
                        if (id > highestId) {
                            highestId = id;
                        }
                    } else if (parts[0].equals("VARIABLE") && parts.length == 6) {
                        int id = Integer.parseInt(parts[1]);
                        String description = parts[2];
                        double amount = Double.parseDouble(parts[3]);
                        LocalDate date = LocalDate.parse(parts[4]);
                        String category = parts[5];
                        expenses.add(new VariableExpense(id, description, amount, date, category));
                        if (id > highestId) {
                            highestId = id;
                        }
                    }
                } catch (Exception lineError) {
                    System.out.println("Skipped a corrupted line while loading expenses.");
                }
            }
            reader.close();
            nextId = highestId + 1;
        } catch (IOException ex) {
            System.out.println("Could not load expenses: " + ex.getMessage());
        }
    }
}
