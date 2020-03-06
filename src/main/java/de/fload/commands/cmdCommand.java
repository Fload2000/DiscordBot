package de.fload.commands;

import de.fload.core.commandHandler;
import de.fload.database.DatabaseAction;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static de.fload.database.DatabaseAction.isAllowed;
import static de.fload.util.FUNCTION.*;

public class cmdCommand implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (!isAllowed(event, "command")) {
            printWarning(event, "You are not allowed to use that command");
            return;
        }

        if (args.length == 0) {
            printError(event, "No flags set!");
            return;
        }

        switch (args[0]) {
            case "add": {
                if (args.length < 3) {
                    printError(event, "Not enough flags set!");
                    break;
                }
                String name = args[1];
                StringBuilder builder = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    builder.append(args[i]);
                    if (i + 1 < args.length) {
                        builder.append(" ");
                    }
                }
                if (commandHandler.commands.containsKey(name) || DatabaseAction.getCommands(event.getGuild()).containsValue(name)) {
                    printError(event, "Name + `" + name + "` not available!");
                    break;
                }
                DatabaseAction.addCommand(event.getGuild(), name, builder.toString());
                printOk(event, "Command `" + name + "` added successfully!");
                break;
            }
            case "remove": {
                if (args.length < 2) {
                    printError(event, "Not enough flags set!");
                    break;
                }
                String name = args[1];
                if (DatabaseAction.getCommands(event.getGuild()).containsValue(name)) {
                    DatabaseAction.deleteCommand(event.getGuild(), name);
                    printOk(event, "Command `" + args[1] + "` removed successfully!");
                } else {
                    printError(event, "Command invalid!");
                }
                break;
            }
            case "list": {
                var cmds = DatabaseAction.getCommands(event.getGuild());
                var builder = new StringBuilder();
                builder.append("```\n");
                for (var cmd : cmds.keySet()) {
                    builder.append(cmd);
                    builder.append("\n");
                }
                builder.append("```");
                event.getTextChannel().sendMessage(builder.toString()).queue();
                break;
            }
            default: {
                printError(event, "Used command flags are wrong!");
                break;
            }
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        cmdMsg(event, "command");
    }

    @Override
    public String help() {
        return "Adding/Removing custom commands - Flags: add; remove; list";
    }
}
