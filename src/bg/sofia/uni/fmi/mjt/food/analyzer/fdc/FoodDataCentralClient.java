package bg.sofia.uni.fmi.mjt.food.analyzer.fdc;

import bg.sofia.uni.fmi.mjt.food.analyzer.dto.ApiResponseMetadata;
import bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.FoodData;
import bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.FoodReport;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodDataCentralClientException;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class FoodDataCentralClient {
    private static final String DEFAULT_API_KEY = "aXw5cNdqOAYEPzfme2YKKCxQCPQu4qgcGTUn0bBZ";
    private static final String API_SCHEME = "https";
    private static final String API_HOST = "api.nal.usda.gov";

    private static final String PATH_TO_RESOURCE_GET_FOOD = "/fdc/v1/foods/search";
    private static final String PATH_TO_RESOURCE_GET_FOOD_REPORT = "/fdc/v1/food";

    private static final Gson GSON = new Gson();

    private final HttpClient foodDataCentralClient;
    private final String apiKey;

    public FoodDataCentralClient(HttpClient foodDataCentralClient) {
        this(foodDataCentralClient,DEFAULT_API_KEY);
    }

    public FoodDataCentralClient(HttpClient foodDataCentralClient, String apiKey) {
        this.foodDataCentralClient = foodDataCentralClient;
        this.apiKey = apiKey;
    }

    public List<FoodData> getFoodInfo(String queryParameters) throws FoodDataCentralClientException {
        HttpResponse<String> response = null;

        List<FoodData> result = new ArrayList<>();

        try {
//            System.out.println(API_SCHEME+API_HOST+pathToResourceInHost+queryParameters+"&requireAllWords=true&api_key=" + API_KEY);
            URI uri = new URI(API_SCHEME,API_HOST,PATH_TO_RESOURCE_GET_FOOD,queryParameters + "&requireAllWords=true&api_key=" + apiKey + "&pageSize=10",null);
            System.out.println(uri.toString());
            HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
            System.out.println("HERE");
            response = foodDataCentralClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("HERE2");
        }
        catch (IOException | URISyntaxException | InterruptedException e) {
            throw new FoodDataCentralClientException();
        }

        if(response.statusCode() == HttpURLConnection.HTTP_OK) {
//            System.out.println(response.body());
            ApiResponseMetadata r = GSON.fromJson(response.body(),ApiResponseMetadata.class);
            return r.getFoods();
        }

        return new ArrayList<>();
    }

    public FoodReport getFoodReport(String fdcId) throws FoodDataCentralClientException {
        HttpResponse<String> response = null;

        try {
            URI uri = new URI(API_SCHEME,API_HOST,PATH_TO_RESOURCE_GET_FOOD_REPORT +"/"+ fdcId,"api_key=" + apiKey,null);
            System.out.println(uri.toString());
            HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
            System.out.println("HERE");
            response = foodDataCentralClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new FoodDataCentralClientException();
        }

        if(response.statusCode() == HttpURLConnection.HTTP_OK) {
            return GSON.fromJson(response.body(),FoodReport.class);
        }

        return null;
    }

}
