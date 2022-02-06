package bg.sofia.uni.fmi.mjt.food.analyzer.storage;

import bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.FoodReport;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

// most frequently used type of cache
public class FoodDataStorage implements Storage{
    private static final String STORAGE_FILE = "src/bg/sofia/uni/fmi/mjt/food/analyzer/resources/storage.txt";
    private static final int COUNTER_INDEX = 0;

    private Map<Integer, FoodReport> foodReportsInStorage; // fdcId -> food report
    private Map<Integer, Integer> fdcIdUses; // 489568 -> 5

    public FoodDataStorage() {
        foodReportsInStorage = new TreeMap<>();
        fdcIdUses = new HashMap<>();
    }

    @Override
    public void load() {
        try(var storageReader = Files.newBufferedReader(Path.of(STORAGE_FILE))) {
            String line;
            line = storageReader.readLine();
            System.out.println(line);

            while((line = storageReader.readLine()) != null) {
                String[] tokens = line.split(";");
                int currentCounter = Integer.parseInt(tokens[COUNTER_INDEX]);
                FoodReport currentFoodReport = FoodReport.of(line);

                foodReportsInStorage.put(currentCounter,currentFoodReport);
            }

            System.out.println(foodReportsInStorage);

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(FoodReport foodReport) {

    }

    @Override
    public FoodReport getFoodReport(int fdcId) {
        return null;
    }
}
