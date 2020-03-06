package de.fload.listeners.emote;

import de.fload.util.TIME;
import net.dv8tion.jda.api.events.emote.update.EmoteUpdateNameEvent;
import net.dv8tion.jda.api.events.emote.update.EmoteUpdateRolesEvent;
import net.dv8tion.jda.api.events.emote.update.GenericEmoteUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class emoteupdateListener extends ListenerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(emoteupdateListener.class);

    @Override
    public void onGenericEmoteUpdate(@NotNull GenericEmoteUpdateEvent event) {
        //do nothing
    }

    @Override
    public void onEmoteUpdateName(@NotNull EmoteUpdateNameEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] The emote '" + event.getOldName() + "' was renamed to '" + event.getNewName() + "'.";
        logger.info(text);
    }

    @Override
    public void onEmoteUpdateRoles(@NotNull EmoteUpdateRolesEvent event) {
        // do nothing
    }
}
