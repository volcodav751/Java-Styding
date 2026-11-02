package org.example.calculator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    @Test
    void testNumberInput() throws InvalidInputException {
        assertEquals(2.5, Main.parseNumber("2.5"));
    }

    @Test
    void testTextInsteadOfNumber() {
        assertThrows(InvalidInputException.class, () -> Main.parseNumber("abc"));
    }
}
