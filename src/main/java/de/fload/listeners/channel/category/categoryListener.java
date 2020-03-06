package de.fload.listeners.channel.category;

import de.fload.util.TIME;
import net.dv8tion.jda.api.events.channel.category.CategoryCreateEvent;
import net.dv8tion.jda.api.events.channel.category.CategoryDeleteEvent;
import net.dv8tion.jda.api.events.channel.category.GenericCategoryEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class categoryListener extends ListenerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(categoryListener.class);

    @Override
    public void onGenericCategory(@NotNull GenericCategoryEvent event) {
        //do nothing
    }

    @Override
    public void onCategoryCreate(@NotNull CategoryCreateEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] A category named '" + event.getCategory().getName() + "' was created.";
        logger.info(text);
    }

    @Override
    public void onCategoryDelete(@NotNull CategoryDeleteEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] A category named '" + event.getCategory().getName() + "' was deleted.";
        logger.info(text);
    }
}
