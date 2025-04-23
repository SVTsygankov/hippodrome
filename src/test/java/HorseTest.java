import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HorseTest {

    private static final String DUMMYNAME = "dummy";
    private static final double DUMMYDOUBLE = 2.9;
    private static final double NEGATIVEVALUE = -1.0;
    private static final double ZERO = 0.0;
    private static final double MIN = 0.2;
    private static final double MAX = 0.9;

    @Test
    void throwExceptionIfNameIsNull() {
        var exception = assertThrows(IllegalArgumentException.class,
                () -> new Horse(null, DUMMYDOUBLE));
        assertEquals(exception.getMessage(), "Name cannot be null.");
//        assertThat(exception.getMessage()).isEqualTo("Name cannot be null."); //С использованием assertj
    }

    @ParameterizedTest
    @ValueSource(strings = {
            ""," ", "\t", "\n", "\r", "\f", "\u000B", "\u2000", "\u2001", "\u2002",
            "\u2003", "\u2004", "\u2005", "\u2006", "\u2008", "\u2009", "\u200A",
            "\u2028", "\u2029", "\u205F", "\u3000"})
    void throwExceptionIfNameIsEmpty1 (String name) {
        var exception = assertThrows(IllegalArgumentException.class,
                () -> new Horse(name, DUMMYDOUBLE));
        assertEquals(exception.getMessage(), "Name cannot be blank.");
    }


    @ParameterizedTest
    @MethodSource("getArgumentForNameEmptyTest")
    void throwExceptionIfNameIsEmpty(String name) {
        var exception = assertThrows(IllegalArgumentException.class,
                () -> new Horse(name, DUMMYDOUBLE));
        assertEquals(exception.getMessage(), "Name cannot be blank.");
    }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("getArgumentForSpeedOrDistanceIsNegativeTest")
    void throwExceptionIfSpeedOrDistanceIsNegative( double speed, double distance, String message) {
        var exception = assertThrows(IllegalArgumentException.class,
                () -> new Horse("dummy", speed, distance));
        assertEquals(exception.getMessage(), message);
    }

    @Test
    void checkGetNameMethod() {
        var horse = new Horse(DUMMYNAME, DUMMYDOUBLE);
        assertEquals(horse.getName(), DUMMYNAME);
    }

    @Test
    void checkGetSpeedMethod() {
        var horse = new Horse(DUMMYNAME, DUMMYDOUBLE);
        assertEquals(horse.getSpeed(), DUMMYDOUBLE);
    }

    @Test
    void checkGetDistanceMethod () {
        var horse1 = new Horse(DUMMYNAME, DUMMYDOUBLE, DUMMYDOUBLE);
        var horse2 = new Horse(DUMMYNAME, DUMMYDOUBLE);
        assertEquals(horse1.getDistance(), DUMMYDOUBLE);
        assertEquals(horse2.getDistance(), ZERO);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8})
    void checkMethodMove (double randomValue) {
        var horse = new Horse(DUMMYNAME, DUMMYDOUBLE);
        try (MockedStatic<Horse> mockedStatic = Mockito.mockStatic(Horse.class)) {
            mockedStatic.when(() -> Horse.getRandomDouble(MIN, MAX)).thenReturn(randomValue);
            horse.move();
            // Проверяем, что getRandomDouble был вызван один раз с параметрами 0.2 и 0.9
            mockedStatic.verify(() -> Horse.getRandomDouble(MIN, MAX),
                    Mockito.times(1));

            double expectedDistance = ZERO + DUMMYDOUBLE * randomValue; // Формула: distance + speed * getRandomDouble(0.2, 0.9)
            assertEquals(expectedDistance, horse.getDistance(), 0.0001);
        }
    }

    static Stream<Arguments> getArgumentForSpeedOrDistanceIsNegativeTest() {
        return Stream.of(
                Arguments.of(NEGATIVEVALUE, DUMMYDOUBLE, "Speed cannot be negative."),
                       Arguments.of(DUMMYDOUBLE, NEGATIVEVALUE, "Distance cannot be negative."));
    }

    static Stream<Arguments> getArgumentForNameEmptyTest() {
        String[] whitespaceChars = {
                " ", "\t", "\n", "\r", "\f", "\u000B", "\u2000", "\u2001", "\u2002",
                "\u2003", "\u2004", "\u2005", "\u2006", "\u2008", "\u2009", "\u200A",
                "\u2028", "\u2029", "\u205F", "\u3000"
        };
//     "\u00A0"   "\u2007"  "\u202F"
        return Stream.concat(
                Stream.of(Arguments.of("")), // Пустая строка
                Arrays.stream(whitespaceChars).map(Arguments::of));
    }
}