package bg.sofia.uni.fmi.mjt.food.analyzer.storage;

import bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.FoodReport;
import bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.nutrients.LabelNutrients;
import bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.nutrients.Nutrient;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodDataStorageException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FoodDataStorageTest {
    private static final String TEST_STORAGE_FILE = "test/bg/sofia/uni/fmi/mjt/food/analyzer/storage/storage.csv";
    private FoodReport fr1 = new FoodReport(111111, "0845211", "FR1", "Ingredients", new LabelNutrients(new Nutrient(0.0)));
    private FoodReport fr2 = new FoodReport(222222, "0897779", "FR2", "Ingredients", new LabelNutrients(new Nutrient(0.0)));
    private FoodReport fr3 = new FoodReport(333333, "0853153", "FR3", "Ingredients", new LabelNutrients(new Nutrient(0.0)));

    @BeforeEach
    public void createStorageFile() throws IOException {
        Files.createFile(Path.of(TEST_STORAGE_FILE));
    }

    @AfterEach
    public void deleteStorageFile() throws IOException {
        Files.delete(Path.of(TEST_STORAGE_FILE));
    }

    @Test
    public void testLoadEmptyStorageFile() {
        FoodDataStorage storage = new FoodDataStorage(TEST_STORAGE_FILE);
        storage.load();
        assertTrue(storage.getFoodReportsInStorage().isEmpty(),
                "Storage should be empty, because there is nothing in the file.");
    }

    private void writeFoodReportToStorageFile(int fdcIdUses, FoodReport fr, String storageFile) {
        try (var storageFileWriter = Files.newBufferedWriter(Path.of(storageFile), StandardOpenOption.APPEND)) {
            storageFileWriter.write(fdcIdUses + ";" + fr.toCSV());
        } catch (IOException e) {
            System.out.println("Storage file writer exception");
        }
    }

    private List<String> readFromStorageFile(String storageFile) {
        List<String> result;

        try (var storageReader = Files.newBufferedReader(Path.of(storageFile))) {
            result = storageReader.lines().toList();
        } catch (IOException e) {
            throw new UncheckedIOException("Error while reading from storage file.", e);
        }

        return result;
    }

    @Test
    public void testLoadNonEmptyStorageFile() {
        FoodDataStorage storage = new FoodDataStorage(TEST_STORAGE_FILE);
        writeFoodReportToStorageFile(1, fr1, TEST_STORAGE_FILE);

        storage.load();

        assertEquals(1, storage.getFoodReportsInStorage().size(),
                "Size of storage should be only 1, because ony 1 food report is in the file.");
        assertEquals("Ingredients", storage.getFoodReportsInStorage().get(111111).getIngredients(),
                "The food report in the storage should be the one with fdcId = 111111");
    }

    @Test
    public void testAddFoodReportStorageHasSpace() {
        FoodDataStorage storage = new FoodDataStorage(TEST_STORAGE_FILE);
        writeFoodReportToStorageFile(1, fr1, TEST_STORAGE_FILE);

        storage.load();
        storage.add(fr2);

        List<String> linesInStorageFile = readFromStorageFile(TEST_STORAGE_FILE);

        assertEquals(2, linesInStorageFile.size(),
                "The number of food reports in storage should be increased by 1.");
        assertEquals("1;111111;0845211;FR1;Ingredients;0.0;0.0;0.0;0.0;0.0;", linesInStorageFile.get(0),
                "The food report which was is storage should be in storage after the addition of fr2.");
        assertEquals("1;222222;0897779;FR2;Ingredients;0.0;0.0;0.0;0.0;0.0;", linesInStorageFile.get(1),
                "The newly added food report should be in storage.");
    }

    @Test
    public void testAddFoodReportStorageOutOfSpace() {
        FoodDataStorage storage = new FoodDataStorage(TEST_STORAGE_FILE);
        storage.setStorageCapacity(2);

        writeFoodReportToStorageFile(2, fr1, TEST_STORAGE_FILE);
        writeFoodReportToStorageFile(1, fr2, TEST_STORAGE_FILE);

        storage.load();
        storage.add(fr3);
        List<String> linesInStorageFile = readFromStorageFile(TEST_STORAGE_FILE);

        assertEquals(2, storage.getFoodReportsInStorage().size(),
                "Storage should be with maximum of 2 food reports.");
        assertEquals("1;333333;0853153;FR3;Ingredients;0.0;0.0;0.0;0.0;0.0;", linesInStorageFile.get(0),
                "The newly added food report should be in storage.");
        assertEquals("2;111111;0845211;FR1;Ingredients;0.0;0.0;0.0;0.0;0.0;", linesInStorageFile.get(1),
                "The remaining food report is the one with more fdcId uses.");


    }

    @Test
    public void testGetFoodReportNotExisting() {
        FoodDataStorage storage = new FoodDataStorage(TEST_STORAGE_FILE);
        writeFoodReportToStorageFile(1, fr1, TEST_STORAGE_FILE);

        storage.load();

        FoodReport result = storage.getFoodReport(12345);
        assertNull(result, "When the storage doesn't contain this food null should be returned.");
    }

    @Test
    public void testGetExistingFoodReport() {
        FoodDataStorage storage = new FoodDataStorage(TEST_STORAGE_FILE);
        writeFoodReportToStorageFile(1, fr1, TEST_STORAGE_FILE);

        storage.load();

        FoodReport result = storage.getFoodReport(111111);
        assertEquals(fr1.getFdcId(), result.getFdcId(), "Result should have the same fdcId as in the storage.");
        assertEquals(fr1.getIngredients(), result.getIngredients(), "Result should have the same ingredients as in the storage.");
    }

    @Test
    public void testGetFoodReportByBarcodeNonExisting() {
        FoodDataStorage storage = new FoodDataStorage(TEST_STORAGE_FILE);
        writeFoodReportToStorageFile(1, fr1, TEST_STORAGE_FILE);

        storage.load();

        assertThrows(FoodDataStorageException.class, () -> storage.getFoodReportByBarcode("010101"),
                "Should throw exception when there is no food with this gtinUpc.");
    }

    @Test
    public void testGetFoodByBarcodeExisting() throws FoodDataStorageException {
        FoodDataStorage storage = new FoodDataStorage(TEST_STORAGE_FILE);
        writeFoodReportToStorageFile(1, fr1, TEST_STORAGE_FILE);

        storage.load();

        assertEquals(111111, storage.getFoodReportByBarcode("0845211").getFdcId(),
                "The storage should contain food report with gtinUpc = 0845211");
    }
}
