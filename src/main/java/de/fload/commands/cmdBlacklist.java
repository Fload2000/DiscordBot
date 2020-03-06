package de.fload.commands;

import de.fload.database.DatabaseAction;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static de.fload.database.DatabaseAction.isAllowed;
import static de.fload.util.FUNCTION.*;

public class cmdBlacklist implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (!isAllowed(event, "blacklist")) {
            printWarning(event, "You are not allowed to use that command");
            return;
        }

        if (args.length == 0) {
            printError(event, "No flags set!");
            return;
        } else if (args.length < 2) {
            printError(event, "Not enough flags set!");
            return;
        }

        var builder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            builder.append(args[i]);
            if (i + 1 < args.length) {
                builder.append(" ");
            }
        }
        var exp = builder.toString();

        switch (args[0]) {
            case "add": {
                addToBlacklist(event, exp);
                break;
            }
            case "remove": {
                removeFromBlacklist(event, exp);
                break;
            }
            case "print": {
                var expressions = new StringBuilder();

                expressions.append("```\n");
                for (var s : DatabaseAction.getBlacklistForGuild(event.getGuild())) {
                    expressions.append(s).append("\n");
                }
                expressions.append("\n```");

                event.getTextChannel().sendMessage(expressions.toString()).queue();
                break;
            }
            default: {
                printError(event, "Used command flags are wrong!");
                break;
            }
        }
    }

    private void removeFromBlacklist(MessageReceivedEvent event, String exp) {
        if (!DatabaseAction.isOnBlacklist(event.getGuild(), exp)) {
            printError(event, "Expression `" + exp + "` is not in the blacklist!");
        } else {
            DatabaseAction.deleteFromBlacklist(event.getGuild(), exp);
            printOk(event, "`" + exp + "` removed successfully from the blacklist");
        }
    }

    private void addToBlacklist(MessageReceivedEvent event, String exp) {
        if (DatabaseAction.isOnBlacklist(event.getGuild(), exp)) {
            printError(event, "Expression `" + exp + "` is already in the blacklist!");
        } else {
            DatabaseAction.addToBlacklist(event.getGuild(), exp);
            printOk(event, "`" + exp + "` added successfully to the blacklist");
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        cmdMsg(event, "blacklist");
    }

    @Override
    public String help() {
        return "Adding/Removing blacklist phrases - Flags: add; remove; print";
    }
}
