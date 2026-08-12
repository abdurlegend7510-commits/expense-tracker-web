package com.expensetracker.model;

/**
 * A single asset/stock item, separate from expenses. Mirrors Expense's
 * encapsulation and validation style so the two models stay consistent.
 */
public class InventoryItem {
    private int id;
    private String itemName;
    private int quantity;
    private double unitPrice;

    public InventoryItem(int id, String itemName, int quantity, double unitPrice) throws InvalidInventoryException {
        if (itemName == null || itemName.trim().isEmpty()) {
            throw new InvalidInventoryException("Item name cannot be empty.");
        }
        if (quantity < 0) {
            throw new InvalidInventoryException("Quantity cannot be negative.");
        }
        if (unitPrice <= 0) {
            throw new InvalidInventoryException("Unit price must be positive.");
        }
        this.id = id;
        this.itemName = itemName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public int getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) throws InvalidInventoryException {
        if (itemName == null || itemName.trim().isEmpty()) {
            throw new InvalidInventoryException("Item name cannot be empty.");
        }
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) throws InvalidInventoryException {
        if (quantity < 0) {
            throw new InvalidInventoryException("Quantity cannot be negative.");
        }
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) throws InvalidInventoryException {
        if (unitPrice <= 0) {
            throw new InvalidInventoryException("Unit price must be positive.");
        }
        this.unitPrice = unitPrice;
    }

    public double getTotalValue() {
        return quantity * unitPrice;
    }

    public String toFileLine() {
        return id + "|" + itemName + "|" + quantity + "|" + unitPrice;
    }
}
