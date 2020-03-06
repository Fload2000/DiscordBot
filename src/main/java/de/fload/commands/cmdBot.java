package de.fload.commands;

import de.fload.core.DiscordBot;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static de.fload.database.DatabaseAction.isAllowed;
import static de.fload.util.FUNCTION.*;

public class cmdBot implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        // Check if user is allowed to use that command.
        if (!isAllowed(event, "bot")) {
            printWarning(event, "You are not allowed to use that command");
            return;
        }

        // Check for correct amount of flags.
        if(args.length == 0) {
            printError(event, "No Flags set!");
            return;
        } else if(args.length > 2) {
            printError(event, "Too much Flags set!");
            return;
        }

        // Perform actions according to the flags.
        switch (args[0]) {
            case "restart": {
                printInfo(event, "Bot is restarting ...");
                pause(5000);
                DiscordBot.restartBot();
                break;
            }
            case "stop": {
                printInfo(event, "Bot is stopping ...");
                pause(5000);
                DiscordBot.stopBot();
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
        cmdMsg(event, "bot");
    }

    @Override
    public String help() {
        return "Controls for the Bot - Flags: restart; stop";
    }
}
