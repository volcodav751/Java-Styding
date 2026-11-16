package com.example.order.model;

import java.util.Locale;

public class Order<T extends Product> {
    private final int id;
    private final T product;
    private final int quantity;

    public Order(int id, T product, int quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public T getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "№%d | %s | %s | %d шт. | %.2f грн",
                id, product.getCategory(), product.getName(), quantity, getTotalPrice());
    }
}
