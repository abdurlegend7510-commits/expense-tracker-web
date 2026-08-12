package com.expensetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the web version. Spring Boot starts an embedded web
 * server and wires up ExpenseManager/InventoryManager as singletons -
 * this replaces Main.java's "new MainGUI()".
 */
@SpringBootApplication
public class ExpenseTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExpenseTrackerApplication.class, args);
    }
}
