package org.example;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String monthYear = "12-2023";
        String start = "01-12-2023";
        String end = "31-12-2023";

        try {
            List<String> lines;

            if (args.length == 0) {
                lines = TransactionCSVReader.readResource(
                        "/transactions.csv"
                );
            } else {
                lines = TransactionCSVReader.readLines(
                        Path.of(args[0])
                );
            }

            List<Transaction> transactions =
                    TransactionCSVParser.parse(lines);

            TransactionReportGenerator.printTransactions(transactions);

            double balance =
                    TransactionAnalyzer.calculateTotalBalance(transactions);

            TransactionReportGenerator.printBalanceReport(balance);

            int count = TransactionAnalyzer.countTransactionsByMonth(
                    transactions,
                    monthYear
            );

            TransactionReportGenerator.printTransactionsCountByMonth(
                    monthYear,
                    count
            );

            List<Transaction> topExpenses =
                    TransactionAnalyzer.findTopExpenses(transactions);

            TransactionReportGenerator.printTopExpensesReport(
                    topExpenses
            );

            System.out.println(
                    "\nПеріод: " + start + " — " + end
            );

            Transaction largest =
                    TransactionAnalyzer.findLargestExpense(
                            transactions,
                            start,
                            end
                    );

            TransactionReportGenerator.printExpense(
                    "Найбільша витрата",
                    largest
            );

            Transaction smallest =
                    TransactionAnalyzer.findSmallestExpense(
                            transactions,
                            start,
                            end
                    );

            TransactionReportGenerator.printExpense(
                    "Найменша витрата",
                    smallest
            );

            TransactionReportGenerator.printExpensesChart(
                    "Витрати за категоріями",
                    TransactionAnalyzer.calculateExpensesByCategory(
                            transactions
                    )
            );

            TransactionReportGenerator.printExpensesChart(
                    "Витрати за місяцями",
                    TransactionAnalyzer.calculateExpensesByMonth(
                            transactions
                    )
            );

        } catch (IOException | IllegalArgumentException e) {
            System.err.println(
                    "Не вдалося обробити файл: " + e.getMessage()
            );
        }
    }
}