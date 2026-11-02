package org.example.calculator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
    private final Calculator calculator = new Calculator();

    @Test
    void testAdd() throws InvalidInputException {
        assertEquals(7.0, calculator.add(3, 4));
    }

    @Test
    void testSubtract() throws InvalidInputException {
        assertEquals(2.0, calculator.subtract(5, 3));
    }

    @Test
    void testMultiply() throws InvalidInputException {
        assertEquals(12.0, calculator.multiply(3, 4));
    }

    @Test
    void testDivide() throws InvalidInputException {
        assertEquals(2.0, calculator.divide(8, 4));
    }

    @Test
    void testDivisionByZero() {
        assertThrows(ArithmeticException.class, () -> calculator.divide(5, 0));
    }

    @Test
    void testInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> calculator.add(Double.NaN, 2));
    }
}
