package com.expensetracker.service;

import com.expensetracker.model.InvalidInventoryException;
import com.expensetracker.model.InventoryItem;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;

/**
 * Owns the lifecycle of every InventoryItem (composition), same pattern
 * as ExpenseManager.
 */
@Service
public class InventoryManager {
    private static final String FILE_NAME = "data/inventory.txt";

    private ArrayList<InventoryItem> items;
    private int nextId;

    public InventoryManager() {
        items = new ArrayList<InventoryItem>();
        nextId = 1;
    }

    @PostConstruct
    public void init() {
        loadFromFile(FILE_NAME);
    }

    public InventoryItem addItem(String itemName, int quantity, double unitPrice) throws InvalidInventoryException {
        InventoryItem item = new InventoryItem(nextId, itemName, quantity, unitPrice);
        items.add(item);
        nextId++;
        saveToFile(FILE_NAME);
        return item;
    }

    public InventoryItem searchById(int id) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId() == id) {
                return items.get(i);
            }
        }
        return null;
    }

    public ArrayList<InventoryItem> searchByKeyword(String keyword) {
        ArrayList<InventoryItem> results = new ArrayList<InventoryItem>();
        String lowerKeyword = keyword.toLowerCase();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getItemName().toLowerCase().contains(lowerKeyword)) {
                results.add(items.get(i));
            }
        }
        return results;
    }

    public boolean updateItem(int id, int newQuantity, double newUnitPrice) throws InvalidInventoryException {
        InventoryItem item = searchById(id);
        if (item == null) {
            return false;
        }
        item.setQuantity(newQuantity);
        item.setUnitPrice(newUnitPrice);
        saveToFile(FILE_NAME);
        return true;
    }

    public boolean deleteItem(int id) {
        InventoryItem item = searchById(id);
        if (item == null) {
            return false;
        }
        items.remove(item);
        saveToFile(FILE_NAME);
        return true;
    }

    public ArrayList<InventoryItem> getAllItems() {
        return items;
    }

    public double getTotalValue() {
        double total = 0;
        for (int i = 0; i < items.size(); i++) {
            total = total + items.get(i).getTotalValue();
        }
        return total;
    }

    public int getTotalQuantity() {
        int total = 0;
        for (int i = 0; i < items.size(); i++) {
            total = total + items.get(i).getQuantity();
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
            for (int i = 0; i < items.size(); i++) {
                writer.println(items.get(i).toFileLine());
            }
            writer.close();
        } catch (IOException ex) {
            System.out.println("Could not save inventory: " + ex.getMessage());
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
                    if (parts.length == 4) {
                        int id = Integer.parseInt(parts[0]);
                        String itemName = parts[1];
                        int quantity = Integer.parseInt(parts[2]);
                        double unitPrice = Double.parseDouble(parts[3]);
                        items.add(new InventoryItem(id, itemName, quantity, unitPrice));
                        if (id > highestId) {
                            highestId = id;
                        }
                    }
                } catch (Exception lineError) {
                    System.out.println("Skipped a corrupted line while loading inventory.");
                }
            }
            reader.close();
            nextId = highestId + 1;
        } catch (IOException ex) {
            System.out.println("Could not load inventory: " + ex.getMessage());
        }
    }
}
