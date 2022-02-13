package barcode;

import bg.sofia.uni.fmi.mjt.food.analyzer.barcode.BarcodeDecoder;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BarcodeDecoderTest {
    @Test
    public void testDecodeNonExistingFile() {
        BarcodeDecoder decoder = new BarcodeDecoder("file.gif");

        assertThrows(IOException.class, decoder::decode,
                "Should throw IO exception when file can't be opened with ImageIO.read().");
    }

    @Test
    public void testDecodeSucceeds() throws NotFoundException, IOException, FormatException {
        BarcodeDecoder decoder = new BarcodeDecoder("C:\\Users\\emma_\\OneDrive\\Documents\\MJT\\food-analyzer\\src\\bg\\sofia\\uni\\fmi\\mjt\\food\\analyzer\\resources\\barcode_exists.gif");

        String expectedCode = "019646001111";
        String actualCode = decoder.decode();

        assertEquals(expectedCode, actualCode, "The decoded string should be 019646001111.");
    }
}
