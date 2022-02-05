package bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.nutrients;

public class Nutrient {
    private double value;

    public Nutrient(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%.1f g",value);
    }
}
