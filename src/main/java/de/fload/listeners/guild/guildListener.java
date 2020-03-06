package de.fload.listeners.guild;

import de.fload.util.TIME;
import net.dv8tion.jda.api.events.guild.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class guildListener extends ListenerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(guildListener.class);

    @Override
    public void onGenericGuild(@NotNull GenericGuildEvent event) {
        // do nothing
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] The bot joined this server.";
        logger.info(text);
    }

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] The bot has left this server.";
        logger.info(text);
    }

    @Override
    public void onGuildAvailable(@NotNull GuildAvailableEvent event) {
        // do nothing
    }

    @Override
    public void onGuildUnavailable(@NotNull GuildUnavailableEvent event) {
        // do nothing
    }

    @Override
    public void onUnavailableGuildJoined(@NotNull UnavailableGuildJoinedEvent event) {
        // do nothing
    }

    @Override
    public void onGuildBan(@NotNull GuildBanEvent event) {
        // do nothing
    }

    @Override
    public void onGuildUnban(@NotNull GuildUnbanEvent event) {
        // do nothing
    }
}
