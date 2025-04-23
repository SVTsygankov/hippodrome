import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class HippodromeTest {

    @ParameterizedTest
    @MethodSource("getArgumentForConstructorTest")
    void constructorTest(List<Horse> horses, String message) {
        var exception = assertThrows(IllegalArgumentException.class,
                () -> new Hippodrome(horses));
        assertEquals(message, exception.getMessage());
    }

    @Test
    void checkGetHorsesReturnsSameList() {
        // Создаем список из 30 разных лошадей с помощью Stream API
        List<Horse> horses = IntStream.rangeClosed(1, 30)
                .mapToObj(i -> new Horse("Horse" + i, i * 0.9)) // Разные имена и скорости
                .collect(Collectors.toList());
        var hippodrome = new Hippodrome(horses);
        assertEquals(horses, hippodrome.getHorses());
    }

    @Test
    void checkMoveCallsMoveOnAllHorses() {
        // Создаем список из 50 моков Horse с помощью Stream API
        List<Horse> horses = IntStream.range(0, 50)
                .mapToObj(i -> mock(Horse.class)) // Создаем мок для каждой лошади
                .collect(Collectors.toList());
        // Создаем объект Hippodrome, передавая список моков
        Hippodrome hippodrome = new Hippodrome(horses);
        // Вызываем метод move у объекта Hippodrome
        hippodrome.move();
        // Проверяем, что метод move был вызван у каждой мокнутой лошади
        horses.forEach(horse -> verify(horse, times(1)).move());
    }

    @Test
    void checkGetWinner() {
        var horse1 = new Horse("Name1", 2.9, 4.0);
        var horse2 = new Horse("Name2", 2.9, 5.0);
        var horse3 = new Horse("Name3", 2.9, 3.0);
        var horse4 = new Horse("Name4", 2.9, 3.9999999);
        var hippodrome = new Hippodrome((List.of(horse1, horse2, horse3, horse4)));

        assertSame(horse2, hippodrome.getWinner());
    }

    static Stream<Arguments> getArgumentForConstructorTest () {
        return Stream.of(
                Arguments.of(null, "Horses cannot be null."),
                Arguments.of(Collections.emptyList(), "Horses cannot be empty."));
    }
}
