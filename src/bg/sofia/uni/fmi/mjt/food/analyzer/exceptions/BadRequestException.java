package bg.sofia.uni.fmi.mjt.food.analyzer.exceptions;

public class BadRequestException extends FoodDataCentralClientException {
    public BadRequestException(String message) {
        super(message);
    }
}
