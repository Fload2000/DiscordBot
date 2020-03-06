package de.fload.listeners.channel.voice;

import de.fload.util.TIME;
import net.dv8tion.jda.api.events.channel.voice.update.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class voicechannelupdateListener extends ListenerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(voicechannelupdateListener.class);

    @Override
    public void onGenericVoiceChannelUpdate(@NotNull GenericVoiceChannelUpdateEvent event) {
        //do nothing
    }

    @Override
    public void onVoiceChannelUpdateName(@NotNull VoiceChannelUpdateNameEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] The voice-channel '" + event.getOldName() + "' was renamed to'" + event.getChannel().getName() + "'.";
        logger.info(text);
    }

    @Override
    public void onVoiceChannelUpdatePosition(@NotNull VoiceChannelUpdatePositionEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "][" + event.getChannel().getName() + "] The position was changed from '" + event.getOldPosition() + "' to '" + event.getChannel().getPosition() + "'.";
        logger.info(text);
    }

    @Override
    public void onVoiceChannelUpdateUserLimit(@NotNull VoiceChannelUpdateUserLimitEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "][" + event.getChannel().getName() + "] The user-limit was changed from '" + event.getOldUserLimit() + "' to '" + event.getChannel().getUserLimit() + "'.";
        logger.info(text);
    }

    @Override
    public void onVoiceChannelUpdateBitrate(@NotNull VoiceChannelUpdateBitrateEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "][" + event.getChannel().getName() + "] The bitrate was changed from '" + event.getOldBitrate() + "' to '" + event.getChannel().getBitrate() + "'.";
        logger.info(text);
    }

    @Override
    public void onVoiceChannelUpdatePermissions(@NotNull VoiceChannelUpdatePermissionsEvent event) {
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
    public void onVoiceChannelUpdateParent(@NotNull VoiceChannelUpdateParentEvent event) {
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
