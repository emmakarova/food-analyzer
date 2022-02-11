package bg.sofia.uni.fmi.mjt.food.analyzer.logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FoodAnalyzerLoggerTest {
    private static final String TEST_LOG_DIRECTORY = "test/bg/sofia/uni/fmi/mjt/food/analyzer/logs";
    private static final String LOG_FILE_NAME = "%s/logs-%d.txt";

    @BeforeEach
    public void createLogDirectory() throws IOException {
        Files.createDirectory(Path.of(TEST_LOG_DIRECTORY));
    }

    @AfterEach
    public void deleteLogDirectory() throws IOException {
        Path dir = Path.of(TEST_LOG_DIRECTORY);

        DirectoryStream<Path> stream = Files.newDirectoryStream(dir);

        for (Path fileOrSubDir : stream) {
            Files.delete(fileOrSubDir);
        }

        Files.delete(Path.of(TEST_LOG_DIRECTORY));
    }


    @Test
    public void testLogWithNullTimestamp() {
        FoodAnalyzerLogger logger = new FoodAnalyzerLogger(TEST_LOG_DIRECTORY);
        assertThrows(IllegalArgumentException.class, () -> logger.log(null, "message", "stacktrace"),
                "Should throw exception when time is null");
    }

    @Test
    public void testLogWithNullMessage() {
        FoodAnalyzerLogger logger = new FoodAnalyzerLogger(TEST_LOG_DIRECTORY);
        assertThrows(IllegalArgumentException.class, () -> logger.log(LocalDateTime.now(), null, "stacktrace"),
                "Should throw exception when message is null.");
    }

    @Test
    public void testLogWithNullStacktrace() {
        FoodAnalyzerLogger logger = new FoodAnalyzerLogger(TEST_LOG_DIRECTORY);
        assertThrows(IllegalArgumentException.class, () -> logger.log(LocalDateTime.now(), "message", null),
                "Should throw exception when stacktrace is null.");
    }

    @Test
    public void testLogWithEmptyStacktrace() {
        FoodAnalyzerLogger logger = new FoodAnalyzerLogger(TEST_LOG_DIRECTORY);
        assertThrows(IllegalArgumentException.class, () -> logger.log(null, "message", ""),
                "Should throw exception when stacktrace is empty.");
    }

    @Test
    public void testFirstLogFileCreated() {
        FoodAnalyzerLogger logger = new FoodAnalyzerLogger(TEST_LOG_DIRECTORY);
        LocalDateTime currentTime = LocalDateTime.now();
        logger.log(currentTime, "message", "stacktrace");

        try (var logReader = Files.newBufferedReader(Path.of(String.format(LOG_FILE_NAME, TEST_LOG_DIRECTORY, 0)))) {
            String line = logReader.readLine();
            assertEquals(currentTime + " | message | stacktrace", line, "Log file should contain only one line.");
        } catch (IOException e) {
            throw new UncheckedIOException("Error while reading from log file.", e);
        }
    }

    @Test
    public void testLogsInNewLogFiles() {
        FoodAnalyzerLogger logger = new FoodAnalyzerLogger(TEST_LOG_DIRECTORY);
        logger.setCurrentSizeOfLogFile(120);

        logger.log(LocalDateTime.now(), "message 1", "Stacktrace 1");
        logger.log(LocalDateTime.now(), "message 2", "Stacktrace 2");
        logger.log(LocalDateTime.now(), "message 3", "Stacktrace 3");
        logger.log(LocalDateTime.now(), "message 4", "Stacktrace 4");

        assertTrue(Files.exists(Path.of(String.format(LOG_FILE_NAME, TEST_LOG_DIRECTORY, 0))),
                "First log file should contain the first 2 logs.");
        assertTrue(Files.exists(Path.of(String.format(LOG_FILE_NAME, TEST_LOG_DIRECTORY, 1))),
                "Second file should contain the last 2 logs.");
        assertFalse(Files.exists(Path.of(String.format(LOG_FILE_NAME, TEST_LOG_DIRECTORY, 2))),
                "Third lod file shouldn't exist.");
    }
}
