package bg.sofia.uni.fmi.mjt.food.analyzer.server;

import bg.sofia.uni.fmi.mjt.food.analyzer.commands.CommandExecutor;
import bg.sofia.uni.fmi.mjt.food.analyzer.commands.CommandFactory;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodDataCentralClientException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodDataStorageException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.InvalidArgumentsException;
import bg.sofia.uni.fmi.mjt.food.analyzer.logger.FoodAnalyzerLogger;
import bg.sofia.uni.fmi.mjt.food.analyzer.logger.Logger;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

public class FoodAnalyzerServer {
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 1024;

    private static ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
    private static CommandExecutor cmdExecutor = new CommandExecutor();
    private Selector selector;

    private Logger foodAnalyzerLogger = new FoodAnalyzerLogger();
    private boolean serverIsWorking;

    public void start() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            serverSocketChannel.configureBlocking(false);

            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            serverIsWorking = true;

            while (serverIsWorking) {
                try {
                    int readyChannels = selector.select();
                    if (readyChannels == 0) {
                        continue;
                    }

                    Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();

                        if (key.isReadable()) {
                            SocketChannel sc = (SocketChannel) key.channel();

                            String clientInput = getClientInput(sc);
                            System.out.println("*" + clientInput + "*");

                            if (clientInput == null) {
                                continue;
                            }

                            clientInput = clientInput.replace(System.lineSeparator(), "");

                            String res = null;
                            try {
                                res = cmdExecutor.execute(CommandFactory.newCommand(clientInput));
                            } catch (InvalidArgumentsException | FoodDataStorageException | FoodDataCentralClientException e) {
                                foodAnalyzerLogger.log(LocalDateTime.now(), e.getMessage(), Arrays.toString(e.getStackTrace()));
                                writeClientOutput(sc, e.getMessage());
                            }

                            if (res != null) {
                                writeClientOutput(sc, res);
                            }

                        } else if (key.isAcceptable()) {
                            ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
                            SocketChannel accept = sockChannel.accept();
                            accept.configureBlocking(false);
                            accept.register(selector, SelectionKey.OP_READ);

                        }

                        keyIterator.remove();
                    }
                }
                catch (IOException e) {
                    System.out.println("Error when processing the client request: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("There is a problem with the server socket", e);
        }
    }

    public static void main(String[] args) {
        FoodAnalyzerServer s = new FoodAnalyzerServer();
        s.start();
    }

    private static String getClientInput(SocketChannel clientChannel) throws IOException {
        buffer.clear();

        int readBytes = clientChannel.read(buffer);
        if (readBytes < 0) {
            clientChannel.close();
            return null;
        }

        buffer.flip();

        byte[] clientInputBytes = new byte[buffer.remaining()];
        buffer.get(clientInputBytes);

        return new String(clientInputBytes,StandardCharsets.UTF_8);
    }

    private static void writeClientOutput(SocketChannel clientChannel,
                                          String output) throws IOException {
        buffer.clear();
        buffer.put(output.getBytes());
        buffer.flip();
        clientChannel.write(buffer);
    }
}
