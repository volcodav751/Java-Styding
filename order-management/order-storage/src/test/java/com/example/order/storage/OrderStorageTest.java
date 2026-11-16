package com.example.order.storage;

import com.example.order.model.Electronics;
import com.example.order.model.Order;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderStorageTest {
    private final Electronics product = Electronics.builder().name("Монітор").price(5000).build();

    @Test
    void savesAllOrdersFromTwoThreads() throws InterruptedException {
        OrderStorage storage = new OrderStorage();
        CountDownLatch start = new CountDownLatch(1);

        Thread first = new Thread(() -> saveOrders(storage, start, 1));
        Thread second = new Thread(() -> saveOrders(storage, start, 501));
        first.start();
        second.start();
        start.countDown();
        first.join();
        second.join();

        assertEquals(1000, storage.getAll().size());
        assertEquals(1000L, storage.getAll().stream().map(Order::getId).distinct().count());
    }

    @Test
    void returnsCopyOfStoredOrders() {
        OrderStorage storage = new OrderStorage();
        storage.save(new Order<>(1, product, 1));
        storage.getAll().clear();
        assertEquals(1, storage.getAll().size());
    }

    private void saveOrders(OrderStorage storage, CountDownLatch start, int firstId) {
        try {
            start.await();
            for (int i = firstId; i < firstId + 500; i++) {
                storage.save(new Order<>(i, product, 1));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }
    }
}
