package bg.sofia.uni.fmi.mjt.food.analyzer.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class FoodAnalyzerClient {
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 1024;

    private static ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

    public void start() {
        try (SocketChannel socketChannel = SocketChannel.open();
             Scanner scanner = new Scanner(System.in)) {

            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));

            System.out.println("Connected to the server.");

            while (true) {
                System.out.print("> ");
                String message = scanner.nextLine();

                if (message.equals("quit")) {
                    System.out.println("Disconnecting from the server and exiting...");
                    break;
                }

                System.out.println("Waiting for server to respond...");

                buffer.clear();
                buffer.put(message.getBytes());
                buffer.flip();
                socketChannel.write(buffer);

                buffer.clear();
                socketChannel.read(buffer);
                buffer.flip();

                byte[] byteArray = new byte[buffer.remaining()];
                buffer.get(byteArray);
                String reply = new String(byteArray, StandardCharsets.UTF_8);

                System.out.println(reply);
            }

        } catch (IOException e) {
            System.out.println("Sorry, can't connect to the server, try again later.");
        }
    }

    public static void main(String[] args) {
        FoodAnalyzerClient c = new FoodAnalyzerClient();
        c.start();
    }
}
