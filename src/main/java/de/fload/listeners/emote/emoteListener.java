package de.fload.listeners.emote;

import de.fload.util.TIME;
import net.dv8tion.jda.api.events.emote.EmoteAddedEvent;
import net.dv8tion.jda.api.events.emote.EmoteRemovedEvent;
import net.dv8tion.jda.api.events.emote.GenericEmoteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class emoteListener extends ListenerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(emoteListener.class);

    @Override
    public void onGenericEmote(@NotNull GenericEmoteEvent event) {
        //do nothing
    }

    @Override
    public void onEmoteAdded(@NotNull EmoteAddedEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] A emote named '" + event.getEmote().getName() + "' was added.";
        logger.info(text);
    }

    @Override
    public void onEmoteRemoved(@NotNull EmoteRemovedEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] The emote named '" + event.getEmote().getName() + "' was deleted.";
        logger.info(text);
    }
}
