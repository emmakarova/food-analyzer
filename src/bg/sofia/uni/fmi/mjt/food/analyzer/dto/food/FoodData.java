package bg.sofia.uni.fmi.mjt.food.analyzer.dto.food;

public class FoodData {
    private String description;
    private int fdcId;

    public FoodData(String description, int fdcId) {
        this.description = description;
        this.fdcId = fdcId;
    }

    @Override
    public String toString() {
        return String.format("\n\tDescription: %s\n\tFdcId: %d\n", description, fdcId);
    }
}
