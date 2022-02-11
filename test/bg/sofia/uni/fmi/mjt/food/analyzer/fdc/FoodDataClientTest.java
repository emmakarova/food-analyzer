package bg.sofia.uni.fmi.mjt.food.analyzer.fdc;

import bg.sofia.uni.fmi.mjt.food.analyzer.dto.ApiResponseMetadata;
import bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.FoodData;
import bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.FoodReport;
import bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.nutrients.LabelNutrients;
import bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.nutrients.Nutrient;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.BadRequestException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodDataCentralClientException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodNotFoundException;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FoodDataClientTest {
    private static final String NOT_EXISTING_FOOD = "not exists";
    private static FoodData f1;
    private static FoodData f2;
    private static ApiResponseMetadata fdcResponseMetadata;
    private static String fdcResponseMetadataJson;

    private static FoodReport fdcFoodReport;
    private static String fdcFoodReportJson;

    private FoodDataCentralClient foodDataCentralClient;

    @Mock
    private HttpClient httpClientMock;

    @Mock
    private HttpResponse<String> fdcResponseMock;

    private static void setResponseMetadata() {
        f1 = new FoodData("Food 1", 111111);
        f2 = new FoodData("Food 2", 222222);
        List<FoodData> foods = new ArrayList<>(List.of(f1, f2));
        fdcResponseMetadata = new ApiResponseMetadata(2, foods);
        fdcResponseMetadataJson = new Gson().toJson(fdcResponseMetadata);
    }

    private static void setFoodReportResponse() {
        LabelNutrients nutrients = new LabelNutrients(new Nutrient(0.0));
        fdcFoodReport = new FoodReport(111111, "0101010", "Food Report 1", "Ingredients 1", nutrients);
        fdcFoodReportJson = new Gson().toJson(fdcFoodReport);
    }

    @BeforeAll
    public static void setUpClass() {
        setResponseMetadata();
        setFoodReportResponse();
    }

    @BeforeEach
    public void createFoodDataCentralClient() throws IOException, InterruptedException {
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(fdcResponseMock);
        foodDataCentralClient = new FoodDataCentralClient(httpClientMock);
    }

    @Test
    public void testGetFoodInfoFoodNotFound() {
        when(fdcResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_NOT_FOUND);

        assertThrows(FoodNotFoundException.class, () -> foodDataCentralClient.getFoodInfo(1, NOT_EXISTING_FOOD),
                "Should throw food not found exception when api response is 404.");
    }

    @Test
    public void testGetFoodInfoBadRequest() {
        when(fdcResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);

        assertThrows(BadRequestException.class, () -> foodDataCentralClient.getFoodInfo(1, "food"),
                "Should throw bad request exception when api response is 400.");
    }

    @Test
    public void testGetFoodInfoUnknownError() {
        when(fdcResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_INTERNAL_ERROR);

        assertThrows(FoodDataCentralClientException.class, () -> foodDataCentralClient.getFoodInfo(1, "food"),
                "Should throw food data central exception when api response is not expected.");
    }

    @Test
    public void testGetFoodInfo() throws FoodDataCentralClientException {
        when(fdcResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);

        when(fdcResponseMock.body()).thenReturn(fdcResponseMetadataJson);

        List<FoodData> result = foodDataCentralClient.getFoodInfo(1, "Food");
        assertEquals(2, result.size(), "Size of result should be 2.");
        assertEquals(f1.toString(), result.get(0).toString(), "First food data should be f1.");
        assertEquals(f2.toString(), result.get(1).toString(), "Second food data should be f2.");
    }


    @Test
    public void testGetFoodReportFoodNotFound() {
        when(fdcResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_NOT_FOUND);

        assertThrows(FoodNotFoundException.class, () -> foodDataCentralClient.getFoodReport(11111),
                "Should throw food not found exception when api response is 404.");
    }

    @Test
    public void testGetFoodReportBadRequest() {
        when(fdcResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);

        assertThrows(BadRequestException.class, () -> foodDataCentralClient.getFoodReport(111111),
                "Should throw bad request exception when api response is 400.");
    }

    @Test
    public void testGetFoodReportUnknownError() {
        when(fdcResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_INTERNAL_ERROR);

        assertThrows(FoodDataCentralClientException.class, () -> foodDataCentralClient.getFoodReport(111111),
                "Should throw food data central exception when api response is not expected.");
    }

    @Test
    public void testGetFoodReport() throws FoodDataCentralClientException {
        when(fdcResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);

        when(fdcResponseMock.body()).thenReturn(fdcFoodReportJson);

        FoodReport result = foodDataCentralClient.getFoodReport(111111);

        assertEquals(fdcFoodReport.toString(), result.toString(),
                "Result from the request to api should be the same as fdcReport");
    }

}
