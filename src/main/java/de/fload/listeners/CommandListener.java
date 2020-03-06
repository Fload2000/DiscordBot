package de.fload.listeners;

import de.fload.core.commandHandler;
import de.fload.core.commandParser;
import de.fload.database.DatabaseAction;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().startsWith(DatabaseAction.getCmdPrefix(event.getGuild())) && !event.getMessage().getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {
            commandHandler.handleCommand(commandParser.parser(event.getMessage().getContentRaw(), event));
        }
    }
}
