package bg.sofia.uni.fmi.mjt.food.analyzer.commands;

import bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.FoodData;
import bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.FoodReport;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodDataCentralClientException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodDataStorageException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.InvalidArgumentsException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.InvalidNumberOfArgumentsException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.NotMatchingArgumentsFormatException;
import bg.sofia.uni.fmi.mjt.food.analyzer.fdc.FoodDataCentralClient;
import bg.sofia.uni.fmi.mjt.food.analyzer.storage.FoodDataStorage;
import bg.sofia.uni.fmi.mjt.food.analyzer.storage.Storage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CommandExecutor {
    private static final String GET_FOOD = "get-food";
    private static final String GET_FOOD_REPORT = "get-food-report";
    private static final String GET_FOOD_BY_BARCODE = "get-food-by-barcode";
    private static final String HELP = "help";

    private static final String HELP_FILE_PATH = "src/bg/sofia/uni/fmi/mjt/food/analyzer/resources/help.txt";
    private static final String STORAGE_FILE = "src/bg/sofia/uni/fmi/mjt/food/analyzer/resources/storage.csv";
    private static final String CODE_ARGUMENT_REGEX = "--code=[0-9]+";
    private static final String IMAGE_ARGUMENT_REGEX = "--img=";
    private static final String CODE_ARGUMENT_PREFIX = "--code";
    private static final int BARCODE_CODE_START_INDEX = 7;
    private static final int BARCODE_IMG_START_INDEX = 6;

    private FoodDataCentralClient fdcClient;
    private Storage foodDataStorage;

    public CommandExecutor() {
        foodDataStorage = new FoodDataStorage(STORAGE_FILE);
    }

    public CommandExecutor(FoodDataCentralClient fdcClient, Storage fdStorage) {
        this.fdcClient = fdcClient;
        this.foodDataStorage = fdStorage;
    }

    private void validateNumberOfArguments(List<String> arguments) throws InvalidNumberOfArgumentsException {
        int numberOfArguments = arguments.size();
        if (numberOfArguments == 0) {
            throw new InvalidNumberOfArgumentsException("Expected at least one argument, type 'help' for more information.");
        }
    }

    private void validateBarcodeArguments(List<String> arguments) throws InvalidArgumentsException {
        validateNumberOfArguments(arguments);

        int startIndex = 0;
        int endIndex = 6;

        for (String s : arguments) {
            if (s.length() < endIndex || !(s.matches(CODE_ARGUMENT_REGEX) ||
                    s.substring(startIndex, endIndex).matches(IMAGE_ARGUMENT_REGEX))) {
                throw new NotMatchingArgumentsFormatException("Command arguments did not match the expected format, type 'help' for more information.");
            }
        }
    }

    private String processRequestToApi(int pageNumber, String queryParameters) throws FoodDataCentralClientException {
        fdcClient = new FoodDataCentralClient(HttpClient.newHttpClient());

        List<FoodData> foodInfo = null;

        foodInfo = fdcClient.getFoodInfo(pageNumber, queryParameters);

        if (foodInfo.isEmpty()) {
            return "No results";
        }

        StringBuilder result = new StringBuilder();
        int foodCounter = 1;
        for (FoodData f : foodInfo) {
            result.append("Food #").append(foodCounter++).append(f.toString());
        }

        return result.toString();
    }

    private String getFoodByName(List<String> arguments) throws FoodDataCentralClientException,
            InvalidNumberOfArgumentsException {
        validateNumberOfArguments(arguments);

        int defaultPageNumber;
        List<String> foodName = new ArrayList<>(arguments);

        try {
            defaultPageNumber = Integer.parseInt(arguments.get(0));
            foodName.remove(0);
        } catch (NumberFormatException e) {
            defaultPageNumber = 1;
        }

        StringBuilder queryParameters = new StringBuilder();
        queryParameters.append("query=");
        for (String str : foodName) {
            queryParameters.append(str).append(" ");
        }

        return processRequestToApi(defaultPageNumber, queryParameters.toString());
    }

    private String getFoodReport(List<String> arguments) throws InvalidNumberOfArgumentsException,
            FoodDataCentralClientException {
        int numberOfArguments = arguments.size();
        if (numberOfArguments > 1) {
            throw new InvalidNumberOfArgumentsException("Wrong number of arguments: expected 1 but given " + numberOfArguments);
        }

        FoodReport fr = null;
        foodDataStorage.load();

        int fdcId = Integer.parseInt(arguments.get(0));
        if ((fr = foodDataStorage.getFoodReport(fdcId)) != null) {
            return fr.toString();
        }

        fdcClient = new FoodDataCentralClient(HttpClient.newHttpClient());

        fr = fdcClient.getFoodReport(fdcId);

        foodDataStorage.add(fr);

        return fr.toString();
    }

    private String getFoodByBarcode(List<String> arguments) throws InvalidArgumentsException, FoodDataStorageException {
        validateBarcodeArguments(arguments);
        foodDataStorage.load();

        for (String a : arguments) {
            if (a.startsWith(CODE_ARGUMENT_PREFIX)) {
                String gtinUpc = a.substring(BARCODE_CODE_START_INDEX);

                return foodDataStorage.getFoodReportByBarcode(gtinUpc).toString();
            }
        }

        throw new InvalidArgumentsException("Expected --code= or --img argument.");
    }

    private String getHelp() {
        StringBuilder helpInfo = new StringBuilder();

        try (var helpReader = Files.newBufferedReader(Path.of(HELP_FILE_PATH))) {
            String line;

            while ((line = helpReader.readLine()) != null) {
                helpInfo.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Error when loading the help information.", e);
        }

        return helpInfo.toString();
    }

    public String execute(Command command) throws InvalidArgumentsException, FoodDataStorageException,
            FoodDataCentralClientException {
        return switch (command.cmd()) {
            case GET_FOOD -> getFoodByName(command.arguments());
            case GET_FOOD_REPORT -> getFoodReport(command.arguments());
            case GET_FOOD_BY_BARCODE -> getFoodByBarcode(command.arguments());
            case HELP -> getHelp();
            default -> throw new InvalidArgumentsException("The command is not supported, type 'help' for more information.");
        };
    }
}
