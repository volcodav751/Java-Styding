package com.example.order.threads;

import com.example.order.model.Electronics;
import com.example.order.model.Order;
import com.example.order.processing.OrderProcessor;
import com.example.order.storage.OrderStorage;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderTaskTest {
    @Test
    void continuesAfterInvalidOrder() {
        Electronics product = Electronics.builder().name("Ноутбук").price(20000).build();
        Order<Electronics> invalidOrder = new Order<>(1, product, 0);
        Order<Electronics> validOrder = new Order<>(2, product, 1);
        OrderStorage storage = new OrderStorage();

        OrderTask<Electronics> task = new OrderTask<>(
                List.of(invalidOrder, validOrder), new OrderProcessor<>(), storage);
        task.run();

        assertEquals(1, storage.getAll().size());
        assertEquals(2, storage.getAll().get(0).getId());
    }
}
