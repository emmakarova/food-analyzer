package bg.sofia.uni.fmi.mjt.food.analyzer.fdc;

import bg.sofia.uni.fmi.mjt.food.analyzer.dto.ApiResponseMetadata;
import bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.FoodData;
import bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.FoodReport;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.BadRequestException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodDataCentralClientException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodNotFoundException;
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
            throw new FoodDataCentralClientException("An error when sending the request occurred");
        }
        int responseStatusCode = response.statusCode();

        if(responseStatusCode == HttpURLConnection.HTTP_OK) {
//            System.out.println(response.body());
            ApiResponseMetadata r = GSON.fromJson(response.body(),ApiResponseMetadata.class);
            return r.getFoods();
        }
        else {
            checkResponseStatusCode(responseStatusCode);
        }


        return new ArrayList<>();
    }

    private void checkResponseStatusCode(int statusCode) throws FoodDataCentralClientException {
        switch (statusCode) {
            case HttpURLConnection.HTTP_NOT_FOUND -> throw new FoodNotFoundException("Food with this fdcId was not found.");
            case HttpURLConnection.HTTP_BAD_REQUEST -> throw new BadRequestException("Missing or misconfigured parameter in request.");
            default -> throw new FoodDataCentralClientException("An unexpected error occurred when calling the api, status code " + statusCode);
        }
    }

    public FoodReport getFoodReport(int fdcId) throws FoodDataCentralClientException {
        HttpResponse<String> response = null;

        try {
            URI uri = new URI(API_SCHEME,API_HOST,PATH_TO_RESOURCE_GET_FOOD_REPORT +"/"+ fdcId,"api_key=" + apiKey,null);
            System.out.println(uri);
            HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
            System.out.println("HERE");
            response = foodDataCentralClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            // connection error
            throw new FoodDataCentralClientException("An error when sending the request occurred");
        }

        int responseStatusCode = response.statusCode();

        if(responseStatusCode == HttpURLConnection.HTTP_OK) {
            return GSON.fromJson(response.body(),FoodReport.class);
        }
        else {
            checkResponseStatusCode(responseStatusCode);
        }

        return null;
    }

}
