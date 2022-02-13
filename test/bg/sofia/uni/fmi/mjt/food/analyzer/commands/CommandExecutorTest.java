package bg.sofia.uni.fmi.mjt.food.analyzer.commands;

import bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.FoodData;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodDataCentralClientException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodDataStorageException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.InvalidArgumentsException;
import bg.sofia.uni.fmi.mjt.food.analyzer.fdc.FoodDataCentralClient;
import bg.sofia.uni.fmi.mjt.food.analyzer.storage.Storage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class CommandExecutorTest {
    private static final String UNKNOWN_COMMAND = "unknown";
    private static final String HElP_INFO = """
            Commands supported:
                > get-food <food_name>
                    - returns information about the given food by food name (returns the first page from the result).
                > get-food <page-number> <food-name>
                    - return information about the given food by name (returns the given page-number from the result).
                > get-food-report <food_fdcId>
                    - returns the name of the product, gtinUpc, calories, protein, fats, carbohydrates and fibers.
                > get-food-by-barcode --code=<gtinUpc_code>|--img=<barcode_image_file>
                    - returns information about the product searched by barcode or barcode image (if present in the storage of the server).
                > help
                    - displays information about the supported commands.
                > quit
                    - exits the application.
                    """.replace("\n","\r\n");

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
    public void testHelpCommand() throws FoodDataCentralClientException, FoodDataStorageException, InvalidArgumentsException {
        CommandExecutor cmdExecutor = new CommandExecutor();
        Command cmd = new Command("help", new ArrayList<>());

        assertEquals(HElP_INFO, cmdExecutor.execute(cmd), "Executor should return the help info.");
    }
}
