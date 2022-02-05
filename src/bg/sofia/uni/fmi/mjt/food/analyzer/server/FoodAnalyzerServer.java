package bg.sofia.uni.fmi.mjt.food.analyzer.server;

import bg.sofia.uni.fmi.mjt.food.analyzer.commands.CommandExecutor;
import bg.sofia.uni.fmi.mjt.food.analyzer.commands.CommandFactory;
import bg.sofia.uni.fmi.mjt.food.analyzer.storage.Storage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class FoodAnalyzerServer {
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 1024;

    private static ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
    private static CommandExecutor cmdExecutor = new CommandExecutor();

    private Storage storage;


    //add constructor

    public static void main(String[] args) {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            serverSocketChannel.configureBlocking(false);

            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                System.out.println("START");
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

//                        String clientInput = "help";
                        // mock for now
                        String clientInput = getClientInput(sc);
                        System.out.println("*"+clientInput+"*");
                        if(clientInput == null) {
                            continue;
                        }

                        clientInput = clientInput.replace(System.lineSeparator(),"");

                     String res = cmdExecutor.execute(CommandFactory.newCommand(clientInput));
                     // res will be the final result from either:
                        // -> storage
                        // -> api request
                        // -> help/unknown command

                        System.out.println(res);

                        writeClientOutput(sc,res);
                        // mock for now
//                        writeClientOutput(sc, clientInput);

                    }
                    else if (key.isAcceptable()) {
                        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
                        SocketChannel accept = sockChannel.accept();
                        accept.configureBlocking(false);
                        accept.register(selector, SelectionKey.OP_READ);

                    }

                    keyIterator.remove();

                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("There is a problem with the server socket", e);
        }
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
        System.out.println(output);
        System.out.println("WRITE " + buffer);
    }
}
