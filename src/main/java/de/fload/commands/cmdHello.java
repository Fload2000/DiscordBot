package de.fload.commands;

import de.fload.util.FUNCTION;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static de.fload.database.DatabaseAction.isAllowed;
import static de.fload.util.FUNCTION.printWarning;

public class cmdHello implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if(!isAllowed(event, "hello")) {
            printWarning(event, "You are not allowed to use that command");
            return;
        }

        event.getTextChannel().sendMessage("Hello  <@" + event.getMessage().getAuthor().getId() + "> :wave:").queue();
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        FUNCTION.cmdMsg(event, "hallo");
    }

    @Override
    public String help() {
        return "Greets the user who calls the command";
    }
}
