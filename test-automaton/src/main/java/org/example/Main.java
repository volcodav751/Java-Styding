package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введіть рядок для пошуку TEST:");
        String input = scanner.nextLine();

        TestAutomaton automaton = new TestAutomaton();
        System.out.println("Кінцевий стан: " + automaton.process(input));
    }
}
