package com.expensetracker.service;

import com.expensetracker.model.InvalidInventoryException;
import com.expensetracker.model.InventoryItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryManager {

    private final InventoryRepository repository;

    public InventoryManager(InventoryRepository repository) {
        this.repository = repository;
    }

    public InventoryItem addItem(String itemName, int quantity, double unitPrice) throws InvalidInventoryException {
        InventoryItem item = new InventoryItem(itemName, quantity, unitPrice);
        return repository.save(item);
    }

    public InventoryItem searchById(Long id) {
        Optional<InventoryItem> found = repository.findById(id);
        return found.orElse(null);
    }

    public List<InventoryItem> searchByKeyword(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        List<InventoryItem> results = new ArrayList<InventoryItem>();
        List<InventoryItem> all = repository.findAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getItemName().toLowerCase().contains(lowerKeyword)) {
                results.add(all.get(i));
            }
        }
        return results;
    }

    public boolean updateItem(Long id, int newQuantity, double newUnitPrice) throws InvalidInventoryException {
        InventoryItem item = searchById(id);
        if (item == null) {
            return false;
        }
        item.setQuantity(newQuantity);
        item.setUnitPrice(newUnitPrice);
        repository.save(item);
        return true;
    }

    public boolean deleteItem(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    public List<InventoryItem> getAllItems() {
        return repository.findAll();
    }

    public double getTotalValue() {
        double total = 0;
        List<InventoryItem> all = repository.findAll();
        for (int i = 0; i < all.size(); i++) {
            total = total + all.get(i).getTotalValue();
        }
        return total;
    }
}
