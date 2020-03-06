package de.fload.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static de.fload.database.DatabaseAction.getLevel;
import static de.fload.database.DatabaseAction.isAllowed;
import static de.fload.util.FUNCTION.*;

public class cmdLevel implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (!isAllowed(event, "level")) {
            printWarning(event, "You are not allowed to use that command");
            return;
        }

        printLevel(event);
    }

    private void printLevel(@NotNull MessageReceivedEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Objects.requireNonNull(event.getMember()).getColor());
        builder.setDescription(":star: <@" + event.getMember().getUser().getId() + "> is Level " + getLevel(event) + " :star:");
        event.getTextChannel().sendMessage(builder.build()).queue();
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        cmdMsg(event, "level");
    }

    @Override
    public String help() {
        return "Returns the user level";
    }
}
