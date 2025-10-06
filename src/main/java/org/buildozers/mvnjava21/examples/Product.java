package org.buildozers.mvnjava21.examples;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

/**
 * 📦 Product Model
 * 
 * Represents a product with business logic for inventory management.
 * Demonstrates Lombok integration for boilerplate reduction.
 */
@Data
@Builder
public class Product {
    private final String name;
    private final String category;
    private final double price;
    private final int quantity;
    private final LocalDateTime createdAt;

    public double getTotalValue() {
        return price * quantity;
    }

    public boolean isExpensive() {
        return price > 100.0;
    }

    public boolean isLowStock() {
        return quantity < 10;
    }
}
