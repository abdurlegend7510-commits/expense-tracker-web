package com.expensetracker.web;

import com.expensetracker.model.InvalidInventoryException;
import com.expensetracker.model.InventoryItem;
import com.expensetracker.service.InventoryManager;
import com.expensetracker.web.dto.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryManager manager;

    public InventoryController(InventoryManager manager) {
        this.manager = manager;
    }

    @GetMapping
    public List<InventoryDto> getAll(@RequestParam(required = false) String keyword) {
        List<InventoryItem> source;
        if (keyword == null || keyword.trim().isEmpty()) {
            source = manager.getAllItems();
        } else {
            source = manager.searchByKeyword(keyword);
        }
        List<InventoryDto> result = new ArrayList<InventoryDto>();
        for (int i = 0; i < source.size(); i++) {
            result.add(toDto(source.get(i)));
        }
        return result;
    }

    @PostMapping
    public InventoryDto add(@RequestBody AddInventoryRequest request) throws InvalidInventoryException {
        InventoryItem created = manager.addItem(request.itemName, request.quantity, request.unitPrice);
        return toDto(created);
    }

    @PutMapping("/{id}")
    public InventoryDto update(@PathVariable Long id, @RequestBody UpdateInventoryRequest request) throws InvalidInventoryException {
        boolean success = manager.updateItem(id, request.quantity, request.unitPrice);
        if (!success) {
            throw new NoSuchElementFoundException("No inventory item found with id " + id);
        }
        return toDto(manager.searchById(id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        boolean success = manager.deleteItem(id);
        if (!success) {
            throw new NoSuchElementFoundException("No inventory item found with id " + id);
        }
    }

    private InventoryDto toDto(InventoryItem item) {
        InventoryDto dto = new InventoryDto();
        dto.id = item.getId();
        dto.itemName = item.getItemName();
        dto.quantity = item.getQuantity();
        dto.unitPrice = item.getUnitPrice();
        dto.totalValue = item.getTotalValue();
        return dto;
    }
}
