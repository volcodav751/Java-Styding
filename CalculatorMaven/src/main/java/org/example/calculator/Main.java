package org.example.calculator;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Calculator calculator = new Calculator();

        try {
            System.out.print("Введіть перше число: ");
            double a = parseNumber(scanner.nextLine());

            System.out.print("Введіть друге число: ");
            double b = parseNumber(scanner.nextLine());

            System.out.print("Введіть операцію (+, -, *, /): ");
            String operation = scanner.nextLine().trim();

            double result;
            switch (operation) {
                case "+" -> result = calculator.add(a, b);
                case "-" -> result = calculator.subtract(a, b);
                case "*" -> result = calculator.multiply(a, b);
                case "/" -> result = calculator.divide(a, b);
                default -> throw new InvalidInputException("Невідома операція.");
            }

            System.out.println("Результат: " + result);
        } catch (ArithmeticException e) {
            System.out.println("Помилка: " + e.getMessage());
        } catch (InvalidInputException e) {
            System.out.println("Помилка введення: " + e.getMessage());
        } finally {
            System.out.println("Обробку запиту завершено.");
        }
    }

    public static double parseNumber(String text) throws InvalidInputException {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Введіть число, а не текст.");
        }
    }
}
