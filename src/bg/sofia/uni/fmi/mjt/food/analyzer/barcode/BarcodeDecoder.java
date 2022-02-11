package bg.sofia.uni.fmi.mjt.food.analyzer.barcode;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class BarcodeDecoder {
    private String imagePath;

    public BarcodeDecoder(String imagePath) {
        this.imagePath = imagePath;
    }

    public String decode() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();

        Path bFile = Path.of(imagePath);
        Map<Object, Object> data = new LinkedHashMap<>();
        data.put("file", bFile);

        String boundary = new BigInteger(256, new Random()).toString();
        var request = HttpRequest.newBuilder()
                .header("Content-type", "multipart/form-data; boundary=" + boundary)
                .POST(ofMimeMultipartData(data, boundary))
                .uri(URI.create("https://zxing.org/w/decode"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String b = response.body();

        return "barcode";
    }

    public HttpRequest.BodyPublisher ofMimeMultipartData(Map<Object, Object> data,
                                                         String boundary) throws IOException {
        var byteArrays = new ArrayList<byte[]>();
        byte[] separator = ("--" + boundary + System.lineSeparator() + "Content-Disposition: form-data; name=")
                .getBytes(StandardCharsets.UTF_8);
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            byteArrays.add(separator);

            if (entry.getValue() instanceof Path) {
                var path = (Path) entry.getValue();
                String mimeType = Files.probeContentType(path);
                byteArrays.add(("\"" + entry.getKey() + "\"; file=\"" + path.getFileName()
                        + "\"" + System.lineSeparator() + "Content-Type: " + mimeType + System.lineSeparator() +
                        System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
                byteArrays.add(Files.readAllBytes(path));
                byteArrays.add(System.lineSeparator().getBytes(StandardCharsets.UTF_8));
            } else {
                byteArrays.add(("\"" + entry.getKey() + "\"" + System.lineSeparator() + System.lineSeparator() +
                        entry.getValue() + System.lineSeparator())
                        .getBytes(StandardCharsets.UTF_8));
            }
        }
        byteArrays.add(("--" + boundary + "--").getBytes(StandardCharsets.UTF_8));
        return HttpRequest.BodyPublishers.ofByteArrays(byteArrays);
    }
}
