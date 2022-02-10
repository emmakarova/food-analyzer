package bg.sofia.uni.fmi.mjt.food.analyzer.exceptions;

public class FoodNotFoundException extends FoodDataCentralClientException{
    public FoodNotFoundException(String message) {
        super(message);
    }
}
