package de.fload.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static de.fload.database.DatabaseAction.isAllowed;
import static de.fload.util.FUNCTION.*;

public class cmdClear implements Command {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        // Check if user is allowed to use that command.
        if (!isAllowed(event, "clear")) {
            printWarning(event, "You are not allowed to use that command");
            return;
        }

        if (args.length == 0) {
            printError(event, "No flags set!");
        } else if (args.length > 2) {
            printError(event, "Too much Flags set!");
        } else {
            try {
                MessageHistory history = new MessageHistory(event.getTextChannel());
                var amount = getInteger(args[0]);
                if (amount.isPresent()) {
                    List<Message> msgs = history.retrievePast(amount.get() + 1).complete();
                    event.getTextChannel().deleteMessages(msgs).queue();

                    Message msg = event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.GREEN).setDescription(args[0] + " Messages have been deleted!").build()).complete();

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            msg.delete().queue();
                        }
                    }, 3000);
                }

            } catch (Exception e) {
                event.getMessage().delete().queue();

                Message msg = event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.RED).setDescription("Message/Messages are to old to delete!").build()).complete();

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        msg.delete().queue();
                    }
                }, 4000);
            }
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        cmdMsg(event, "clear");
    }

    @Override
    public String help() {
        return "Deletes a specific amount of messages";
    }
}
