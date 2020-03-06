package de.fload.commands;

import de.fload.core.commandHandler;
import de.fload.database.DatabaseAction;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Collection;

import static de.fload.database.DatabaseAction.isAllowed;
import static de.fload.util.FUNCTION.cmdMsg;
import static de.fload.util.FUNCTION.printWarning;

public class cmdHelp implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (!isAllowed(event, "help")) {
            printWarning(event, "You are not allowed to use that command");
            return;
        }

        StringBuilder builder = new StringBuilder();
        builder.append("```");
        for (int i = 0; i < commandHandler.commands.keySet().size(); i++) {
            builder.append(DatabaseAction.getCmdPrefix(event.getGuild())).append(commandHandler.commands.keySet().toArray()[i]);
            Collection<Command> clist = commandHandler.commands.values();
            Command c = (Command) clist.toArray()[i];
            builder.append(" - ").append(c.help());

            if (i + 1 < commandHandler.commands.keySet().size()) {
                builder.append("\n\n");
            }
        }
        builder.append("```");

        event.getTextChannel().sendMessage(builder.toString()).queue();
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        cmdMsg(event, "help");
    }

    @Override
    public String help() {
        return "Overview over all help messages.";
    }
}
