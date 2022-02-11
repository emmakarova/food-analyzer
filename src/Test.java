import bg.sofia.uni.fmi.mjt.food.analyzer.logger.FoodAnalyzerLogger;
import bg.sofia.uni.fmi.mjt.food.analyzer.logger.Logger;

import java.time.LocalDateTime;

public class Test {
    public static void main(String[] args) {
        Logger l = new FoodAnalyzerLogger();
        l.log(LocalDateTime.now(),"HI","stacktrace");
    }
}
