package bg.sofia.uni.fmi.mjt.food.analyzer.dto.food;

public class FoodData {
    //*
    private String description;
    private int fdcId;

    // not required
    private String gtinUpc;

    public FoodData(String description, int fdcId, String gtinUpc) {
        this.description = description;
        this.fdcId = fdcId;
        this.gtinUpc = gtinUpc;
    }

    @Override
    public String toString() {
        return "FoodData{" +
                "description='" + description + '\'' +
                ", fdcId=" + fdcId +
                ", gtinUpc=" + gtinUpc +
                '}';
    }
}
