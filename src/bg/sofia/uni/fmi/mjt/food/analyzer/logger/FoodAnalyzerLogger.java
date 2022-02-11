package bg.sofia.uni.fmi.mjt.food.analyzer.logger;

import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.InvalidArgumentsException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

public class FoodAnalyzerLogger implements Logger{
    private int currentLogFileNumber;
    private Path currentLogFilePath;
    private static final int MAX_SIZE_OF_LOG_FILE = 6_000;
    private static final String LOG_DIRECTORY = "src/bg/sofia/uni/fmi/mjt/food/analyzer/logs";

    public FoodAnalyzerLogger() {
        int dirSize = new File(LOG_DIRECTORY).list().length;
        currentLogFileNumber = dirSize == 0 ? 0 : dirSize - 1;
        System.out.println("LOG FILE " + currentLogFileNumber);
        this.currentLogFilePath = Path.of(LOG_DIRECTORY + "/logs-" + currentLogFileNumber + ".txt");
    }

    @Override
    public void log(LocalDateTime timestamp, String message, String stacktrace) {
        if(timestamp == null || message == null || stacktrace == null || stacktrace.isEmpty()) {
            // maybe not
            throw new IllegalArgumentException("Null args");
        }

        Log currentLog = new Log(timestamp,message,stacktrace);

        try {
            if(Files.exists(currentLogFilePath) && Files.size(currentLogFilePath) + currentLog.toString().length() >= MAX_SIZE_OF_LOG_FILE) {
                currentLogFileNumber++;
                currentLogFilePath = Path.of(LOG_DIRECTORY + "/logs-" + currentLogFileNumber + ".txt");
                System.out.println("new file path " + currentLogFilePath);
            }
        } catch (IOException e) {
            System.out.println("Exception here");
        }

        // what if the server restarts?

        try(var logFileWriter = Files.newBufferedWriter(currentLogFilePath,
                StandardOpenOption.CREATE,StandardOpenOption.APPEND)) {
            System.out.println("WRITING");
            logFileWriter.write(currentLog.toString());
            logFileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
