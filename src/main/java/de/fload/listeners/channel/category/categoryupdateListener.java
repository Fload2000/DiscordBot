package de.fload.listeners.channel.category;

import de.fload.util.TIME;
import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdateNameEvent;
import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdatePermissionsEvent;
import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdatePositionEvent;
import net.dv8tion.jda.api.events.channel.category.update.GenericCategoryUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class categoryupdateListener extends ListenerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(categoryupdateListener.class);

    @Override
    public void onGenericCategoryUpdate(@NotNull GenericCategoryUpdateEvent event) {
        //do nothing
    }

    @Override
    public void onCategoryUpdateName(@NotNull CategoryUpdateNameEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] The category '" + event.getOldName() + "' was renamed to '" + event.getCategory().getName() + "'.";
        logger.info(text);
    }

    @Override
    public void onCategoryUpdatePosition(@NotNull CategoryUpdatePositionEvent event) {
        final String text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] The category '" + event.getCategory().getName() + "' was moved from position " + event.getOldPosition() + " to position " + event.getCategory().getPosition() + ".";
        logger.info(text);
    }

    @Override
    public void onCategoryUpdatePermissions(@NotNull CategoryUpdatePermissionsEvent event) {
        final String text;
        if (event.getChangedPermissionHolders().isEmpty()) {
            text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] In category '" + event.getCategory().getName() + "' the permission for the role '" + event.getChangedRoles().get(0).getName() + "' was changed.";
        } else if (event.getChangedRoles().isEmpty()) {
            text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] In category '" + event.getCategory().getName() + "' the permission for the user '" + event.getChangedMembers().get(0).getUser().getName() + "#" + event.getChangedMembers().get(0).getUser().getDiscriminator() + " (ID: " + event.getChangedMembers().get(0).getUser().getId() + ")' was changed.";
        } else {
            text = TIME.getTimestamp() + "[" + event.getGuild().getName() + "] In category '" + event.getCategory().getName() + "' the permission " + event.getChangedPermissionHolders().get(0) + " was changed.";
        }
        logger.info(text);
    }
}
