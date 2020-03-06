package de.fload.listeners.guild;

import de.fload.util.TIME;
import net.dv8tion.jda.api.events.guild.voice.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static de.fload.database.DatabaseAction.getVoiceLogChannel;
import static de.fload.database.DatabaseAction.showVoicelog;

public class voiceListener extends ListenerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(voiceListener.class);

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        if (showVoicelog(event.getGuild())) {
            Objects.requireNonNull(event.getGuild().getTextChannelById(getVoiceLogChannel(event.getGuild()))).sendMessage("**" + event.getVoiceState().getMember().getUser().getName() + "** has connected to **" + event.getChannelJoined().getName() + "**.").queue();
        }
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] " + event.getVoiceState().getMember().getUser().getName() + "#" + event.getVoiceState().getMember().getUser().getDiscriminator() + " (ID: " + event.getVoiceState().getMember().getUser().getId() + ") has connected to '" + event.getChannelJoined().getName() + "'.";
        logger.info(text);
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        if (showVoicelog(event.getGuild())) {
            Objects.requireNonNull(event.getGuild().getTextChannelById(getVoiceLogChannel(event.getGuild()))).sendMessage("**" + event.getVoiceState().getMember().getUser().getName() + "** has left the voice-channel **" + event.getChannelLeft().getName() + "**.").queue();
        }
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] " + event.getVoiceState().getMember().getUser().getName() + "#" + event.getVoiceState().getMember().getUser().getDiscriminator() + " (ID: " + event.getVoiceState().getMember().getUser().getId() + ") has left the voice-channel '" + event.getChannelLeft().getName() + "'.";
        logger.info(text);
    }

    @Override
    public void onGuildVoiceMove(@NotNull GuildVoiceMoveEvent event) {
        if (showVoicelog(event.getGuild())) {
            Objects.requireNonNull(event.getGuild().getTextChannelById(getVoiceLogChannel(event.getGuild()))).sendMessage("**" + event.getVoiceState().getMember().getUser().getName() + "** switched from **" + event.getChannelLeft().getName() + "** to **" + event.getChannelJoined().getName() + "**.").queue();
        }
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] " + event.getVoiceState().getMember().getUser().getName() + "#" + event.getVoiceState().getMember().getUser().getDiscriminator() + " (ID: " + event.getVoiceState().getMember().getUser().getId() + ") switched from '" + event.getChannelLeft().getName() + "' to '" + event.getChannelJoined().getName() + "'. ";
        logger.info(text);
    }

    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {
        // do nothing
    }

    @Override
    public void onGuildVoiceMute(GuildVoiceMuteEvent event) {
        String text = "";
        if (event.getVoiceState().isMuted()) {
            if (showVoicelog(event.getGuild())) {
                Objects.requireNonNull(event.getGuild().getTextChannelById(getVoiceLogChannel(event.getGuild()))).sendMessage("**" + event.getVoiceState().getMember().getUser().getName() + "** is muted.").queue();
            }
            text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] " + event.getVoiceState().getMember().getUser().getName() + "#" + event.getVoiceState().getMember().getUser().getDiscriminator() + " (ID: " + event.getVoiceState().getMember().getUser().getId() + ") is muted.";
        } else if (!event.getVoiceState().isMuted()) {
            if (showVoicelog(event.getGuild())) {
                Objects.requireNonNull(event.getGuild().getTextChannelById(getVoiceLogChannel(event.getGuild()))).sendMessage("**" + event.getVoiceState().getMember().getUser().getName() + "** is unmute.").queue();
            }
            text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] " + event.getVoiceState().getMember().getUser().getName() + "#" + event.getVoiceState().getMember().getUser().getDiscriminator() + " (ID: " + event.getVoiceState().getMember().getUser().getId() + ") is unmute.";
        }
        logger.info(text);
    }

    @Override
    public void onGuildVoiceDeafen(GuildVoiceDeafenEvent event) {
        String text = "";
        if (event.getVoiceState().isDeafened()) {
            if (showVoicelog(event.getGuild())) {
                Objects.requireNonNull(event.getGuild().getTextChannelById(getVoiceLogChannel(event.getGuild()))).sendMessage("**" + event.getVoiceState().getMember().getUser().getName() + "** is deaf.").queue();
            }
            text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] " + event.getVoiceState().getMember().getUser().getName() + "#" + event.getVoiceState().getMember().getUser().getDiscriminator() + " (ID: " + event.getVoiceState().getMember().getUser().getId() + ") is deaf.";
        } else if (!event.getVoiceState().isDeafened()) {
            if (showVoicelog(event.getGuild())) {
                Objects.requireNonNull(event.getGuild().getTextChannelById(getVoiceLogChannel(event.getGuild()))).sendMessage("**" + event.getVoiceState().getMember().getUser().getName() + "** is audible.").queue();
            }
            text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] " + event.getVoiceState().getMember().getUser().getName() + "#" + event.getVoiceState().getMember().getUser().getDiscriminator() + " (ID: " + event.getVoiceState().getMember().getUser().getId() + ") is audible.";
        }
        logger.info(text);
    }

    @Override
    public void onGuildVoiceGuildMute(GuildVoiceGuildMuteEvent event) {
        String text = "";
        if (event.getVoiceState().isGuildMuted()) {
            if (showVoicelog(event.getGuild())) {
                Objects.requireNonNull(event.getGuild().getTextChannelById(getVoiceLogChannel(event.getGuild()))).sendMessage("**" + Objects.requireNonNull(event.getVoiceState().getChannel()).getName() + "** was muted.").queue();
            }
            text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] '" + Objects.requireNonNull(event.getVoiceState().getChannel()).getName() + "' was muted.";
        } else if (!event.getVoiceState().isGuildMuted()) {
            if (showVoicelog(event.getGuild())) {
                Objects.requireNonNull(event.getGuild().getTextChannelById(getVoiceLogChannel(event.getGuild()))).sendMessage("**" + Objects.requireNonNull(event.getVoiceState().getChannel()).getName() + "** was unmute.").queue();
            }
            text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] '" + Objects.requireNonNull(event.getVoiceState().getChannel()).getName() + "' was unmute.";
        }
        logger.info(text);
    }

    @Override
    public void onGuildVoiceGuildDeafen(GuildVoiceGuildDeafenEvent event) {
        String text = "";
        if (event.getVoiceState().isGuildDeafened()) {
            if (showVoicelog(event.getGuild())) {
                Objects.requireNonNull(event.getGuild().getTextChannelById(getVoiceLogChannel(event.getGuild()))).sendMessage("**" + Objects.requireNonNull(event.getVoiceState().getChannel()).getName() + "** was set to deafen.").queue();
            }
            text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] '" + Objects.requireNonNull(event.getVoiceState().getChannel()).getName() + "' was set to deafen.";
        } else if (!event.getVoiceState().isGuildDeafened()) {
            if (showVoicelog(event.getGuild())) {
                Objects.requireNonNull(event.getGuild().getTextChannelById(getVoiceLogChannel(event.getGuild()))).sendMessage("**" + Objects.requireNonNull(event.getVoiceState().getChannel()).getName() + "** was set to audible.").queue();
            }
            text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] '" + Objects.requireNonNull(event.getVoiceState().getChannel()).getName() + "' was set to audible.";
        }
        logger.info(text);
    }

    @Override
    public void onGuildVoiceSelfMute(@NotNull GuildVoiceSelfMuteEvent event) {
        // do nothing
    }

    @Override
    public void onGuildVoiceSelfDeafen(@NotNull GuildVoiceSelfDeafenEvent event) {
        // do nothing
    }

    @Override
    public void onGuildVoiceSuppress(@NotNull GuildVoiceSuppressEvent event) {
        // do nothing
    }
}
