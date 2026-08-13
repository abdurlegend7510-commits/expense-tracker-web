package com.expensetracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;
    private int quantity;
    private double unitPrice;

    protected InventoryItem() {
    }

    public InventoryItem(String itemName, int quantity, double unitPrice) throws InvalidInventoryException {
        if (itemName == null || itemName.trim().isEmpty()) {
            throw new InvalidInventoryException("Item name cannot be empty.");
        }
        if (quantity < 0) {
            throw new InvalidInventoryException("Quantity cannot be negative.");
        }
        if (unitPrice <= 0) {
            throw new InvalidInventoryException("Unit price must be positive.");
        }
        this.itemName = itemName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Long getId() {
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
}
