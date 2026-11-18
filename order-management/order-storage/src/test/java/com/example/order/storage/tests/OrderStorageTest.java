package com.example.order.storage.tests;

import com.example.order.model.Electronics;
import com.example.order.model.Order;
import com.example.order.storage.OrderStorage;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderStorageTest {
    private final Electronics product = Electronics.builder().name("Монітор").price(5000).build();

    @Test
    void savesAllOrdersFromTwoThreads() throws Exception {
        OrderStorage storage = new OrderStorage();
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService workers = Executors.newFixedThreadPool(2);
        try {
            Future<?> first = workers.submit(() -> saveOrders(storage, ready, start, 1));
            Future<?> second = workers.submit(() -> saveOrders(storage, ready, start, 501));
            assertTrue(ready.await(5, TimeUnit.SECONDS), "Both workers must be ready");
            start.countDown();
            first.get(5, TimeUnit.SECONDS);
            second.get(5, TimeUnit.SECONDS);
        } finally {
            start.countDown();
            workers.shutdownNow();
        }

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

    private void saveOrders(OrderStorage storage, CountDownLatch ready, CountDownLatch start, int firstId) {
        try {
            ready.countDown();
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
