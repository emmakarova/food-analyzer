package bg.sofia.uni.fmi.mjt.food.analyzer.commands;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CommandExecutor {
    private static final String GET_FOOD = "get-food";
    private static final String HELP = "help";
    private static final Gson GSON = new Gson();

    private static final String HELP_FILE_PATH = "src/bg/sofia/uni/fmi/mjt/food/analyzer/resources/help.txt";

    private String getFoodByName(List<String> foodName) {
        StringBuilder s = new StringBuilder();
        for(String str : foodName) {
            s.append(str).append("+");
        }

        return s.toString();
    }

    private String getHelp() {
        StringBuilder helpInfo = new StringBuilder();

        try(var helpReader = Files.newBufferedReader(Path.of(HELP_FILE_PATH))) {
            String line;

            while((line = helpReader.readLine()) != null) {
                helpInfo.append(line).append(System.lineSeparator());
            }
        }
        catch (IOException e) {
            System.out.println("Maybe throw exception");
        }

        return helpInfo.toString();
    }

    public String execute(Command command) {
        return switch (command.cmd()) {
            case GET_FOOD -> getFoodByName(command.arguments());
            case HELP -> getHelp();
            default -> "nothing";
        };
    }

}
