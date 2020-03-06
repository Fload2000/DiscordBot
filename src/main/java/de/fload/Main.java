package de.fload;

import de.fload.core.DiscordBot;
import de.fload.core.Settings;
import de.fload.database.DatabaseCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class
 */
public class Main {

    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    /**
     * Main method.
     *
     * @param args execution flags
     */
    public static void main(String[] args) {
        // load settings from config file.
        logger.info("Load settings.");
        Settings.getInstance().load();
        logger.info("Settings successfully loaded.");

        // initialize the database
        logger.info("Init database.");
        DatabaseCreator.initDatabase();
        logger.info("Database successfully initialized.");

        // init and start the Discord-Bot
        logger.info("Init Discord-Bot.");
        DiscordBot.init();
    }
}
