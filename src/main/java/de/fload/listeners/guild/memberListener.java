package de.fload.listeners.guild;

import de.fload.database.DatabaseAction;
import de.fload.util.TIME;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.guild.member.*;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class memberListener extends ListenerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(memberListener.class);

    @Override
    public void onGenericGuildMember(@NotNull GenericGuildMemberEvent event) {
        // do nothing
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] " + event.getMember().getUser().getName() + "#" + event.getMember().getUser().getDiscriminator() + " (ID: " + event.getUser().getId() + ") has joined.";
        logger.info(text);

        Message msg = Objects.requireNonNull(event.getGuild().getTextChannelById(DatabaseAction.getChannelWelcome(event.getGuild()))).sendMessage("Hello <@" + event.getMember().getUser().getId() + ">, welcome on " + event.getGuild().getName() + "!").complete();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                msg.delete().queue();
            }
        }, 60000);
    }

    @Override
    public void onGuildMemberLeave(@NotNull GuildMemberLeaveEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] " + event.getMember().getUser().getName() + "#" + event.getMember().getUser().getDiscriminator() + " (ID: " + event.getUser().getId() + ") has left.";
        logger.info(text);

        Message msg = Objects.requireNonNull(event.getGuild().getTextChannelById(DatabaseAction.getChannelWelcome(event.getGuild()))).sendMessage("<@" + event.getMember().getUser().getId() + "> left the server!").complete();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                msg.delete().queue();
            }
        }, 60000);
    }

    @Override
    public void onGuildMemberRoleAdd(@NotNull GuildMemberRoleAddEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] " + event.getMember().getUser().getName() + "#" + event.getMember().getUser().getDiscriminator() + " (ID: " + event.getUser().getId() + ") has received the role '" + event.getRoles().get(0).getName() + "'.";
        logger.info(text);
    }

    @Override
    public void onGuildMemberRoleRemove(@NotNull GuildMemberRoleRemoveEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] " + event.getMember().getUser().getName() + "#" + event.getMember().getUser().getDiscriminator() + " (ID: " + event.getUser().getId() + ") was taken the role '" + event.getRoles().get(0).getName() + "'.";
        logger.info(text);
    }

    @Override
    public void onGuildMemberUpdateNickname(@NotNull GuildMemberUpdateNicknameEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] " + event.getMember().getUser().getName() + "#" + event.getMember().getUser().getDiscriminator() + " (ID: " + event.getUser().getId() + ") changed the nick from '" + event.getOldNickname() + "' to '" + event.getNewNickname() + "'.";
        logger.info(text);
    }
}
