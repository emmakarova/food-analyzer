package bg.sofia.uni.fmi.mjt.food.analyzer.commands;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class CommandFactoryTest {
    @Test
    public void testNewCommandOneArgument() {
        Command command = CommandFactory.newCommand("get-food oreo");
        assertEquals(command.cmd(), "get-food");
        assertIterableEquals(command.arguments(), List.of("oreo"));
    }

    @Test
    public void testNewCommandZeroArguments() {
        Command command = CommandFactory.newCommand("get-food");
        assertEquals(command.cmd(), "get-food");
        assertIterableEquals(command.arguments(), new ArrayList<>());
    }

    @Test
    public void testNewCommandMoreThanOneArgument() {
        Command command = CommandFactory.newCommand("get-food oreo sandwich cookie");
        assertEquals(command.cmd(), "get-food");
        assertIterableEquals(command.arguments(), List.of("oreo", "sandwich", "cookie"));
    }
}
