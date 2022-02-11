package bg.sofia.uni.fmi.mjt.food.analyzer.commands;

import bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.FoodData;
import bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.FoodReport;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodDataCentralClientException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodDataStorageException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.InvalidArgumentsException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.InvalidNumberOfArgumentsException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.NotMatchingArgumentsFormatException;
import bg.sofia.uni.fmi.mjt.food.analyzer.fdc.FoodDataCentralClient;
import bg.sofia.uni.fmi.mjt.food.analyzer.logger.FoodAnalyzerLogger;
import bg.sofia.uni.fmi.mjt.food.analyzer.logger.Logger;
import bg.sofia.uni.fmi.mjt.food.analyzer.storage.FoodDataStorage;
import bg.sofia.uni.fmi.mjt.food.analyzer.storage.Storage;

import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CommandExecutor {
    private static final String GET_FOOD = "get-food";
    private static final String GET_FOOD_REPORT = "get-food-report";
    private static final String GET_FOOD_BY_BARCODE = "get-food-by-barcode";
    private static final String HELP = "help";

    private static final String HELP_FILE_PATH = "src/bg/sofia/uni/fmi/mjt/food/analyzer/resources/help.txt";
    private static final String UNKNOWN_COMMAND = "The command is not supported.";
    private static final String CODE_ARGUMENT_REGEX = "--code=[0-9]+";
    private static final String IMAGE_ARGUMENT_REGEX = "--img=";


    private FoodDataCentralClient fdcClient;
    private Storage foodDataStorage;
    private Logger foodAnalyzerLogger;

    public CommandExecutor() {
        foodDataStorage = new FoodDataStorage();
        foodAnalyzerLogger = new FoodAnalyzerLogger();
    }

    private String getFoodByName(List<String> foodName) throws FoodDataCentralClientException {
        StringBuilder s = new StringBuilder();
        s.append("query=");
        for(String str : foodName) {
            s.append(str).append(" ");
        }

        int stringBuilderSize = s.length();
        s.deleteCharAt(stringBuilderSize-1);
        System.out.println(s.toString());

        fdcClient = new FoodDataCentralClient(HttpClient.newHttpClient());

        List<FoodData> foodInfo = null;

        foodInfo = fdcClient.getFoodInfo(s.toString());


        StringBuilder result = new StringBuilder();
        int foodCounter = 1;
        for(FoodData f : foodInfo) {
            result.append("Food #").append(foodCounter++).append(f.toString());
        }

        return result.toString();
    }

    private String getFoodReport(List<String> arguments) throws InvalidNumberOfArgumentsException, FoodDataCentralClientException {
        int numberOfArguments = arguments.size();
        if(numberOfArguments > 1) {
            throw new InvalidNumberOfArgumentsException("Wrong number of arguments: expected 1 but given " + numberOfArguments);
        }
        System.out.println("TESTING STORAGE");
        FoodReport fr = null;
        foodDataStorage.load();
        int fdcId = Integer.parseInt(arguments.get(0));
        if((fr = foodDataStorage.getFoodReport(fdcId)) != null) {
            System.out.println("IN STORAGE");
            return fr.toString();
        }


        fdcClient = new FoodDataCentralClient(HttpClient.newHttpClient());


        fr = fdcClient.getFoodReport(fdcId);


        foodDataStorage.add(fr);

        return fr.toString();

    }

    private void validateBarcodeArguments(List<String> arguments) throws InvalidArgumentsException {
        if(arguments.size() == 0) {
            throw new InvalidNumberOfArgumentsException("Expected at least one argument, type 'help' for more information.");
        }

        int startIndex = 0;
        int endIndex = 6;

        for(String s : arguments) {
            if(s.length() < endIndex || !(s.matches(CODE_ARGUMENT_REGEX) || s.substring(startIndex,endIndex).matches(IMAGE_ARGUMENT_REGEX))) {
                throw new NotMatchingArgumentsFormatException("Command arguments did not match the expected format, type 'help' for more information.");
            }
        }

    }
//get-food-by-barcode --code=<gtinUpc_code>|--img=<barcode_image_file>
    private String getFoodByBarcode(List<String> arguments) throws InvalidArgumentsException, FoodDataStorageException {
        validateBarcodeArguments(arguments);

        for(String a : arguments) {
            if(a.startsWith("--code")) {
                String gtinUpc = a.substring(7);
                System.out.println("code = " + gtinUpc);
                return foodDataStorage.getFoodReportByBarcode(gtinUpc).toString();
            }
        }

//        String bPath = "C:\\Users\\emma_\\OneDrive\\Documents\\MJT\\food-analyzer\\src\\bg\\sofia\\uni\\fmi\\mjt\\food\\analyzer\\resources\\barcode.gif";


        // img case

        // if img => generate code from img => getInfo

        return "barcode by image :(";
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
            // change?
            throw new IllegalStateException();
        }

        return helpInfo.toString();
    }

    public String execute(Command command) throws InvalidArgumentsException, FoodDataStorageException, FoodDataCentralClientException {
        return switch (command.cmd()) {
            case GET_FOOD -> getFoodByName(command.arguments());
            case GET_FOOD_REPORT -> getFoodReport(command.arguments());
            case GET_FOOD_BY_BARCODE -> getFoodByBarcode(command.arguments());
            case HELP -> getHelp();
            default -> throw new InvalidArgumentsException(UNKNOWN_COMMAND);
        };
    }

}
