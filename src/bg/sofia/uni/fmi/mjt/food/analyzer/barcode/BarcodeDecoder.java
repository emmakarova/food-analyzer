package bg.sofia.uni.fmi.mjt.food.analyzer.barcode;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.oned.UPCAReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BarcodeDecoder {
    private String imagePath;

    public BarcodeDecoder(String imagePath) {
        this.imagePath = imagePath;
    }

    public String decode() throws IOException, NotFoundException, FormatException {
        File file = new File(imagePath);
        BinaryBitmap bitmap;
        Result result;

        BufferedImage image = ImageIO.read(file);

        int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        RGBLuminanceSource source = new RGBLuminanceSource(image.getWidth(), image.getHeight(), pixels);

        bitmap = new BinaryBitmap(new HybridBinarizer(source));

        UPCAReader reader = new UPCAReader();

        result = reader.decode(bitmap);

        return result.getText();
    }
}
