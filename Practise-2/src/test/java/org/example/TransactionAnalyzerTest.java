package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TransactionAnalyzerTest {

    @Test
    void testCalculateTotalBalance() {
        List<Transaction> data = List.of(
                new Transaction("01-01-2023", 100, "Дохід"),
                new Transaction("02-01-2023", -50, "Витрата"),
                new Transaction("03-01-2023", 150, "Дохід")
        );

        double result =
                TransactionAnalyzer.calculateTotalBalance(data);

        assertEquals(200, result, 0.001);
    }

    @Test
    void testCountTransactionsByMonth() {
        List<Transaction> data = List.of(
                new Transaction("01-02-2023", 50, "Дохід"),
                new Transaction("15-02-2023", -20, "Витрата"),
                new Transaction("05-03-2023", 100, "Дохід"),
                new Transaction("01-02-2024", 10, "Дохід")
        );

        assertEquals(
                2,
                TransactionAnalyzer.countTransactionsByMonth(
                        data, "02-2023"
                )
        );

        assertEquals(
                1,
                TransactionAnalyzer.countTransactionsByMonth(
                        data, "03-2023"
                )
        );

        assertEquals(
                0,
                TransactionAnalyzer.countTransactionsByMonth(
                        data, "04-2023"
                )
        );
    }

    @Test
    void testTopTenExpenses() {
        List<Transaction> data = new ArrayList<>();

        data.add(
                new Transaction("01-12-2023", 9000, "Дохід")
        );

        for (int i = 1; i <= 12; i++) {
            data.add(
                    new Transaction(
                            "01-12-2023",
                            -i * 100,
                            "Витрата " + i
                    )
            );
        }

        List<Transaction> top =
                TransactionAnalyzer.findTopExpenses(data);

        assertEquals(10, top.size());

        for (int i = 0; i < 10; i++) {
            assertEquals(
                    -1200 + i * 100,
                    top.get(i).getAmount(),
                    0.001
            );
        }

        // Початковий список не повинен змінюватися.
        assertEquals(13, data.size());
    }

    @Test
    void testPeriodAndExtremes() {
        List<Transaction> data = List.of(
                new Transaction(
                        "30-11-2023", -9000, "Поза періодом"
                ),
                new Transaction(
                        "01-12-2023", -500, "Їжа"
                ),
                new Transaction(
                        "31-12-2023", -50, "Кава"
                ),
                new Transaction(
                        "15-12-2023", 10000, "Дохід"
                ),
                new Transaction(
                        "01-01-2024", -1, "Поза періодом"
                )
        );

        List<Transaction> expenses =
                TransactionAnalyzer.findExpensesByPeriod(
                        data,
                        "01-12-2023",
                        "31-12-2023"
                );

        assertEquals(2, expenses.size());

        Transaction largest =
                TransactionAnalyzer.findLargestExpense(
                        data,
                        "01-12-2023",
                        "31-12-2023"
                );

        Transaction smallest =
                TransactionAnalyzer.findSmallestExpense(
                        data,
                        "01-12-2023",
                        "31-12-2023"
                );

        assertEquals(-500, largest.getAmount(), 0.001);
        assertEquals(-50, smallest.getAmount(), 0.001);

        assertNull(
                TransactionAnalyzer.findLargestExpense(
                        data,
                        "01-02-2024",
                        "29-02-2024"
                )
        );

        assertNull(
                TransactionAnalyzer.findSmallestExpense(
                        data,
                        "01-02-2024",
                        "29-02-2024"
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> TransactionAnalyzer.findExpensesByPeriod(
                        data,
                        "31-12-2023",
                        "01-12-2023"
                )
        );
    }

    @Test
    void testCategoryAndMonthTotals() {
        List<Transaction> data = List.of(
                new Transaction("01-12-2023", -100, "Їжа"),
                new Transaction("02-12-2023", -200, "Їжа"),
                new Transaction("01-01-2024", -50, "Кава"),
                new Transaction("03-12-2023", 5000, "Дохід")
        );

        Map<String, Double> expectedCategories = Map.of(
                "Їжа", 300.0,
                "Кава", 50.0
        );

        assertEquals(
                expectedCategories,
                TransactionAnalyzer.calculateExpensesByCategory(data)
        );

        Map<String, Double> expectedMonths = Map.of(
                "12-2023", 300.0,
                "01-2024", 50.0
        );

        Map<String, Double> actualMonths =
                TransactionAnalyzer.calculateExpensesByMonth(data);

        assertEquals(expectedMonths, actualMonths);

        // Перевіряємо хронологічний порядок місяців.
        assertEquals(
                List.of("12-2023", "01-2024"),
                new ArrayList<>(actualMonths.keySet())
        );
    }

    @Test
    void testEmptyInput() {
        List<Transaction> data = List.of();

        assertEquals(
                0,
                TransactionAnalyzer.calculateTotalBalance(data),
                0.001
        );

        assertTrue(
                TransactionAnalyzer.findTopExpenses(data).isEmpty()
        );

        assertTrue(
                TransactionAnalyzer.calculateExpensesByCategory(data)
                        .isEmpty()
        );
    }
}