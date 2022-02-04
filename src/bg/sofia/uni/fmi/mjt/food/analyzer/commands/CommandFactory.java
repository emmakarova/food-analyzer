package bg.sofia.uni.fmi.mjt.food.analyzer.commands;

import java.util.ArrayList;
import java.util.List;

public class CommandFactory {
    private static List<String> splitClientInput(String clientInput) {
        String[] arguments = clientInput.split(" ");

        // maybe ignore some words

        return new ArrayList<>(List.of(arguments));

    }

    public static Command newCommand(String clientInput) {
        List<String> words = CommandFactory.splitClientInput(clientInput);

        return new Command(words.get(0),words.subList(1,words.size()));
    }
}
