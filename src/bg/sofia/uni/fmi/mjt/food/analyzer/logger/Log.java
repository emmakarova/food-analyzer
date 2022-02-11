package bg.sofia.uni.fmi.mjt.food.analyzer.logger;

import java.time.LocalDateTime;

public record Log(LocalDateTime timestamp, String message, String stacktrace) {
    @Override
    public String toString() {
        return String.format("%s | %s | %s\n", timestamp, message, stacktrace);
    }
}
