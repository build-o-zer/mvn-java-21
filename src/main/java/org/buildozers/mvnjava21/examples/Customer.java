package org.buildozers.mvnjava21.examples;

import lombok.Builder;
import lombok.Data;

/**
 * 👤 Customer Model
 * 
 * Represents a customer with business logic for classification.
 * Demonstrates Lombok integration for boilerplate reduction.
 */
@Data
@Builder
public class Customer {
    private final String name;
    private final String email;
    private final String city;
    private final int age;
    private final double totalSpent;

    public boolean isPremium() {
        return totalSpent > 1000.0;
    }

    public boolean isYoung() {
        return age < 30;
    }
}
