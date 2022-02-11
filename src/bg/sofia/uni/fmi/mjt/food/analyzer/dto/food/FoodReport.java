package bg.sofia.uni.fmi.mjt.food.analyzer.dto.food;

import bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.nutrients.LabelNutrients;
import bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.nutrients.Nutrient;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

public class FoodReport {
    private int fdcId;
    private String gtinUpc;
    @SerializedName("description")
    private String name;
    private String ingredients;
    private LabelNutrients labelNutrients;

    private static final int FDCID = 1;
    private static final int GTINUPC = 2;
    private static final int NAME = 3;
    private static final int INGREDIENTS = 4;
    private static final int FAT = 5;
    private static final int CARBOHYDRATES = 6;
    private static final int FIBER = 7;
    private static final int PROTEIN = 8;
    private static final int CALORIES = 9;


    public FoodReport(int fdcId,String gtinUpc,String name, String ingredients, LabelNutrients labelNutrients) {
        this.fdcId = fdcId;
        this.gtinUpc = gtinUpc;
        this.name = name;
        this.ingredients = ingredients;
        this.labelNutrients = labelNutrients;
    }

    public int getFdcId() {
        return fdcId;
    }

    public String getGtinUpc() {
        return gtinUpc == null ? "No gtinUpc" : gtinUpc;
    }

    public String getIngredients() {
        return ingredients == null ? "No information" : ingredients;
    }

    public LabelNutrients getLabelNutrients() {
        return labelNutrients == null ? new LabelNutrients(new Nutrient(0.0)) : labelNutrients;
    }

    public static FoodReport of(String line) {
        String[] tokens = line.split(";");

        int fdcId = Integer.parseInt(tokens[FDCID]);
        String gtinUpc = tokens[GTINUPC];
        String name = tokens[NAME];
        String ingredients = tokens[INGREDIENTS];

        Nutrient fat = new Nutrient(Double.parseDouble(tokens[FAT]));
        Nutrient carbohydrates = new Nutrient(Double.parseDouble(tokens[CARBOHYDRATES]));
        Nutrient fiber = new Nutrient(Double.parseDouble(tokens[FIBER]));
        Nutrient protein = new Nutrient(Double.parseDouble(tokens[PROTEIN]));
        Nutrient calories = new Nutrient(Double.parseDouble(tokens[CALORIES]));

        LabelNutrients labelNutrients = new LabelNutrients(fat,carbohydrates,fiber,protein,calories);

        return new FoodReport(fdcId,gtinUpc,name,ingredients,labelNutrients);
    }

    @Override
    public String toString() {
        return String.format("Food report:\n\tFdcId: %d\n\tGtinUpc: %s\n\tName: %s\n\tIngredients: %s\n\tNutrients:\n%s",fdcId,getGtinUpc(),name,getIngredients(),getLabelNutrients());
    }

    public String toCSV() {
        System.out.println(toString());
        LabelNutrients currentLabelNutrients = getLabelNutrients();

        return String.format(Locale.US,"%d;%s;%s;%s;%.1f;%.1f;%.1f;%.1f;%.1f;\n",
                fdcId,getGtinUpc(),name,getIngredients(),currentLabelNutrients.getFat().getValue(),
                currentLabelNutrients.getCarbohydrates().getValue(),
                currentLabelNutrients.getFiber().getValue(),currentLabelNutrients.getProtein().getValue(),
                currentLabelNutrients.getCalories().getValue());
    }
}
