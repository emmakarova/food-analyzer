package bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.nutrients;

public class LabelNutrients {
    private Nutrient fat;
    private Nutrient carbohydrates;
    private Nutrient fiber;
    private Nutrient protein;
    private Nutrient calories;

    public LabelNutrients(Nutrient noInformation) {
        this.fat = noInformation;
        this.carbohydrates = noInformation;
        this.fiber = noInformation;
        this.protein = noInformation;
        this.calories = noInformation;

    }

    public LabelNutrients(Nutrient fat, Nutrient carbohydrates, Nutrient fiber, Nutrient protein, Nutrient calories) {
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.fiber = fiber;
        this.protein = protein;
        this.calories = calories;
    }

    public Nutrient getFat() {
        return fat;
    }

    public Nutrient getCarbohydrates() {
        return carbohydrates;
    }

    public Nutrient getFiber() {
        return fiber;
    }

    public Nutrient getProtein() {
        return protein;
    }

    public Nutrient getCalories() {
        return calories;
    }

    @Override
    public String toString() {
        return String.format("\t\t- fat: %s\n\t\t- carbohydrates: %s\n\t\t- fiber: %s\n\t\t- protein: %s\n\t\t- calories: %s\n",
                fat, carbohydrates, fiber, protein, calories);
    }
}
