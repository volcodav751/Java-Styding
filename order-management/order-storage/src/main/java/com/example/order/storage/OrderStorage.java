package com.example.order.storage;

import com.example.order.model.Order;
import com.example.order.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderStorage {
    private final List<Order<? extends Product>> orders = new ArrayList<>();

    public synchronized void save(Order<? extends Product> order) {
        orders.add(Objects.requireNonNull(order));
    }

    public synchronized List<Order<? extends Product>> getAll() {
        return new ArrayList<>(orders);
    }
}
