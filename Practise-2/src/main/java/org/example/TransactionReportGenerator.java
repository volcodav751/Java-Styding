package org.example;

import java.util.List;
import java.util.Map;

public abstract class TransactionReportGenerator {

    private static final int MONEY_PER_STAR = 1000;

    public static void printTransactions(List<Transaction> transactions) {
        System.out.println(
                "Прочитані транзакції: " + transactions.size()
        );

        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }

    public static void printBalanceReport(double balance) {
        System.out.printf(
                "%nЗагальний баланс: %.2f грн%n",
                balance
        );
    }

    public static void printTransactionsCountByMonth(
            String monthYear,
            int count) {

        System.out.println(
                "Кількість транзакцій за " + monthYear + ": " + count
        );
    }

    public static void printTopExpensesReport(
            List<Transaction> expenses) {

        System.out.println(
                "\nДо 10 найбільших витрат (знайдено "
                        + expenses.size() + "):"
        );

        for (Transaction expense : expenses) {
            printExpense("Витрата", expense);
        }
    }

    public static void printExpense(
            String title,
            Transaction expense) {

        if (expense == null) {
            System.out.println(
                    title + ": витрат за цей період немає."
            );
        } else {
            System.out.printf(
                    "%s: %s | %s | %.2f грн%n",
                    title,
                    expense.getDate(),
                    expense.getDescription(),
                    -expense.getAmount()
            );
        }
    }

    public static void printExpensesChart(
            String title,
            Map<String, Double> totals) {

        System.out.println("\n" + title);
        System.out.println(
                "* = 1000 грн; + = залишок менше 1000 грн."
        );

        if (totals.isEmpty()) {
            System.out.println("Витрат немає.");
        }

        for (Map.Entry<String, Double> entry : totals.entrySet()) {
            double amount = entry.getValue();

            int stars = (int) (amount / MONEY_PER_STAR);
            String chart = "*".repeat(stars);

            if (amount - stars * MONEY_PER_STAR > 0.001) {
                chart += "+";
            }

            System.out.printf(
                    "%-25s %10.2f грн  %s%n",
                    entry.getKey(),
                    amount,
                    chart
            );
        }
    }
}