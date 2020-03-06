package de.fload.listeners;

import de.fload.database.DatabaseAction;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class readyListener extends ListenerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(readyListener.class);

    @Override
    public void onReady(@NotNull ReadyEvent event) {

        // Init database: add missing data or update already present data.
        DatabaseAction.init(event);

        StringBuilder out = new StringBuilder("\nThis bot is on the following Discord servers: \n");

        for (Guild guild : event.getJDA().getGuilds()) {
            out.append(guild.getName()).append(" (").append(guild.getId()).append(") \n");
        }

        logger.info(out.toString());
    }
}
