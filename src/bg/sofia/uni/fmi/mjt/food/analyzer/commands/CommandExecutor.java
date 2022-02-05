package bg.sofia.uni.fmi.mjt.food.analyzer.commands;

import bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.FoodData;
import bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.FoodReport;
import bg.sofia.uni.fmi.mjt.food.analyzer.fdc.FoodDataCentralClient;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CommandExecutor {
    private static final String GET_FOOD = "get-food";
    private static final String GET_FOOD_REPORT = "get-food-report";
    private static final String HELP = "help";

    private static final String HELP_FILE_PATH = "src/bg/sofia/uni/fmi/mjt/food/analyzer/resources/help.txt";

    private FoodDataCentralClient fdcClient;

    private String getFoodByName(List<String> foodName) {
        StringBuilder s = new StringBuilder();
        s.append("query=");
        for(String str : foodName) {
            s.append(str).append(" ");
        }

        int stringBuilderSize = s.length();
//        s.delete(stringBuilderSize-3,stringBuilderSize);
        s.deleteCharAt(stringBuilderSize-1);
        System.out.println(s.toString());

        fdcClient = new FoodDataCentralClient(HttpClient.newHttpClient());

        List<FoodData> foodInfo = fdcClient.getFoodInfo(s.toString());

        StringBuilder result = new StringBuilder();
        for(FoodData f : foodInfo) {
            result.append(f.toString()).append(System.lineSeparator());
        }

        return result.toString();
    }

    private String getFoodReport(List<String> arguments) {
        if(arguments.size() > 1) {
            System.out.println("Exception");
        }

        String fdcId = arguments.get(0);
        fdcClient = new FoodDataCentralClient(HttpClient.newHttpClient());

        FoodReport fr = fdcClient.getFoodReport(fdcId);
        return fr.toString();

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
            case GET_FOOD_REPORT -> getFoodReport(command.arguments());
            case HELP -> getHelp();
            default -> "nothing";
        };
    }

}
