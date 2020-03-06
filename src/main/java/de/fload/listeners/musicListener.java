package de.fload.listeners;

import de.fload.database.DatabaseAction;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import static de.fload.database.DatabaseAction.*;

public class musicListener extends ListenerAdapter {

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        if (event.getChannelJoined().getName().equals(getChannelMusicVoice(event.getGuild())) && !event.getMember().getUser().isBot()) {
            event.getGuild().getTextChannelsByName(getChannelMusicText(event.getGuild()), true).get(0).sendMessage(DatabaseAction.getCmdPrefix(event.getGuild()) + "music play " + getAutoplaylink(event.getGuild())).queue();
        }
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        if (event.getChannelLeft().getName().equals(getChannelMusicVoice(event.getGuild())) && !event.getMember().getUser().isBot()) {
            event.getGuild().getTextChannelsByName(getChannelMusicText(event.getGuild()), true).get(0).sendMessage(DatabaseAction.getCmdPrefix(event.getGuild()) + "music stop").queue();
        }
    }

    @Override
    public void onGuildVoiceMove(@NotNull GuildVoiceMoveEvent event) {
        if (event.getChannelJoined().getName().equals(getChannelMusicVoice(event.getGuild())) && !event.getMember().getUser().isBot()) {
            event.getGuild().getTextChannelsByName(getChannelMusicText(event.getGuild()), true).get(0).sendMessage(DatabaseAction.getCmdPrefix(event.getGuild()) + "music play " + getAutoplaylink(event.getGuild())).queue();
        }
    }
}
