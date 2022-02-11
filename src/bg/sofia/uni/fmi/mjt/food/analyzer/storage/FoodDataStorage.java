package bg.sofia.uni.fmi.mjt.food.analyzer.storage;

import bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.FoodReport;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodDataStorageException;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class FoodDataStorage implements Storage {
    private static final int DEFAULT_STORAGE_CAPACITY = 100;
    private static final int COUNTER_INDEX = 0;
    private static final int FDCID_INDEX = 1;

    private Map<Integer, FoodReport> foodReportsInStorage;
    private Map<Integer, Integer> fdcIdUses;
    private String storageFile;
    private int storageCapacity;

    public FoodDataStorage(String storageFile) {
        this.storageFile = storageFile;
        this.storageCapacity = DEFAULT_STORAGE_CAPACITY;
        this.foodReportsInStorage = new HashMap<>();
        this.fdcIdUses = new HashMap<>();
    }

    public Map<Integer, FoodReport> getFoodReportsInStorage() {
        return foodReportsInStorage;
    }

    public void setStorageCapacity(int storageCapacity) {
        this.storageCapacity = storageCapacity;
    }

    @Override
    public void load() {
        try (var storageReader = Files.newBufferedReader(Path.of(storageFile))) {
            String line;

            while ((line = storageReader.readLine()) != null) {
                String[] tokens = line.split(";");
                int currentCounter = Integer.parseInt(tokens[COUNTER_INDEX]);
                int currentFdcId = Integer.parseInt((tokens[FDCID_INDEX]));

                FoodReport currentFoodReport = FoodReport.of(line);

                fdcIdUses.put(currentFdcId, currentCounter);
                foodReportsInStorage.put(currentFdcId, currentFoodReport);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Error when trying to load the storage file.", e);
        }
    }

    private int getEvictionKey() {
        int minCount = Integer.MAX_VALUE;
        int fdcIdToBeRemoved = 0;

        for (int fdcId : fdcIdUses.keySet()) {
            int currentUsage = fdcIdUses.get(fdcId);
            if (currentUsage < minCount) {
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
        try (var storageFileWriter = Files.newBufferedWriter(Path.of(storageFile), StandardOpenOption.TRUNCATE_EXISTING)) {
            for (int fdcId : foodReportsInStorage.keySet()) {
                storageFileWriter.write(fdcIdUses.get(fdcId) + ";" + foodReportsInStorage.get(fdcId).toCSV());
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Error when saving storage info in the file.", e);
        }
    }

    @Override
    public void add(FoodReport foodReport) {
        if (foodReportsInStorage.size() >= storageCapacity) {
            evictFoodReportFromStorage();
        }

        foodReportsInStorage.put(foodReport.getFdcId(), foodReport);
        fdcIdUses.put(foodReport.getFdcId(), 1);

        saveInfoInStorageFile();
    }

    @Override
    public FoodReport getFoodReport(int fdcId) {
        if (foodReportsInStorage.containsKey(fdcId)) {
            fdcIdUses.put(fdcId, fdcIdUses.get(fdcId) + 1);
            saveInfoInStorageFile();

            return foodReportsInStorage.get(fdcId);
        }

        return null;
    }

    @Override
    public FoodReport getFoodReportByBarcode(String gtinUpc) throws FoodDataStorageException {
        for (int fdcId : foodReportsInStorage.keySet()) {
            if (foodReportsInStorage.get(fdcId).getGtinUpc().equals(gtinUpc)) {
                return getFoodReport(fdcId);
            }
        }

        throw new FoodDataStorageException("No such food exists with gtinUpc = " + gtinUpc);
    }
}
