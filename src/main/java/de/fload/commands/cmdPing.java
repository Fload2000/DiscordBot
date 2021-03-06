package de.fload.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static de.fload.database.DatabaseAction.isAllowed;
import static de.fload.util.FUNCTION.cmdMsg;
import static de.fload.util.FUNCTION.printWarning;

public class cmdPing implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (!isAllowed(event, "ping")) {
            printWarning(event, "You are not allowed to use that command");
            return;
        }

        event.getTextChannel().sendMessage("Pong!").queue();
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        cmdMsg(event, "ping");
    }

    @Override
    public String help() {
        return "Pong!";
    }
}
