package com.example.order.threads;

import com.example.order.model.Order;
import com.example.order.model.Product;
import com.example.order.processing.OrderProcessingException;
import com.example.order.processing.OrderProcessor;
import com.example.order.storage.OrderStorage;

import java.util.List;

public class OrderTask<T extends Product> implements Runnable {
    private final List<Order<T>> orders;
    private final OrderProcessor<T> processor;
    private final OrderStorage storage;

    public OrderTask(List<Order<T>> orders, OrderProcessor<T> processor, OrderStorage storage) {
        this.orders = orders;
        this.processor = processor;
        this.storage = storage;
    }

    @Override
    public void run() {
        orders.forEach(this::processOrder);
    }

    private void processOrder(Order<T> order) {
        try {
            processor.process(order);
            storage.save(order);
        } catch (OrderProcessingException e) {
            System.out.println("[" + Thread.currentThread().getName() + "] Помилка: " + e.getMessage());
        }
    }
}
