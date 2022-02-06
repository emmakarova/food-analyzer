package bg.sofia.uni.fmi.mjt.food.analyzer.dto.food;

import bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.nutrients.LabelNutrients;
import bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.nutrients.Nutrient;
import com.google.gson.annotations.SerializedName;

public class FoodReport {
    private int fdcId;
    @SerializedName("description")
    private String name;
    private String ingredients;
    private LabelNutrients labelNutrients;
//fdcId;name;ingredients;fat;carbohydrates;fiber;protein;calories;
    private static final int FDCID = 1;
    private static final int NAME = 2;
    private static final int INGREDIENTS = 3;
    private static final int FAT = 4;
    private static final int CARBOHYDRATES = 5;
    private static final int FIBER = 6;
    private static final int PROTEIN = 7;
    private static final int CALORIES = 8;


    public FoodReport(int fdcId,String name, String ingredients, LabelNutrients labelNutrients) {
        this.fdcId = fdcId;
        this.name = name;
        this.ingredients = ingredients;
        this.labelNutrients = labelNutrients;
    }

    public static FoodReport of(String line) {
        String[] tokens = line.split(";");

        int fdcId = Integer.parseInt(tokens[FDCID]);
        String name = tokens[NAME];
        String ingredients = tokens[INGREDIENTS];

        Nutrient fat = new Nutrient(Double.parseDouble(tokens[FAT]));
        Nutrient carbohydrates = new Nutrient(Double.parseDouble(tokens[CARBOHYDRATES]));
        Nutrient fiber = new Nutrient(Double.parseDouble(tokens[FIBER]));
        Nutrient protein = new Nutrient(Double.parseDouble(tokens[PROTEIN]));
        Nutrient calories = new Nutrient(Double.parseDouble(tokens[CALORIES]));

        LabelNutrients labelNutrients = new LabelNutrients(fat,carbohydrates,fiber,protein,calories);

        return new FoodReport(fdcId,name,ingredients,labelNutrients);
    }

    @Override
    public String toString() {
        return String.format("Food report:\n\tFdcId: %d\n\tName: %s\n\tIngredients: %s\n\tNutrients:\n%s",fdcId,name,ingredients,labelNutrients);
    }
}
