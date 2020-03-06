package de.fload.listeners.guild;

import de.fload.util.TIME;
import net.dv8tion.jda.api.events.guild.update.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class updateListener extends ListenerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(updateListener.class);

    @Override
    public void onGenericGuildUpdate(@NotNull GenericGuildUpdateEvent event) {
        //do nothing
    }

    @Override
    public void onGuildUpdateAfkChannel(@NotNull GuildUpdateAfkChannelEvent event) {
        String text;
        try {
            text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] The AFK-channel was changed from '" + Objects.requireNonNull(event.getOldAfkChannel()).getName() + "' to '" + Objects.requireNonNull(event.getGuild().getAfkChannel()).getName() + "'.";
        } catch (NullPointerException e) {
            try {
                text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] The AFK-channel was changed from none to '" + Objects.requireNonNull(event.getGuild().getAfkChannel()).getName() + "'.";
            } catch (NullPointerException e2) {
                text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] The AFK-channel was changed from '" + Objects.requireNonNull(event.getOldAfkChannel()).getName() + "' to none.";
            }
        }
        logger.info(text);
    }

    @Override
    public void onGuildUpdateSystemChannel(@NotNull GuildUpdateSystemChannelEvent event) {
        String text;
        try {
            text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] The welcome-channel was changed from '" + Objects.requireNonNull(event.getOldSystemChannel()).getName() + "' to '" + Objects.requireNonNull(event.getGuild().getSystemChannel()).getName() + "'.";
        } catch (NullPointerException e) {
            try {
                text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] The welcome-channel was changed from none to '" + Objects.requireNonNull(event.getGuild().getSystemChannel()).getName() + "'.";
            } catch (NullPointerException e2) {
                text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] The welcome-channel was changed from '" + Objects.requireNonNull(event.getOldSystemChannel()).getName() + "' to none.";
            }
        }
        logger.info(text);
    }

    @Override
    public void onGuildUpdateAfkTimeout(@NotNull GuildUpdateAfkTimeoutEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] The AFK-time was changed from " + (event.getOldAfkTimeout().getSeconds() / 60) + " minute[s] to " + (event.getGuild().getAfkTimeout().getSeconds() / 60) + " minute[s].";
        logger.info(text);
    }

    @Override
    public void onGuildUpdateExplicitContentLevel(@NotNull GuildUpdateExplicitContentLevelEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] The filter-level was changed from '" + event.getOldLevel().getDescription() + "' to '" + event.getGuild().getExplicitContentLevel().getDescription() + "'.";
        logger.info(text);
    }

    @Override
    public void onGuildUpdateIcon(@NotNull GuildUpdateIconEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] The icon was changed from [" + event.getOldIconUrl() + "](ID: " + event.getOldIconId() + ") to [" + event.getGuild().getIconUrl() + "](ID: " + event.getGuild().getIconId() + ").";
        logger.info(text);
    }

    @Override
    public void onGuildUpdateMFALevel(@NotNull GuildUpdateMFALevelEvent event) {
        // do nothing
    }

    @Override
    public void onGuildUpdateName(@NotNull GuildUpdateNameEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] The name of the server was changed from " + event.getOldName() + " to " + event.getGuild().getName() + ".";
        logger.info(text);
    }

    @Override
    public void onGuildUpdateNotificationLevel(@NotNull GuildUpdateNotificationLevelEvent event) {
        String text = "";
        if (event.getGuild().getDefaultNotificationLevel().getKey() == 1) {
            text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] Only mentions get pushed.";
        } else if (event.getGuild().getDefaultNotificationLevel().getKey() == 0) {
            text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] All notifications get pushed.";
        }
        logger.info(text);
    }

    @Override
    public void onGuildUpdateOwner(@NotNull GuildUpdateOwnerEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] The owner was changed from " + Objects.requireNonNull(event.getOldOwner()).getUser().getName() + "#" + event.getOldOwner().getUser().getDiscriminator() + " (ID: " + event.getOldOwner().getUser().getId() + ") to " + Objects.requireNonNull(event.getGuild().getOwner()).getUser().getName() + "#" + event.getGuild().getOwner().getUser().getDiscriminator() + " (ID: " + event.getGuild().getOwner().getUser().getId() + ").";
        logger.info(text);
    }

    @Override
    public void onGuildUpdateRegion(@NotNull GuildUpdateRegionEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] The serverregion was changed from " + event.getOldRegionRaw() + " to " + event.getNewRegionRaw() + ".";
        logger.info(text);
    }

    @Override
    public void onGuildUpdateSplash(@NotNull GuildUpdateSplashEvent event) {
        // do nothing
    }

    @Override
    public void onGuildUpdateVerificationLevel(@NotNull GuildUpdateVerificationLevelEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] The verification-level was set from " + event.getOldVerificationLevel().name() + " to " + event.getGuild().getVerificationLevel().name() + ".";
        logger.info(text);
    }

    @Override
    public void onGuildUpdateFeatures(@NotNull GuildUpdateFeaturesEvent event) {
        // do nothing
    }
}
