package bg.sofia.uni.fmi.mjt.food.analyzer.logger;

import java.time.LocalDateTime;

public interface Logger {
    void log(LocalDateTime timestamp, String message, String stacktrace);
}
