package com.example.order.model;

import lombok.Builder;

public class Clothing extends Product {
    private final String size;
    private final String material;

    @Builder
    public Clothing(String name, double price, String size, String material) {
        super(name, price);
        this.size = size;
        this.material = material;
    }

    public String getSize() {
        return size;
    }

    public String getMaterial() {
        return material;
    }

    @Override
    public String getCategory() {
        return "Одяг";
    }
}
