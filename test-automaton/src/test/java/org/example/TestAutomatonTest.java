package org.example;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestAutomatonTest {

    @ParameterizedTest(name = "[{index}] {0} -> {1}")
    @MethodSource("testCases")
    void shouldReturnFinalState(String input, String expectedState) {
        TestAutomaton automaton = new TestAutomaton();
        assertEquals(expectedState, automaton.process(input));
    }

    static Stream<Arguments> testCases() {
        return Stream.of(
                // Порожній рядок та всі можливі кінцеві стани.
                Arguments.of("", "S"),
                Arguments.of("abc", "S"),
                Arguments.of("T", "1"),
                Arguments.of("TE", "2"),
                Arguments.of("TES", "3"),
                Arguments.of("TEST", "F"),

                // Скидання часткового збігу до S.
                Arguments.of("TX", "S"),
                Arguments.of("TEX", "S"),
                Arguments.of("TESX", "S"),

                // Пошук на початку, посередині та в кінці рядка.
                Arguments.of("TESTabc", "F"),
                Arguments.of("abcTESTabc", "F"),
                Arguments.of("abcTEST", "F"),
                Arguments.of("abcTES", "3"),

                // Регістр, пробіли та розірвана послідовність.
                Arguments.of("test", "S"),
                Arguments.of("TEsT", "1"),
                Arguments.of("TE ST", "1"),
                Arguments.of("  TEST  ", "F"),

                // Пошук після невдалої спроби; стан F не скидається.
                Arguments.of("TESXTEST", "F"),
                Arguments.of("TESTTEST", "F"),
                Arguments.of("TESTEST", "F"),

                // Завдання із зірочкою: новий T у станах 1 і 2.
                Arguments.of("TT", "1"),
                Arguments.of("TET", "1"),
                Arguments.of("TTEST", "F"),
                Arguments.of("TETEST", "F")
        );
    }
}
