package de.fload.listeners.channel.text;

import de.fload.util.TIME;
import net.dv8tion.jda.api.events.channel.text.GenericTextChannelEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class textListener extends ListenerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(textListener.class);

    @Override
    public void onGenericTextChannel(@NotNull GenericTextChannelEvent event) {
        //do nothing
    }

    @Override
    public void onTextChannelCreate(@NotNull TextChannelCreateEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] A new text-channel named '" + event.getChannel().getName() + "' was created.";
        logger.info(text);
    }

    @Override
    public void onTextChannelDelete(@NotNull TextChannelDeleteEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] A text-channel named '" + event.getChannel().getName() + "' was deleted.";
        logger.info(text);
    }
}
