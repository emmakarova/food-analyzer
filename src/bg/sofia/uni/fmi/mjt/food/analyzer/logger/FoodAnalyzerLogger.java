package bg.sofia.uni.fmi.mjt.food.analyzer.logger;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

public class FoodAnalyzerLogger implements Logger {
    private static final int DEFAULT_MAX_SIZE_OF_LOG_FILE = 6_000;
    private static final String LOG_FILE_NAME = "%s/logs-%d.txt";

    private int currentSizeOfLogFile;
    private int currentLogFileNumber;
    private Path currentLogFilePath;
    private String logDirectory;

    public FoodAnalyzerLogger(String logDirectory) {
        this.currentSizeOfLogFile = DEFAULT_MAX_SIZE_OF_LOG_FILE;
        this.logDirectory = logDirectory;
        int dirSize = 0;
        // ?
        if (Files.exists(Path.of(logDirectory))) {
            dirSize = new File(logDirectory).list().length;
        }

        currentLogFileNumber = dirSize == 0 ? 0 : dirSize - 1;

        this.currentLogFilePath = Path.of(String.format(LOG_FILE_NAME, logDirectory, currentLogFileNumber));
    }

    public void setCurrentSizeOfLogFile(int currentSizeOfLogFile) {
        this.currentSizeOfLogFile = currentSizeOfLogFile;
    }

    @Override
    public void log(LocalDateTime timestamp, String message, String stacktrace) {
        validateLogParameters(stacktrace, timestamp, message);

        Log currentLog = new Log(timestamp, message, stacktrace);

        try {
            if (Files.exists(currentLogFilePath) && checkFileSize(currentLog.toString().length())) {
                getNextLogFile();
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Error while trying to access log files.", e);
        }

        try (var logFileWriter = Files.newBufferedWriter(currentLogFilePath,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            logFileWriter.write(currentLog.toString());
            logFileWriter.flush();
        } catch (IOException e) {
            throw new UncheckedIOException("Error while writing in log files.", e);
        }
    }

    private void validateLogParameters(String stacktrace, Object... args) {
        if (stacktrace == null || stacktrace.isEmpty()) {
            throw new IllegalArgumentException("Stacktrace should not be empty");
        }

        for (Object a : args) {
            if (a == null) {
                throw new IllegalArgumentException("Parameters should not be null");
            }
        }
    }

    private boolean checkFileSize(int currentLogSize) throws IOException {
        return Files.size(currentLogFilePath) + currentLogSize >= currentSizeOfLogFile;
    }

    private void getNextLogFile() {
        currentLogFileNumber++;
        currentLogFilePath = Path.of(String.format(LOG_FILE_NAME, logDirectory, currentLogFileNumber));
    }
}
