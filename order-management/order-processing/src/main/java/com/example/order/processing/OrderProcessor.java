package com.example.order.processing;

import com.example.order.model.Order;
import com.example.order.model.Product;
import com.example.order.processing.internal.OrderValidator;

public class OrderProcessor<T extends Product> {
    public void process(Order<T> order) throws OrderProcessingException {
        OrderValidator.validate(order);
        System.out.println("[" + Thread.currentThread().getName() + "] Оброблено " + order);
    }
}
