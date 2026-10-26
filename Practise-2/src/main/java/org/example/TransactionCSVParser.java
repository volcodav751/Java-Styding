package org.example;

import java.util.ArrayList;
import java.util.List;

public abstract class TransactionCSVParser {

    public static List<Transaction> parse(List<String> lines) {
        List<Transaction> transactions = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i)
                    .replace("\uFEFF", "")
                    .trim();

            if (line.isEmpty() || line.equals("Дата,Сума,Категорія")) {
                continue;
            }

            String[] values = line.split(",", 3);

            if (values.length != 3) {
                throw new IllegalArgumentException(
                        "Неправильний CSV, рядок " + (i + 1)
                );
            }

            try {
                String date = values[0].trim();
                double amount = Double.parseDouble(values[1].trim());
                String description = values[2].trim();

                 TransactionAnalyzer.parseDate(date);

                if (!Double.isFinite(amount) || description.isEmpty()) {
                    throw new IllegalArgumentException(
                            "Некоректна сума або категорія"
                    );
                }

                transactions.add(
                        new Transaction(date, amount, description)
                );

            } catch (RuntimeException e) {
                throw new IllegalArgumentException(
                        "Помилка в CSV, рядок " + (i + 1) + ": " + line,
                        e
                );
            }
        }

        return transactions;
    }
}