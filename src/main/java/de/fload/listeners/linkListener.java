package de.fload.listeners;

import de.fload.database.DatabaseAction;
import de.fload.util.TIME;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.fload.database.DatabaseAction.isAllowed;

public class linkListener extends ListenerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(linkListener.class);

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String msg = event.getMessage().getContentRaw();
        if (msg.startsWith(DatabaseAction.getCmdPrefix(event.getGuild()))) {
            return;
        }
        for (String m : DatabaseAction.getLinkparts()) {
            if (msg.contains(m)) {
                if (!isAllowed(event, "linkListener") || event.getAuthor().isBot()) {
                    return;
                } else {
                    final String text = "Message{" + TIME.getTimestamp() + "[" + event.getGuild().getName() + "][" + event.getMessage().getTextChannel().getName() + "][" + event.getMessage().getAuthor().getName() + "#" + event.getMessage().getAuthor().getDiscriminator() + " (ID: " + event.getMessage().getAuthor().getId() + ")] " + event.getMessage().getContentRaw() + "} deleted - cause: Link";
                    logger.info(text);

                    event.getMessage().delete().queue();
                    break;
                }
            }
        }
    }
}

