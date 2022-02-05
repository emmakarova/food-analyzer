package bg.sofia.uni.fmi.mjt.food.analyzer.dto.food;

import bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.nutrients.LabelNutrients;
import com.google.gson.annotations.SerializedName;

public class FoodReport {
    private int fdcId;
    @SerializedName("description")
    private String name;
    private String ingredients;
    private LabelNutrients labelNutrients;

    public FoodReport(int fdcId,String name, String ingredients, LabelNutrients labelNutrients) {
        this.fdcId = fdcId;
        this.name = name;
        this.ingredients = ingredients;
        this.labelNutrients = labelNutrients;
    }

    @Override
    public String toString() {
        return String.format("Food report:\n\tFdcId: %d\n\tName: %s\n\tIngredients: %s\n\tNutrients:\n%s",fdcId,name,ingredients,labelNutrients);
    }
}
