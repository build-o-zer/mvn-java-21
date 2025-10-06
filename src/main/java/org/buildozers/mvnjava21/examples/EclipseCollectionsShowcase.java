package org.buildozers.mvnjava21.examples;

import static org.buildozers.mvnjava21.examples.ConsoleColors.blank;
import static org.buildozers.mvnjava21.examples.ConsoleColors.blue;
import static org.buildozers.mvnjava21.examples.ConsoleColors.green;
import static org.buildozers.mvnjava21.examples.ConsoleColors.separator;
import static org.buildozers.mvnjava21.examples.ConsoleColors.white;
import static org.buildozers.mvnjava21.examples.ConsoleColors.whiteLabel;
import static org.buildozers.mvnjava21.examples.ConsoleColors.whiteLabelBold;
import static org.buildozers.mvnjava21.examples.ConsoleColors.yellow;
import static org.fusesource.jansi.Ansi.Color.BLUE;
import static org.fusesource.jansi.Ansi.Color.CYAN;
import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.MAGENTA;
import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.Color.YELLOW;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Maps;
import org.eclipse.collections.impl.factory.Sets;
import org.fusesource.jansi.AnsiConsole;

import lombok.extern.java.Log;

/**
 * 🚀 Eclipse Collections Showcase
 * 
 * This class demonstrates the powerful features of Eclipse Collections
 * combined with Lombok for boilerplate reduction and Jansi for colorful output.
 */
@Log
public class EclipseCollectionsShowcase {

    public static void main(String[] args) {
        // Initialize Jansi for colored output
        AnsiConsole.systemInstall();
        
        EclipseCollectionsShowcase showcase = new EclipseCollectionsShowcase();
        
        try {
            showcase.printHeader();
            showcase.demonstrateLists();
            showcase.demonstrateMaps();
            showcase.demonstrateSets();
            showcase.demonstrateAdvancedOperations();
        } finally {
            AnsiConsole.systemUninstall();
        }
    }

    private void printHeader() {
        separator();
        yellow("🚀 Eclipse Collections Showcase with Lombok & Jansi");
        green("✨ Demonstrating functional programming with high-performance collections");
        separator();
        blank();
    }

    private void demonstrateLists() {
        this.printSectionHeader("📋 Eclipse Collections Lists");

        // Create a MutableList of products
        MutableList<Product> products = Lists.mutable.of(
            Product.builder()
                .name("MacBook Pro")
                .category("Electronics")
                .price(2499.99)
                .quantity(5)
                .createdAt(LocalDateTime.now().minusDays(10))
                .build(),
            Product.builder()
                .name("iPhone 15")
                .category("Electronics")
                .price(999.99)
                .quantity(15)
                .createdAt(LocalDateTime.now().minusDays(5))
                .build(),
            Product.builder()
                .name("Coffee Mug")
                .category("Kitchen")
                .price(19.99)
                .quantity(50)
                .createdAt(LocalDateTime.now().minusDays(2))
                .build(),
            Product.builder()
                .name("Wireless Mouse")
                .category("Electronics")
                .price(79.99)
                .quantity(8)
                .createdAt(LocalDateTime.now().minusDays(1))
                .build()
        );

        whiteLabelBold("📦 Total products: ", String.valueOf(products.size()), CYAN);

        // Functional operations with Eclipse Collections
        MutableList<Product> expensiveProducts = products.select(Product::isExpensive);
        whiteLabelBold("💰 Expensive products (>$100): ", String.valueOf(expensiveProducts.size()), RED);

        MutableList<Product> lowStockProducts = products.select(Product::isLowStock);
        whiteLabelBold("⚠️  Low stock products (<10): ", String.valueOf(lowStockProducts.size()), YELLOW);

        // Transform data
        MutableList<String> productNames = products.collect(Product::getName);
        whiteLabel("🏷️  Product names: ", productNames.makeString(", "), GREEN);

        // Calculate total inventory value
        double totalValue = products.sumOfDouble(Product::getTotalValue);
        whiteLabelBold("💎 Total inventory value: ", "$" + String.format("%.2f", totalValue), MAGENTA);

        blank();
    }

    private void demonstrateMaps() {
        this.printSectionHeader("🗺️  Eclipse Collections Maps");

        // Create a MutableMap of customers
        MutableMap<String, Customer> customers = Maps.mutable.empty();
        
        customers.put("C001", Customer.builder()
            .name("Alice Johnson")
            .email("alice@example.com")
            .city("New York")
            .age(28)
            .totalSpent(1250.75)
            .build());
            
        customers.put("C002", Customer.builder()
            .name("Bob Smith")
            .email("bob@example.com")
            .city("San Francisco")
            .age(35)
            .totalSpent(750.50)
            .build());
            
        customers.put("C003", Customer.builder()
            .name("Carol Williams")
            .email("carol@example.com")
            .city("Chicago")
            .age(42)
            .totalSpent(2100.25)
            .build());

        whiteLabelBold("👥 Total customers: ", String.valueOf(customers.size()), CYAN);

        // Filter premium customers
        MutableMap<String, Customer> premiumCustomers = customers.select((id, customer) -> customer.isPremium());
        whiteLabelBold("⭐ Premium customers: ", String.valueOf(premiumCustomers.size()), YELLOW);

        // Group by city
        var customersByCity = customers.groupBy(Customer::getCity);
        white("🏙️  Customers by city:");
        customersByCity.forEachKeyMultiValues((city, cityCustomers) -> {
            whiteLabel("  • " + city + ": ", cityCustomers.size() + " customers", CYAN);
        });

        // Calculate average spending
        double averageSpending = customers.sumOfDouble(Customer::getTotalSpent) / customers.size();
        whiteLabelBold("📊 Average customer spending: ", "$" + String.format("%.2f", averageSpending), MAGENTA);

        blank();
    }

    private void demonstrateSets() {
        this.printSectionHeader("🎯 Eclipse Collections Sets");

        // Create sets for different categories
        MutableSet<String> electronicsFeatures = Sets.mutable.of(
            "Bluetooth", "WiFi", "USB-C", "Retina Display", "Touch ID", "Wireless Charging"
        );

        MutableSet<String> premiumFeatures = Sets.mutable.of(
            "Premium Design", "Retina Display", "Touch ID", "Face ID", "Wireless Charging", "Fast Charging"
        );

        MutableSet<String> mobileFeatures = Sets.mutable.of(
            "5G", "Face ID", "Wireless Charging", "Camera", "GPS", "Bluetooth"
        );

        whiteLabel("📱 Electronics features: ", electronicsFeatures.makeString(", "), CYAN);
        whiteLabel("⭐ Premium features: ", premiumFeatures.makeString(", "), YELLOW);
        whiteLabel("📱 Mobile features: ", mobileFeatures.makeString(", "), GREEN);

        // Set operations
        MutableSet<String> commonElectronicsPremium = electronicsFeatures.intersect(premiumFeatures);
        whiteLabel("🔗 Common electronics & premium features: ", commonElectronicsPremium.makeString(", "), MAGENTA);

        MutableSet<String> uniqueToElectronics = electronicsFeatures.difference(premiumFeatures);
        whiteLabel("🔧 Unique to electronics: ", uniqueToElectronics.makeString(", "), BLUE);

        MutableSet<String> allFeatures = electronicsFeatures.union(premiumFeatures).union(mobileFeatures);
        whiteLabel("🌟 All unique features: ", allFeatures.size() + " features", RED);

        blank();
    }

    private void demonstrateAdvancedOperations() {
        this.printSectionHeader("🎪 Advanced Operations");

        // Generate sample data using IntStream instead of for-loop
        MutableList<Integer> numbers = IntStream.range(0, 20)
            .map(i -> ThreadLocalRandom.current().nextInt(1, 101))
            .collect(Lists.mutable::empty, MutableList::add, MutableList::addAll);

        whiteLabel("🎲 Random numbers: ", numbers.makeString(", "), CYAN);

        // Partition numbers
        var partitioned = numbers.partition(n -> n % 2 == 0);
        MutableList<Integer> evenNumbers = partitioned.getSelected();
        MutableList<Integer> oddNumbers = partitioned.getRejected();

        whiteLabel("➡️  Even numbers: ", evenNumbers.makeString(", "), GREEN);
        whiteLabel("⬅️  Odd numbers: ", oddNumbers.makeString(", "), YELLOW);

        // Statistical operations
        long sum = numbers.sumOfInt(Integer::intValue);
        double average = (double) sum / numbers.size();
        int min = numbers.minBy(Integer::intValue);
        int max = numbers.maxBy(Integer::intValue);

        white("📊 Statistics:");
        whiteLabelBold("  • Sum: ", String.valueOf(sum), CYAN);
        whiteLabelBold("  • Average: ", String.format("%.2f", average), CYAN);
        whiteLabelBold("  • Min: ", String.valueOf(min), CYAN);
        whiteLabelBold("  • Max: ", String.valueOf(max), CYAN);

        // Transform operations
        MutableList<Integer> squares = numbers.collect(n -> n * n);

        whiteLabel("🔢 Squares: ", squares.makeString(", "), MAGENTA);

        this.printFooter();
    }

    private void printSectionHeader(String title) {
        blue(title);
        blue("─".repeat(title.length()));
    }

    private void printFooter() {
        blank();
        separator();
        green("✅ Eclipse Collections showcase completed successfully!");
        yellow("🎉 Powered by Eclipse Collections " + RuntimeVersionDetector.detectVersion(Lists.class, "eclipse-collections") + ", Lombok, and Jansi");
        separator();
    }


}
