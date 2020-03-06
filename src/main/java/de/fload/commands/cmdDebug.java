package de.fload.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static de.fload.database.DatabaseAction.isAllowed;
import static de.fload.util.FUNCTION.cmdMsg;
import static de.fload.util.FUNCTION.printWarning;

public class cmdDebug implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (!isAllowed(event, "debug")) {
            printWarning(event, "You are not allowed to use that command");
            return;
        }

        if (args.length == 0) {
            return;
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            builder.append(args[i]);
            if (i + 1 < args.length) {
                builder.append(" ");
            }
        }
        event.getTextChannel().sendMessage(builder.toString()).queue();
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        cmdMsg(event, "debug");
    }

    @Override
    public String help() {
        return "DEBUG";
    }
}
