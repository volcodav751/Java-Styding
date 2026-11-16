package com.example.order.processing;

import com.example.order.model.Clothing;
import com.example.order.model.Electronics;
import com.example.order.model.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderProcessorTest {
    private final Electronics product = Electronics.builder()
            .name("Ноутбук")
            .price(20000)
            .brand("Lenovo")
            .warrantyMonths(24)
            .build();

    @Test
    void processesBothProductTypes() {
        Clothing clothing = Clothing.builder()
                .name("Футболка")
                .price(500)
                .size("M")
                .material("Cotton")
                .build();

        Order<Electronics> electronicsOrder = new Order<>(1, product, 2);
        Order<Clothing> clothingOrder = new Order<>(2, clothing, 3);

        assertDoesNotThrow(() -> new OrderProcessor<Electronics>().process(electronicsOrder));
        assertDoesNotThrow(() -> new OrderProcessor<Clothing>().process(clothingOrder));
        assertEquals(40000.0, electronicsOrder.getTotalPrice(), 0.001);
        assertEquals(1500.0, clothingOrder.getTotalPrice(), 0.001);
        assertEquals("Lenovo", product.getBrand());
        assertEquals(24, product.getWarrantyMonths());
        assertEquals("M", clothing.getSize());
        assertEquals("Cotton", clothing.getMaterial());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void rejectsInvalidQuantity(int quantity) {
        Order<Electronics> order = new Order<>(1, product, quantity);
        assertThrows(OrderProcessingException.class,
                () -> new OrderProcessor<Electronics>().process(order));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0, -10, Double.NaN, Double.POSITIVE_INFINITY})
    void rejectsInvalidPrice(double price) {
        Electronics invalidProduct = Electronics.builder().name("Товар").price(price).build();
        Order<Electronics> order = new Order<>(1, invalidProduct, 1);
        assertThrows(OrderProcessingException.class,
                () -> new OrderProcessor<Electronics>().process(order));
    }

    @Test
    void rejectsMissingProduct() {
        Order<Electronics> order = new Order<>(1, null, 1);
        assertThrows(OrderProcessingException.class,
                () -> new OrderProcessor<Electronics>().process(order));
    }

    @Test
    void rejectsMissingOrder() {
        assertThrows(OrderProcessingException.class,
                () -> new OrderProcessor<Electronics>().process(null));
    }

    @Test
    void rejectsEmptyName() {
        Electronics invalidProduct = Electronics.builder().name(" ").price(100).build();
        assertThrows(OrderProcessingException.class,
                () -> new OrderProcessor<Electronics>().process(new Order<>(1, invalidProduct, 1)));
    }

    @Test
    void rejectsInvalidId() {
        assertThrows(OrderProcessingException.class,
                () -> new OrderProcessor<Electronics>().process(new Order<>(0, product, 1)));
    }

    @Test
    void rejectsTotalOverflow() {
        Electronics expensiveProduct = Electronics.builder().name("Товар").price(Double.MAX_VALUE).build();
        assertThrows(OrderProcessingException.class,
                () -> new OrderProcessor<Electronics>().process(new Order<>(1, expensiveProduct, 2)));
    }
}
