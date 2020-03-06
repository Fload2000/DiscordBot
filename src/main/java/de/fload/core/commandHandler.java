package de.fload.core;

import de.fload.commands.Command;
import de.fload.database.DatabaseAction;
import de.fload.util.FUNCTION;

import java.util.HashMap;
import java.util.Map;

import static de.fload.database.DatabaseAction.isAllowed;
import static de.fload.util.FUNCTION.printWarning;

public class commandHandler {
    public static final commandParser parse = new commandParser();
    public static HashMap<String, Command> commands = new HashMap<>();

    public static void handleCommand(@org.jetbrains.annotations.NotNull commandParser.commandContainer cmd) {
        Map<String, String> cmds = DatabaseAction.getCommands(cmd.event.getGuild());

        if (commands.containsKey(cmd.invoke)) {
            boolean safe = commands.get(cmd.invoke).called(cmd.args, cmd.event);

            if (!safe) {
                commands.get(cmd.invoke).action(cmd.args, cmd.event);
                commands.get(cmd.invoke).executed(safe, cmd.event);
            } else {
                commands.get(cmd.invoke).executed(safe, cmd.event);
            }
        } else {
            if (cmds.containsKey(cmd.invoke)) {
                if (!isAllowed(cmd.event, cmd.invoke)) {
                    printWarning(cmd.event, "You are not allowed to use that command");
                    return;
                }
                cmd.event.getTextChannel().sendMessage(cmds.get(cmd.invoke)).queue();
            } else {
                System.out.println("Unknown Command");
                FUNCTION.printWarning(cmd.event, "Unknown Command");
            }
        }
    }
}
