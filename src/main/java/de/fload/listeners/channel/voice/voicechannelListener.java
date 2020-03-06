package de.fload.listeners.channel.voice;

import de.fload.util.TIME;
import net.dv8tion.jda.api.events.channel.voice.GenericVoiceChannelEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class voicechannelListener extends ListenerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(voicechannelListener.class);

    @Override
    public void onGenericVoiceChannel(@NotNull GenericVoiceChannelEvent event) {
        //do nothing
    }

    @Override
    public void onVoiceChannelCreate(@NotNull VoiceChannelCreateEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] A new voice-channel named '" + event.getChannel().getName() + "' was created.";
        logger.info(text);
    }

    @Override
    public void onVoiceChannelDelete(@NotNull VoiceChannelDeleteEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] The voice-channel named '" + event.getChannel().getName() + "' was deleted.";
        logger.info(text);
    }
}
