package bg.sofia.uni.fmi.mjt.food.analyzer.commands;

import bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.FoodData;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodDataCentralClientException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodDataStorageException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.InvalidArgumentsException;
import bg.sofia.uni.fmi.mjt.food.analyzer.fdc.FoodDataCentralClient;
import bg.sofia.uni.fmi.mjt.food.analyzer.storage.Storage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommandExecutorTest {
    private static final String UNKNOWN_COMMAND = "unknown";
    private static final String HElP_INFO = """
            Commands supported:
                > get-food <food_name>
                    - returns information about the given food by food name.
                > get-food-report <food_fdcId>
                    - returns the name of the product, its calories,protein, fats,carbohydrates and fibers.
                > get-food-by-barcode --code=<gtinUpc_code>|--img=<barcode_image_file>
                    - returns information about the product searched by barcode or barcode image.
                > help
                    - displays information about the supported commands.
                > quit
                    - exits the application.
                    """;

    private static List<FoodData> foodInfo;

    @Mock
    private Storage foodDataStorageMock;

    @Mock
    private FoodDataCentralClient fdcClientMock;

    @BeforeAll
    public static void setUpClass() {
        FoodData f1 = new FoodData("Food 1", 111111);
        foodInfo = new ArrayList<>(List.of(f1));
    }

    @Test
    public void testExecuteUnknownCommand() {
        CommandExecutor cmdExecutor = new CommandExecutor();
        Command cmd = new Command(UNKNOWN_COMMAND, new ArrayList<>());

        assertThrows(InvalidArgumentsException.class, () -> cmdExecutor.execute(cmd),
                "Should throw illegal arguments exception when the comamnd is not supported by the server.");
    }

    @Test
    @Disabled // fix line separators
    public void testHelpCommand() throws FoodDataCentralClientException, FoodDataStorageException, InvalidArgumentsException {
        CommandExecutor cmdExecutor = new CommandExecutor();
        Command cmd = new Command("help", new ArrayList<>());

        assertEquals(HElP_INFO, cmdExecutor.execute(cmd), "Executor should return the help info.");
    }

    @Test
    @Disabled // fix mocking
    public void testGetFoodCommand() throws FoodDataCentralClientException, FoodDataStorageException, InvalidArgumentsException {
        when(fdcClientMock.getFoodInfo(Mockito.any(Integer.class), Mockito.any(String.class))).thenReturn(foodInfo);

        CommandExecutor cmdExecutor = new CommandExecutor(fdcClientMock, foodDataStorageMock);
        Command cmd = new Command("get-food", List.of("oreo"));

        cmdExecutor.execute(cmd);
    }
}
