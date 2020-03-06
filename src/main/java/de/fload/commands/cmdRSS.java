package de.fload.commands;

import de.fload.database.DatabaseAction;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Map;

import static de.fload.database.DatabaseAction.isAllowed;
import static de.fload.util.FUNCTION.*;

public class cmdRSS implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (!isAllowed(event, "rss")) {
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
                String link = args[2];

                if (DatabaseAction.isInRSS(event.getGuild(), name)) {
                    printError(event, "Link `" + link + "` is already in the list!");
                    break;
                }
                DatabaseAction.insertRSS(event.getGuild(), name, link);
                printOk(event, "`" + link + "` added successfully to the RSS-List");
                break;
            }
            case "remove": {
                if (args.length < 2) {
                    printError(event, "Not enough flags set!");
                    break;
                }
                String name = args[1];

                if (!DatabaseAction.isInRSS(event.getGuild(), name)) {
                    printError(event, "Link is not in the list!");
                    break;
                }
                DatabaseAction.deleteRSS(event.getGuild(), name);
                printOk(event, "Removed successfully from the RSS-List");
                break;
            }
            case "print": {
                if (args.length < 2) {
                    printError(event, "Not enough flags set!");
                    break;
                }

                StringBuilder builder = new StringBuilder();
                builder.append("```");
                Map<String, String> hm = DatabaseAction.getRss(event.getGuild());
                for (int i = 0; i < hm.values().size(); i++) {
                    builder.append(hm.keySet().toArray()[i]).append(" - ").append(hm.values().toArray()[i]);
                    if (i + 1 < hm.values().size()) {
                        builder.append("\n");
                    }
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
        cmdMsg(event, "rss");
    }

    @Override
    public String help() {
        return "Settings command for RSS - Flags: add; remove; print";
    }
}
