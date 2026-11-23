package org.example;

public class TestAutomaton {

    public String process(String input) {
        String state = "S";

        for (int i = 0; i < input.length(); i++) {
            char symbol = input.charAt(i);

            switch (state) {
                case "S":
                    if (symbol == 'T') {
                        state = "1";
                    }
                    break;

                case "1":
                    if (symbol == 'E') {
                        state = "2";
                    } else if (symbol == 'T') {
                        state = "1"; // Цей T може бути початком нового TEST.
                    } else {
                        state = "S";
                    }
                    break;

                case "2":
                    if (symbol == 'S') {
                        state = "3";
                    } else if (symbol == 'T') {
                        state = "1"; // Цей T може бути початком нового TEST.
                    } else {
                        state = "S";
                    }
                    break;

                case "3":
                    if (symbol == 'T') {
                        state = "F";
                    } else {
                        state = "S";
                    }
                    break;

                case "F":
                    break; // TEST вже знайдено: залишаємося у F.
            }
        }

        return state;
    }
}
