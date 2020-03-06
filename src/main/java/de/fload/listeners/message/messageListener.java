package de.fload.listeners.message;

import de.fload.database.DatabaseAction;
import de.fload.util.TIME;
import net.dv8tion.jda.api.events.message.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static de.fload.util.FUNCTION.outputException;

public class messageListener extends ListenerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(messageListener.class);

    @Override
    public void onGenericMessage(@NotNull GenericMessageEvent event) {
        // do nothing
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "][" + event.getMessage().getTextChannel().getName() + "][" + event.getMessage().getAuthor().getName() + "#" + event.getMessage().getAuthor().getDiscriminator() + " (ID: " + event.getMessage().getAuthor().getId() + ")] " + event.getMessage().getContentRaw();
        logger.info(text);

        DatabaseAction.insertNewChatMessage(event);
    }

    @Override
    public void onMessageUpdate(@NotNull MessageUpdateEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "][" + event.getMessage().getTextChannel().getName() + "][" + event.getMessage().getAuthor().getName() + "#" + event.getMessage().getAuthor().getDiscriminator() + " (ID: " + event.getMessage().getAuthor().getId() + ")] " + event.getMessage().getContentRaw();
        logger.info(text);

        DatabaseAction.insertUpdateChatMessage(event);
    }

    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent event) {
        // do nothing
    }

    @Override
    public void onMessageBulkDelete(@NotNull MessageBulkDeleteEvent event) {
        // do nothing
    }

    @Override
    public void onMessageEmbed(@NotNull MessageEmbedEvent event) {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(TIME.getTimestamp());
            builder.append("[").append(event.getGuild().getName()).append("]");
            builder.append("[").append(event.getTextChannel().getName()).append("]");
            builder.append("[Embed]");
            if (event.getMessageEmbeds().size() > 0) {
                if (event.getMessageEmbeds().get(0).getAuthor() != null) {
                    if (Objects.requireNonNull(event.getMessageEmbeds().get(0).getAuthor()).getName() != null) {
                        builder.append(" Author: '").append(Objects.requireNonNull(event.getMessageEmbeds().get(0).getAuthor()).getName()).append("'");
                    }
                    if (Objects.requireNonNull(event.getMessageEmbeds().get(0).getAuthor()).getUrl() != null) {
                        builder.append(" Author-URL: '").append(Objects.requireNonNull(event.getMessageEmbeds().get(0).getAuthor()).getUrl()).append("'");
                    }
                    if (Objects.requireNonNull(event.getMessageEmbeds().get(0).getAuthor()).getIconUrl() != null) {
                        builder.append(" Author-Icon-URL: '").append(Objects.requireNonNull(event.getMessageEmbeds().get(0).getAuthor()).getIconUrl()).append("'");
                    }
                    if (Objects.requireNonNull(event.getMessageEmbeds().get(0).getAuthor()).getProxyIconUrl() != null) {
                        builder.append(" Author-Proxy-Icon-URL: '").append(Objects.requireNonNull(event.getMessageEmbeds().get(0).getAuthor()).getProxyIconUrl()).append("'");
                    }
                }
                if (event.getMessageEmbeds().get(0).getDescription() != null) {
                    builder.append(" Description: '").append(event.getMessageEmbeds().get(0).getDescription()).append("'");
                }
                if (event.getMessageEmbeds().get(0).getTitle() != null) {
                    builder.append(" Title: '").append(event.getMessageEmbeds().get(0).getTitle()).append("'");
                }
                if (event.getMessageEmbeds().get(0).getUrl() != null) {
                    builder.append(" URL: '").append(event.getMessageEmbeds().get(0).getUrl()).append("'");
                }
                if (event.getMessageEmbeds().get(0).getColorRaw() >= 0) {
                    builder.append(" Color: '").append(event.getMessageEmbeds().get(0).getColorRaw()).append("'");
                }
                if (event.getMessageEmbeds().get(0).getFooter() != null) {
                    if (Objects.requireNonNull(event.getMessageEmbeds().get(0).getFooter()).getText() != null) {
                        builder.append(" Footer: '").append(event.getMessageEmbeds().get(0).getFooter()).append("'");
                    }
                    if (Objects.requireNonNull(event.getMessageEmbeds().get(0).getFooter()).getIconUrl() != null) {
                        builder.append(" Footer-Icon-URL: '").append(Objects.requireNonNull(event.getMessageEmbeds().get(0).getFooter()).getIconUrl()).append("'");
                    }
                    if (Objects.requireNonNull(event.getMessageEmbeds().get(0).getFooter()).getProxyIconUrl() != null) {
                        builder.append(" Footer-Icon-Proxy-URL: '").append(Objects.requireNonNull(event.getMessageEmbeds().get(0).getFooter()).getProxyIconUrl()).append("'");
                    }
                }
                if (!event.getMessageEmbeds().get(0).getFields().isEmpty()) {
                    for (int i = 0; i < event.getMessageEmbeds().get(0).getFields().size(); i++) {
                        builder.append(" Field-").append(i).append(": '").append(event.getMessageEmbeds().get(0).getFields().get(i)).append("'");
                    }
                }
            }

            logger.info(builder.toString());

            DatabaseAction.insertNewEmbedMessage(event, builder.toString());
        } catch (NullPointerException e) {
            outputException(e);
        }
    }
}