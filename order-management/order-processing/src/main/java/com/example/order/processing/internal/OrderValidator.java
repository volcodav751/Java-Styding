package com.example.order.processing.internal;

import com.example.order.model.Order;
import com.example.order.model.Product;
import com.example.order.processing.OrderProcessingException;

public final class OrderValidator {
    private OrderValidator() {
    }

    public static void validate(Order<? extends Product> order) throws OrderProcessingException {
        if (order == null) {
            throw new OrderProcessingException("Замовлення відсутнє");
        }
        if (order.getId() <= 0) {
            throw new OrderProcessingException("Номер замовлення має бути додатним");
        }
        if (order.getProduct() == null) {
            throw new OrderProcessingException("Товар відсутній");
        }
        if (order.getProduct().getName() == null || order.getProduct().getName().isBlank()) {
            throw new OrderProcessingException("Назва товару порожня");
        }
        double price = order.getProduct().getPrice();
        if (!Double.isFinite(price) || price <= 0) {
            throw new OrderProcessingException("Ціна має бути додатним скінченним числом");
        }
        if (order.getQuantity() <= 0) {
            throw new OrderProcessingException("Кількість товару має бути більшою за нуль");
        }
        if (!Double.isFinite(order.getTotalPrice())) {
            throw new OrderProcessingException("Сума замовлення занадто велика");
        }
    }
}
