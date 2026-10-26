package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public abstract class TransactionCSVReader {

     public static List<String> readLines(Path filePath) throws IOException {
        return Files.readAllLines(filePath, StandardCharsets.UTF_8);
    }

     public static List<String> readResource(String resourceName) throws IOException {
        InputStream stream =
                TransactionCSVReader.class.getResourceAsStream(resourceName);

        if (stream == null) {
            throw new IOException("Не знайдено файл: " + resourceName);
        }

        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8))) {

            String line;

            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        return lines;
    }
}