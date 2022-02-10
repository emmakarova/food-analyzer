package bg.sofia.uni.fmi.mjt.food.analyzer.storage;

import bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.FoodReport;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodDataStorageException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodNotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

// least frequently used type of cache
public class FoodDataStorage implements Storage {
    private static final String STORAGE_FILE = "src/bg/sofia/uni/fmi/mjt/food/analyzer/resources/storage.csv";
    private static final int STORAGE_CAPACITY = 100;
    private static final int COUNTER_INDEX = 0;
    private static final int FDCID_INDEX = 1;

    private Map<Integer, FoodReport> foodReportsInStorage; // fdcId -> food report
    private Map<Integer, Integer> fdcIdUses; // 489568 -> 5

    public FoodDataStorage() {
        foodReportsInStorage = new HashMap<>();
        fdcIdUses = new HashMap<>();
    }

    @Override
    public void load() {
        try (var storageReader = Files.newBufferedReader(Path.of(STORAGE_FILE))) {
            String line;

            while ((line = storageReader.readLine()) != null) {
                String[] tokens = line.split(";");
                int currentCounter = Integer.parseInt(tokens[COUNTER_INDEX]);
                int currentFdcId = Integer.parseInt((tokens[FDCID_INDEX]));
                FoodReport currentFoodReport = FoodReport.of(line);

                fdcIdUses.put(currentFdcId,currentCounter);
                foodReportsInStorage.put(currentFdcId, currentFoodReport);
            }
            System.out.println(fdcIdUses);
            System.out.println(foodReportsInStorage);

        } catch (IOException e) {
            System.out.println("File exception here");
        }
    }

    private int getEvictionKey() {
        int minCount = Integer.MAX_VALUE;
        int fdcIdToBeRemoved = 0;

        for(int fdcId : fdcIdUses.keySet()) {
            int currentUsage = fdcIdUses.get(fdcId);
            if(currentUsage < minCount) {
                minCount = currentUsage;
                fdcIdToBeRemoved = fdcId;
            }
        }

        return fdcIdToBeRemoved;
    }

    private void evictFoodReportFromStorage() {
        int fdcIdToBeRemoved = getEvictionKey();
        foodReportsInStorage.remove(fdcIdToBeRemoved);
        fdcIdUses.remove(fdcIdToBeRemoved);
    }

    private void saveInfoInStorageFile() {
        try(var storageFileWriter = Files.newBufferedWriter(Path.of(STORAGE_FILE), StandardOpenOption.TRUNCATE_EXISTING)) {
            for(int fdcId : foodReportsInStorage.keySet()) {
                storageFileWriter.write(fdcIdUses.get(fdcId) + ";" + foodReportsInStorage.get(fdcId).toCSV());
            }
        } catch (IOException e) {
            System.out.println("Storage file writer exception");
        }
    }

    @Override
    public void add(FoodReport foodReport) {
        System.out.println("ADD");
        if(foodReportsInStorage.size() >= STORAGE_CAPACITY) {
            System.out.println("HAS TO EVICT");
            evictFoodReportFromStorage();
        }
        foodReportsInStorage.put(foodReport.getFdcId(),foodReport);
        fdcIdUses.put(foodReport.getFdcId(),1);
        System.out.println("SAVE TIME");
        saveInfoInStorageFile();
    }

    @Override
    public FoodReport getFoodReport(int fdcId) {
        if(foodReportsInStorage.containsKey(fdcId)) {
            fdcIdUses.put(fdcId,fdcIdUses.get(fdcId) + 1);
            saveInfoInStorageFile();

            return foodReportsInStorage.get(fdcId);
        }

        return null;
    }

    @Override
    public FoodReport getFoodReportByBarcode(String gtinUpc) throws FoodDataStorageException {
        for(int fdcId : foodReportsInStorage.keySet()) {
            if(foodReportsInStorage.get(fdcId).getGtinUpc().equals(gtinUpc)) {
                System.out.println("YESS");
                return getFoodReport(fdcId);
            }
        }

        throw new FoodDataStorageException("No such food exists with gtinUpc = " + gtinUpc);
    }
}
