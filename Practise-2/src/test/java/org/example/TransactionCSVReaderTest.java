package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionCSVReaderTest {

    @TempDir
    Path directory;

    @Test
    void testReadCsv() throws IOException {
        Path file = directory.resolve("test.csv");

        String content =
                "Дата,Сума,Категорія\n"
                        + "01-12-2023,1000,Дохід\n"
                        + "02-12-2023,-250,Їжа\n";

        Files.writeString(
                file,
                content,
                StandardCharsets.UTF_8
        );

        List<String> lines =
                TransactionCSVReader.readLines(file);

        List<Transaction> data =
                TransactionCSVParser.parse(lines);

        assertEquals(2, data.size());

        assertEquals(
                new Transaction("01-12-2023", 1000, "Дохід"),
                data.get(0)
        );

        assertEquals(
                new Transaction("02-12-2023", -250, "Їжа"),
                data.get(1)
        );
    }

    @Test
    void testSlideData() throws IOException {
        List<String> lines =
                TransactionCSVReader.readResource("/transactions.csv");

        List<Transaction> data =
                TransactionCSVParser.parse(lines);

        assertEquals(13, data.size());

        assertEquals(
                7130,
                TransactionAnalyzer.calculateTotalBalance(data),
                0.001
        );

        assertEquals(
                13,
                TransactionAnalyzer.countTransactionsByMonth(
                        data, "12-2023"
                )
        );

        assertEquals(
                9,
                TransactionAnalyzer.findTopExpenses(data).size()
        );

        assertEquals(
                7870,
                TransactionAnalyzer.calculateExpensesByMonth(data)
                        .get("12-2023"),
                0.001
        );
    }

    @Test
    void testInvalidCsv() {
        assertThrows(
                IllegalArgumentException.class,
                () -> TransactionCSVParser.parse(
                        List.of("01-12-2023,wrong,Їжа")
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> TransactionCSVParser.parse(
                        List.of("31-02-2023,-100,Їжа")
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> TransactionCSVParser.parse(
                        List.of("01-12-2023,-100")
                )
        );
    }

    @Test
    void testMissingFile() {
        assertThrows(
                IOException.class,
                () -> TransactionCSVReader.readLines(
                        directory.resolve("missing.csv")
                )
        );

        assertThrows(
                IOException.class,
                () -> TransactionCSVReader.readResource(
                        "/missing.csv"
                )
        );
    }
}