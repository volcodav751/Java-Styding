package com.example.order.threads;

import com.example.order.model.Clothing;
import com.example.order.model.Electronics;
import com.example.order.model.Order;
import com.example.order.model.Product;
import com.example.order.processing.OrderProcessor;
import com.example.order.storage.OrderStorage;
import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Faker faker = new Faker(Locale.ENGLISH);
        OrderStorage storage = new OrderStorage();

        List<Order<Electronics>> electronicsOrders = createElectronicsOrders(faker);
        List<Order<Clothing>> clothingOrders = createClothingOrders(faker);
        electronicsOrders.add(new Order<>(11, electronicsOrders.get(0).getProduct(), 0));

        OrderTask<Electronics> electronicsTask = new OrderTask<>(
                electronicsOrders, new OrderProcessor<>(), storage);
        OrderTask<Clothing> clothingTask = new OrderTask<>(
                clothingOrders, new OrderProcessor<>(), storage);

        Thread electronicsThread = new Thread(() -> electronicsTask.run(), "electronics-thread");
        Thread clothingThread = new Thread(() -> clothingTask.run(), "clothing-thread");

        electronicsThread.start();
        clothingThread.start();
        electronicsThread.join();
        clothingThread.join();

        List<Order<? extends Product>> savedOrders = storage.getAll();
        System.out.println("\nЗбережено замовлень: " + savedOrders.size());

        double total = savedOrders.stream().mapToDouble(Order::getTotalPrice).sum();
        System.out.printf(Locale.US, "Загальна сума: %.2f грн%n", total);

        System.out.println("\nЗбережені замовлення електроніки:");
        savedOrders.stream()
                .filter(order -> order.getProduct() instanceof Electronics)
                .forEach(System.out::println);
    }

    private static List<Order<Electronics>> createElectronicsOrders(Faker faker) {
        List<Order<Electronics>> orders = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Electronics product = Electronics.builder()
                    .name(faker.options().option("Ноутбук", "Смартфон", "Монітор"))
                    .price(faker.number().numberBetween(5000, 50001))
                    .brand(faker.company().name())
                    .warrantyMonths(faker.options().option(12, 24, 36))
                    .build();
            orders.add(new Order<>(i, product, faker.number().numberBetween(1, 4)));
        }
        return orders;
    }

    private static List<Order<Clothing>> createClothingOrders(Faker faker) {
        List<Order<Clothing>> orders = new ArrayList<>();
        for (int i = 6; i <= 10; i++) {
            Clothing product = Clothing.builder()
                    .name(faker.options().option("Футболка", "Куртка", "Джинси"))
                    .price(faker.number().numberBetween(300, 5001))
                    .size(faker.options().option("S", "M", "L", "XL"))
                    .material(faker.commerce().material())
                    .build();
            orders.add(new Order<>(i, product, faker.number().numberBetween(1, 4)));
        }
        return orders;
    }
}
