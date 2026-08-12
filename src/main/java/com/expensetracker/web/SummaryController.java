package com.expensetracker.web;

import com.expensetracker.service.ExpenseManager;
import com.expensetracker.service.InventoryManager;
import com.expensetracker.web.dto.SummaryDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/summary")
public class SummaryController {

    private final ExpenseManager expenseManager;
    private final InventoryManager inventoryManager;

    public SummaryController(ExpenseManager expenseManager, InventoryManager inventoryManager) {
        this.expenseManager = expenseManager;
        this.inventoryManager = inventoryManager;
    }

    @GetMapping
    public SummaryDto getSummary() {
        SummaryDto dto = new SummaryDto();
        dto.totalAmount = expenseManager.getTotalAmount();
        dto.totalFixed = expenseManager.getTotalByType("FIXED");
        dto.totalVariable = expenseManager.getTotalByType("VARIABLE");
        dto.totalMonthlyImpact = expenseManager.getTotalMonthlyImpact();
        dto.totalInventoryValue = inventoryManager.getTotalValue();
        return dto;
    }
}
