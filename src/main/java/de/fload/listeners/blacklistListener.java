package de.fload.listeners;

import de.fload.database.DatabaseAction;
import de.fload.util.TIME;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static de.fload.util.FUNCTION.splitString;

public class blacklistListener extends ListenerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(blacklistListener.class);

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String msg = event.getMessage().getContentRaw();

        if (msg.startsWith(DatabaseAction.getCmdPrefix(event.getGuild()))) {
            return;
        }

        List<String> blacklist = DatabaseAction.getBlacklistForGuild(event.getGuild());
        ArrayList<String> message = splitString(msg);
        for (String m : blacklist) {
            if (message.contains(m)) {
                final String text = "Message{" + TIME.getTimestamp() + "[" + event.getGuild().getName() + "][" + event.getMessage().getTextChannel().getName() + "][" + event.getMessage().getAuthor().getName() + "#" + event.getMessage().getAuthor().getDiscriminator() + " (ID: " + event.getMessage().getAuthor().getId() + ")] " + event.getMessage().getContentRaw() + "} deleted - cause: Word on blacklist";
                logger.info(text);

                event.getMessage().delete().queue();
                break;
            }
        }
    }
}
