package org.example;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class TransactionAnalyzer {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

    private static final DateTimeFormatter MONTH_FORMAT =
            DateTimeFormatter.ofPattern("MM-uuuu");

    public static LocalDate parseDate(String date) {
        return LocalDate.parse(date, DATE_FORMAT);
    }

     public static double calculateTotalBalance(
            List<Transaction> transactions) {

        double balance = 0;

        for (Transaction transaction : transactions) {
            balance += transaction.getAmount();
        }

        return balance;
    }

     public static int countTransactionsByMonth(
            List<Transaction> transactions,
            String monthYear) {

        YearMonth month = YearMonth.parse(monthYear, MONTH_FORMAT);
        int count = 0;

        for (Transaction transaction : transactions) {
            LocalDate date = parseDate(transaction.getDate());
            YearMonth transactionMonth = YearMonth.from(date);

            if (transactionMonth.equals(month)) {
                count++;
            }
        }

        return count;
    }

     public static List<Transaction> findTopExpenses(
            List<Transaction> transactions) {

        List<Transaction> expenses = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                expenses.add(transaction);
            }
        }

         expenses.sort(
                Comparator.comparingDouble(Transaction::getAmount)
        );

        int count = Math.min(10, expenses.size());

        return new ArrayList<>(expenses.subList(0, count));
    }

     public static List<Transaction> findExpensesByPeriod(
            List<Transaction> transactions,
            String start,
            String end) {

        LocalDate startDate = parseDate(start);
        LocalDate endDate = parseDate(end);

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                    "Початок періоду пізніше за кінець"
            );
        }

        List<Transaction> expenses = new ArrayList<>();

        for (Transaction transaction : transactions) {
            LocalDate date = parseDate(transaction.getDate());

            if (transaction.getAmount() < 0
                    && !date.isBefore(startDate)
                    && !date.isAfter(endDate)) {

                expenses.add(transaction);
            }
        }

        return expenses;
    }

     public static Transaction findLargestExpense(
            List<Transaction> transactions,
            String start,
            String end) {

        Transaction largest = null;

        List<Transaction> expenses =
                findExpensesByPeriod(transactions, start, end);

        for (Transaction transaction : expenses) {
            if (largest == null
                    || transaction.getAmount() < largest.getAmount()) {

                largest = transaction;
            }
        }

        return largest;
    }

     public static Transaction findSmallestExpense(
            List<Transaction> transactions,
            String start,
            String end) {

        Transaction smallest = null;

        List<Transaction> expenses =
                findExpensesByPeriod(transactions, start, end);

        for (Transaction transaction : expenses) {
            if (smallest == null
                    || transaction.getAmount() > smallest.getAmount()) {

                smallest = transaction;
            }
        }

        return smallest;
    }

     public static Map<String, Double> calculateExpensesByCategory(
            List<Transaction> transactions) {

        Map<String, Double> totals = new LinkedHashMap<>();

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                String category = transaction.getDescription();

                double total = totals.getOrDefault(category, 0.0);

                // Віднімаємо від'ємну суму, щоб отримати додатну витрату.
                totals.put(category, total - transaction.getAmount());
            }
        }

        return totals;
    }

     public static Map<String, Double> calculateExpensesByMonth(
            List<Transaction> transactions) {

         Map<YearMonth, Double> totals = new TreeMap<>();

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                LocalDate date = parseDate(transaction.getDate());
                YearMonth month = YearMonth.from(date);

                double total = totals.getOrDefault(month, 0.0);

                totals.put(month, total - transaction.getAmount());
            }
        }

        Map<String, Double> result = new LinkedHashMap<>();

        for (Map.Entry<YearMonth, Double> entry : totals.entrySet()) {
            String month = entry.getKey().format(MONTH_FORMAT);
            result.put(month, entry.getValue());
        }

        return result;
    }
}