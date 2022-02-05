package bg.sofia.uni.fmi.mjt.food.analyzer.dto.food;

public class FoodReport {
    private String name;
    private Ingredients ingredients;

    public FoodReport(String name, Ingredients ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "FoodReport{" +
                "name='" + name + '\'' +
                ", ingredients=" + ingredients +
                '}';
    }
}
