package de.fload.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import de.fload.audio.AudioInfo;
import de.fload.audio.PlayerSendHandler;
import de.fload.audio.TrackManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static de.fload.database.DatabaseAction.isAllowed;
import static de.fload.util.FUNCTION.cmdMsg;
import static de.fload.util.FUNCTION.printWarning;

public class cmdMusic implements Command {
    private static final int PLAYLIST_LIMIT = 1000;
    private static final AudioPlayerManager MANAGER = new DefaultAudioPlayerManager();
    private static final Map<Guild, Map.Entry<AudioPlayer, TrackManager>> PLAYERS = new HashMap<>();
    private static Guild guild;

    public cmdMusic() {
        AudioSourceManagers.registerRemoteSources(MANAGER);
    }

    private AudioPlayer createPlayer(Guild g) {
        AudioPlayer p = MANAGER.createPlayer();
        TrackManager m = new TrackManager(p);
        p.addListener(m);

        guild.getAudioManager().setSendingHandler(new PlayerSendHandler(p));

        PLAYERS.put(g, new AbstractMap.SimpleEntry<>(p, m));

        return p;
    }

    @Contract(pure = true)
    private boolean hasPlayer(Guild g) {
        return PLAYERS.containsKey(g);
    }

    private AudioPlayer getPlayer(Guild g) {
        if (hasPlayer(g)) {
            return PLAYERS.get(g).getKey();
        } else {
            return createPlayer(g);
        }
    }

    private TrackManager getManager(Guild g) {
        return PLAYERS.get(g).getValue();
    }

    private boolean isIdle(Guild g) {
        return !hasPlayer(g) || getPlayer(g).getPlayingTrack() == null;
    }

    private void loadTrack(String identifier, @NotNull Member author) {
        Guild guild = author.getGuild();
        getPlayer(guild);

        MANAGER.setFrameBufferDuration(1000);

        MANAGER.loadItemOrdered(guild, identifier, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                getManager(guild).queue(track, author);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                for (int i = 0; i < (Math.min(playlist.getTracks().size(), PLAYLIST_LIMIT)); i++) {
                    getManager(guild).queue(playlist.getTracks().get(i), author);
                }
            }

            @Override
            public void noMatches() {
                //do nothing
            }

            @Override
            public void loadFailed(FriendlyException e) {
                e.printStackTrace();
            }
        });
    }

    private void skip(Guild g) {
        getPlayer(g).stopTrack();
    }

    @NotNull
    private String getTimestamp(long millis) {
        long seconds = millis / 1000;
        long hours = Math.floorDiv(seconds, 3600);
        seconds = seconds - (hours * 3600);
        long mins = Math.floorDiv(seconds, 60);
        seconds = seconds - (mins * 60);
        return (hours == 0 ? "" : hours + ":") + String.format("%02d", mins) + ":" + String.format("%02d", seconds);
    }

    @NotNull
    private String buildQueueMessage(@NotNull AudioInfo info) {
        AudioTrackInfo trackInfo = info.getTrack().getInfo();
        String title = trackInfo.title;
        long length = trackInfo.length;
        return "`[ " + getTimestamp(length) + " ]` " + title + "\n";
    }

    private void sendErrorMsg(@NotNull MessageReceivedEvent event, String content) {
        event.getTextChannel().sendMessage(new EmbedBuilder()
                .setColor(Color.RED)
                .setDescription(content)
                .build()
        ).queue();
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (!isAllowed(event, "music")) {
            printWarning(event, "You are not allowed to use that command");
            return;
        }

        guild = event.getGuild();

        if (args.length < 1) {
            sendErrorMsg(event, help());
            return;
        }

        switch (args[0].toLowerCase()) {
            case "play": {

                if (args.length < 2) {
                    sendErrorMsg(event, "Please enter a valid source!");
                    return;
                }


                String input = Arrays.stream(args).skip(1).map(s -> " " + s).collect(Collectors.joining()).substring(1);

                if (!(input.startsWith("http://") || input.startsWith("https://"))) {
                    input = "ytsearch: " + input;
                }

                loadTrack(input, Objects.requireNonNull(event.getMember()));

                break;
            }
            case "skip": {
                if (isIdle(guild)) {
                    return;
                }

                for (int i = (args.length > 1 ? Integer.parseInt(args[1]) : 1); i == 1; i--) {
                    skip(guild);
                }

                break;
            }
            case "stop": {
                if (isIdle(guild)) {
                    return;
                }

                getManager(guild).purgeQueue();
                skip(guild);
                guild.getAudioManager().closeAudioConnection();

                break;
            }
            case "shuffle": {
                if (isIdle(guild)) {
                    return;
                }

                getManager(guild).shuffleQueue();

                break;
            }
            case "now": {

                if (isIdle(guild)) {
                    return;
                }

                AudioTrack track = getPlayer(guild).getPlayingTrack();
                AudioTrackInfo info = track.getInfo();

                Message msg = event.getTextChannel().sendMessage(new EmbedBuilder()
                        .setDescription("**CURRENT TRACK INFO:**")
                        .addField("Title", info.title, false)
                        .addField("Duration", "`[ " + getTimestamp(track.getPosition()) + "/" + getTimestamp(track.getDuration()) + " ]`", false)
                        .addField("Author", info.author, false)
                        .build()
                ).complete();

                int time_left = (int) track.getDuration() - (int) track.getPosition();

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        msg.delete().queue();
                    }
                }, time_left);

                break;
            }
            case "playlist": {

                if (isIdle(guild)) {
                    return;
                }

                int sideNumb = args.length > 1 ? Integer.parseInt(args[1]) : 1;

                List<String> tracks = new ArrayList<>();
                List<String> trackSublist;

                getManager(guild).getQueue().forEach(audioInfo -> tracks.add(buildQueueMessage(audioInfo)));

                if (tracks.size() > 20) {
                    trackSublist = tracks.subList((sideNumb - 1) * 20, (sideNumb - 1) * 20 + 20);
                } else {
                    trackSublist = tracks;
                }

                String out = String.join("\n", trackSublist);
                int sideNumbAll = tracks.size() >= 20 ? tracks.size() / 20 : 1;

                event.getTextChannel().sendMessage(new EmbedBuilder()
                        .setDescription("**CURRENT PLAYLIST:**\n" +
                                "*[" + getManager(guild).getQueue().size() + " Tracks | Side " + sideNumb + " / " + sideNumbAll + "]*\n" + out)
                        .build()
                ).queue();

                break;
            }
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        cmdMsg(event, "music");
    }

    @Override
    public String help() {
        return "Music - '!music help' for mor info";
    }
}
