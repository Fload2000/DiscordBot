package de.fload.listeners.channel.text;

import de.fload.core.DiscordBot;
import de.fload.util.TIME;
import net.dv8tion.jda.api.events.channel.text.update.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class textupdateListener extends ListenerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(DiscordBot.class);

    @Override
    public void onGenericTextChannelUpdate(@NotNull GenericTextChannelUpdateEvent event) {
        //do nothing
    }

    @Override
    public void onTextChannelUpdateName(@NotNull TextChannelUpdateNameEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] The text-channel '" + event.getOldName() + "' was renamed to '" + event.getChannel().getName() + "'.";
        logger.info(text);
    }

    @Override
    public void onTextChannelUpdateTopic(@NotNull TextChannelUpdateTopicEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "][" + event.getChannel().getName() + "] The topic was changed from '" + event.getOldTopic() + "' to '" + event.getChannel().getTopic() + "'.";
        logger.info(text);
    }

    @Override
    public void onTextChannelUpdatePosition(@NotNull TextChannelUpdatePositionEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "][" + event.getChannel().getName() + "] The position was changed from '" + event.getOldPosition() + "' to '" + event.getChannel().getPosition() + "'.";
        logger.info(text);
    }

    @Override
    public void onTextChannelUpdatePermissions(@NotNull TextChannelUpdatePermissionsEvent event) {
        final String text;
        if (event.getChangedMembers().isEmpty()) {
            text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "][" + event.getChannel().getName() + "] The permission for the role '" + event.getChangedRoles().get(0).getName() + "' was changed.";
        } else if (event.getChangedRoles().isEmpty()) {
            text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "][" + event.getChannel().getName() + "] The permission for the user '" + event.getChangedMembers().get(0).getUser().getName() + "#" + event.getChangedMembers().get(0).getUser().getDiscriminator() + " (ID: " + event.getChangedMembers().get(0).getUser().getId() + ")' was changed.";
        } else {
            text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "][" + event.getChannel().getName() + "] The permission for " + event.getChangedPermissionHolders().get(0) + " was changed.";
        }
        logger.info(text);
    }

    @Override
    public void onTextChannelUpdateNSFW(@NotNull TextChannelUpdateNSFWEvent event) {
        final String text;
        if (event.getChannel().isNSFW()) {
            text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "][" + event.getChannel().getName() + "] NSFW was activated!";
        } else {
            text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "][" + event.getChannel().getName() + "] NSFW was deactivated!";
        }
        logger.info(text);
    }

    @Override
    public void onTextChannelUpdateParent(@NotNull TextChannelUpdateParentEvent event) {
        String text;
        try {
            text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "][" + event.getChannel().getName() + "] The top-category was changed from '" + Objects.requireNonNull(event.getOldParent()).getName() + "' to '" + Objects.requireNonNull(event.getChannel().getParent()).getName() + "'.";
        } catch (NullPointerException e) {
            try {
                text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "][" + event.getChannel().getName() + "] The top-category was changed from none to '" + Objects.requireNonNull(event.getChannel().getParent()).getName() + "'.";
            } catch (NullPointerException e2) {
                text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "][" + event.getChannel().getName() + "] The top-category was changed from '" + Objects.requireNonNull(event.getOldParent()).getName() + "' to none.";
            }
        }
        logger.info(text);
    }
}
