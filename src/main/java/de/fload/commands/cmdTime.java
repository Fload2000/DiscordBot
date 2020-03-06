package de.fload.commands;

import de.fload.util.TIME;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static de.fload.database.DatabaseAction.isAllowed;
import static de.fload.util.FUNCTION.cmdMsg;
import static de.fload.util.FUNCTION.printWarning;

public class cmdTime implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (!isAllowed(event, "time")) {
            printWarning(event, "You are not allowed to use that command");
            return;
        }

        event.getTextChannel().sendMessage("It's " + TIME.getTime() + ".").queue();
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        cmdMsg(event, "time");
    }

    @Override
    public String help() {
        return "Outputs the current time";
    }
}
