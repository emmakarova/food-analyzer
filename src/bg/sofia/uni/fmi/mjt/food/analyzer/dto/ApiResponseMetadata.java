package bg.sofia.uni.fmi.mjt.food.analyzer.dto;

import bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.FoodData;

import java.util.List;

public class ApiResponseMetadata {
    int totalHits;
    List<FoodData> foods;

    public ApiResponseMetadata(int totalHits, List<FoodData> foods) {
        this.totalHits = totalHits;
        this.foods = foods;
    }

    public int getTotalHits() {
        return totalHits;
    }

    public List<FoodData> getFoods() {
        return foods;
    }

    @Override
    public String toString() {
        return String.format("Total hits: %d\nFood information: %s", totalHits, foods);
    }
}
