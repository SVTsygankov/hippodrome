import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

public class MainTest {

    @Test
    @Disabled("очень длинный. Запускать вручную при необходимости")
    void checkMainFunctionalityPerformance() {
        System.out.println(Thread.currentThread().getName());
        assertTimeout(Duration.ofSeconds(22), () -> {
            System.out.println(Thread.currentThread().getName());
            Main.main(new String[]{});
        });
    }

// Проверяем, что метод main выполняется не более 22 секунд
//        assertTimeoutPreemptively(Duration.ofSeconds(22), () -> {
//                  Main.main(new String[]{});
//          });
}