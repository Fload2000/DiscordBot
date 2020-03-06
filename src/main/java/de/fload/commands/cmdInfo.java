package de.fload.commands;

import de.fload.database.DatabaseAction;
import de.fload.util.TIME;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static de.fload.database.DatabaseAction.isAllowed;
import static de.fload.util.FUNCTION.*;

public class cmdInfo implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (!isAllowed(event, "info")) {
            printWarning(event, "You are not allowed to use that command");
            return;
        }

        try {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(Objects.requireNonNull(event.getMember()).getColor());
            builder.setAuthor(Objects.requireNonNull(event.getGuild().getOwner()).getUser().getName(), "https://discordapp.com/widget?id=" + event.getGuild().getId() + "&theme=dark", event.getGuild().getOwner().getUser().getAvatarUrl());
            builder.setTitle(event.getGuild().getName());
            builder.setThumbnail(event.getGuild().getIconUrl());
            System.out.println(DatabaseAction.getInfotext(event.getGuild()));
            builder.setDescription(DatabaseAction.getInfotext(event.getGuild()));
            builder.setFooter(event.getAuthor().getName() + " | " + TIME.getFullTimestamp(), event.getMember().getUser().getAvatarUrl());

            String value1 = "**Owner:**" + "\n" +
                    "**Creation Date:**" + "\n" +
                    "**Region:**" + "\n" +
                    "**User Online:**" + "\n" +
                    "**User Idle:**" + "\n" +
                    "**User Dnd:**" + "\n" +
                    "**User Offline:**";
            builder.addField("INFO", value1, true);

            int all = event.getGuild().getMembers().size();
            int online = 0;
            int idle = 0;
            int dnd = 0;
            int offline = 0;
            for (Member m : event.getGuild().getMembers()) {
                switch (m.getOnlineStatus().getKey()) {
                    case "online": {
                        online++;
                        break;
                    }
                    case "idle": {
                        idle++;
                        break;
                    }
                    case "dnd": {
                        dnd++;
                        break;
                    }
                    case "offline": {
                        offline++;
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }

            String value2 = "`" + event.getGuild().getOwner().getUser().getName() + "`" + "\n" +
                    "`" + event.getGuild().getTimeCreated().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")) + "`" + "\n" +
                    "`" + event.getGuild().getRegionRaw() + "`" + "\n" +
                    "`" + online + "/" + all + "`" + "\n" +
                    "`" + idle + "/" + all + "`" + "\n" +
                    "`" + dnd + "/" + all + "`" + "\n" +
                    "`" + offline + "/" + all + "`";
            builder.addField("", value2, true);

            event.getTextChannel().sendMessage(builder.build()).queue();
        } catch (Exception e) {
            outputException(e);
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        cmdMsg(event, "info");
    }

    @Override
    public String help() {
        return "Infomessage about the server.";
    }
}
