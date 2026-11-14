package com.example.stockmate;

public class Product {

    // Product attributes
    private int id;
    private String name;
    private String category;
    private double price;
    private int quantity;

    // Constructor
    public Product(int id, String name, String category,
                   double price, int quantity) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    // Get Product ID
    public int getId() {
        return id;
    }

    // Get Product Name
    public String getName() {
        return name;
    }

    // Get Product Category
    public String getCategory() {
        return category;
    }

    // Get Product Price
    public double getPrice() {
        return price;
    }

    // Get Product Quantity
    public int getQuantity() {
        return quantity;
    }

    // Calculate Total Stock Value
    public double getStockValue() {
        return price * quantity;
    }
}