package bg.sofia.uni.fmi.mjt.food.analyzer.storage;

import bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.FoodReport;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodDataStorageException;

public interface Storage {
    // TODO: add params
    void load();

    void add(FoodReport foodReport);

    FoodReport getFoodReport(int fdcId);

    FoodReport getFoodReportByBarcode(String gtinUpc) throws FoodDataStorageException;
}
