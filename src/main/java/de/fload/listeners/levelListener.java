package de.fload.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static de.fload.database.DatabaseAction.*;
import static de.fload.util.FUNCTION.*;

public class levelListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        int exp = (int) (Math.random() * 10) + 20;
        int currentxp = getExperience(event);
        int level = getLevel(event);
        int levelxp = calculateLevelXp(level);
        if (currentxp + exp > levelxp) {
            level++;
            setLevel(event, level);
            lvlMsg(event, level);
            printLevelUp(event);
            setExperience(event, (currentxp + exp) - levelxp);
        } else {
            setExperience(event, currentxp + exp);
        }
        expMsg(event, exp);
    }

    private int calculateLevelXp(int level) {
        return (int) Math.pow(level, 3.25)+250;
    }

    private int calculateXpToLevel(int level) {
        int value = 0;
        for (int i = 0; i <= level; i++) {
            value += calculateLevelXp(i);
        }
        return value;
    }

    private void printLevelUp(@NotNull MessageReceivedEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Objects.requireNonNull(event.getMember()).getColor());
        builder.setTitle("**LEVEL UP**");
        builder.setDescription(":star: <@" + event.getMember().getUser().getId() + "> is now Level " + getLevel(event) + " :star:");
        event.getTextChannel().sendMessage(builder.build()).queue();
    }
}
