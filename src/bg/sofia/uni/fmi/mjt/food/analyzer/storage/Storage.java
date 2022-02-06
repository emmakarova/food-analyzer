package bg.sofia.uni.fmi.mjt.food.analyzer.storage;

import bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.FoodReport;

public interface Storage {
    // TODO: add params
    void load();

    void add(FoodReport foodReport);

    FoodReport getFoodReport(int fdcId);
}
