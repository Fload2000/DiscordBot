package de.fload.database;

import de.fload.core.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * DatabaseUtil class - for creating the base layout of the database.
 */
public class DatabaseCreator {

    /**
     * Logger for logging various stuff.
     */
    private final static Logger logger = LoggerFactory.getLogger(DatabaseCreator.class);

    /**
     * Checks if the database exists and if it contains all necessary tables.
     */
    public static void initDatabase() {
        if(!checkExistence()) {
            logger.info("No Database ... creating ... ");
            create();
            logger.info("Database successfully created.");
        }
    }

    /**
     * Checks the existence of the database.
     *
     * @return Returns if the database exists.
     */
    private static boolean checkExistence() {
        return new File(Settings.getInstance().getDbPath()).exists();
    }

    /**
     * Creates the base layout of the database. This are all the tables.
     */
    private static void create() {
        logger.info("Creating the tables...");

        createUserTable();
        createServerTable();
        createUserOnServerTable();
        createRoleTable();
        createRoleOnServerTable();
        createBlacklistTable();
        createNickTable();
        createLevelTable();
        createExperienceTable();
        createChatLogTable();
        createCommandTable();
        createLinksTable();
        createListenerTable();
        createListenerChannelsTable();
        createPermissionTable();
        createPermissionUserTable();
        createPermissionRoleTable();
        createRSSTable();
        createRSSLogTable();

        createUniqueIndexServer();
        createUniqueIndexLevelTable();
        createUniqueIndexExperienceTable();
    }

    /**
     * Creates the TUSER table.
     */
    private static void createUserTable() {
        String statement = "CREATE TABLE IF NOT EXISTS TUSER (" +
                "DISCORDID NVARCHAR(128) NOT NULL PRIMARY KEY, " +
                "NAME TEXT NOT NULL);";
        Database.commitStatement(statement);
    }

    /**
     * Creates the TSERVER table.
     */
    private static void createServerTable() {
        String statement = "CREATE TABLE IF NOT EXISTS TSERVER (" +
                "DISCORDID NVARCHAR(128) NOT NULL PRIMARY KEY, " +
                "NAME TEXT NOT NULL, " +
                "OWNER NVARCHAR(128) NOT NULL, " +
                "CMDPREFIX NVARCHAR(1) NOT NULL," +
                "SHOWVOICELOG BIT, " +
                "MUSICAUTOPLAY BIT, " +
                "RSSREPEAT INTEGER, " +
                "INFOTEXT TEXT, " +
                "AUTOPLAYLINK TEXT, " +
                "RSSCHANNEL NVARCHAR(128), " +
                "VOICELOGCHANNEL NVARCHAR(128), " +
                "CHANNELMUSICVOICE NVARCHAR(128), " +
                "CHANNELMUSICTEXT NVARCHAR(128), " +
                "CHANNELWELCOME NVARCHAR(128)," +
                "SERVERREGION NVARCHAR(128), " +
                "FOREIGN KEY(OWNER) REFERENCES TUSER(DISCORDID));";
        Database.commitStatement(statement);
    }

    /**
     * Creates a unique index on the TSERVER table.
     */
    private static void createUniqueIndexServer() {
        String statement = "CREATE UNIQUE INDEX TSERVER_INDEX on TSERVER(DISCORDID);";
        Database.commitStatement(statement);
    }

    /**
     * Creates the TUSERONSERVER table.
     */
    private static void createUserOnServerTable() {
        String statement = "CREATE TABLE IF NOT EXISTS TUSERONSERVER (" +
                "SERVERID NVARCHAR(128) NOT NULL, " +
                "USERID NVARCHAR(128) NOT NULL, " +
                "PRIMARY KEY(SERVERID, USERID), " +
                "FOREIGN KEY(SERVERID) REFERENCES TSERVER(DISCORDID), " +
                "FOREIGN KEY(USERID) REFERENCES TUSER(DISCORDID));";
        Database.commitStatement(statement);
    }

    /**
     * Creates the TROLE table.
     */
    private static void createRoleTable() {
        String statement = "CREATE TABLE IF NOT EXISTS TROLE (" +
                "DISCORDID NVARCHAR(128) NOT NULL PRIMARY KEY, " +
                "NAME TEXT NOT NULL);";
        Database.commitStatement(statement);
    }

    /**
     * Creates the TROLEONSERVER table.
     */
    private static void createRoleOnServerTable() {
        String statement = "CREATE TABLE IF NOT EXISTS TROLEONSERVER (" +
                "SERVERID NVARCHAR(128) NOT NULL, " +
                "ROLEID NVARCHAR(128) NOT NULL, " +
                "PRIMARY KEY(SERVERID, ROLEID), " +
                "FOREIGN KEY(SERVERID) REFERENCES TSERVER(DISCORDID), " +
                "FOREIGN KEY(ROLEID) REFERENCES TROLE(DISCORDID));";
        Database.commitStatement(statement);
    }

    /**
     * Creates the TBLACKLIST table.
     */
    private static void createBlacklistTable() {
        String statement = "CREATE TABLE IF NOT EXISTS TBLACKLIST (" +
                "SERVERID NVARCHAR(128) NOT NULL, " +
                "PHRASE TEXT NOT NULL, " +
                "PRIMARY KEY(SERVERID, PHRASE), " +
                "FOREIGN KEY(SERVERID) REFERENCES TSERVER(DISCORDID));";
        Database.commitStatement(statement);
    }

    /**
     * Creates the TNICK table.
     */
    private static void createNickTable() {
        String statement = "CREATE TABLE IF NOT EXISTS TNICK (" +
                "SERVERID NVARCHAR(128) NOT NULL, " +
                "USERID NVARCHAR(128) NOT NULL, " +
                "NICK NVARCHAR(128) NOT NULL, " +
                "PRIMARY KEY(SERVERID, USERID), " +
                "FOREIGN KEY(SERVERID) REFERENCES TSERVER(DISCORDID), " +
                "FOREIGN KEY(USERID) REFERENCES TUSER(DISCORDID));";
        Database.commitStatement(statement);
    }

    /**
     * Creates the TLEVEL table.
     */
    private static void createLevelTable() {
        String statement = "CREATE TABLE IF NOT EXISTS TLEVEL (" +
                "SERVERID NVARCHAR(128) NOT NULL, " +
                "USERID NVARCHAR(128) NOT NULL, " +
                "LEVEL INTEGER NOT NULL, " +
                "PRIMARY KEY(SERVERID, USERID), " +
                "FOREIGN KEY(SERVERID) REFERENCES TSERVER(DISCORDID)," +
                "FOREIGN KEY(USERID) REFERENCES TUSER(DISCORDID));";
        Database.commitStatement(statement);
    }

    /**
     * Creates a unique index on the TLEVEL table.
     */
    private static void createUniqueIndexLevelTable() {
        String statement = "CREATE UNIQUE INDEX TLEVEL_INDEX on TLEVEL(SERVERID, USERID);";
        Database.commitStatement(statement);
    }

    /**
     * Creates the TEXPERIENCE table.
     */
    private static void createExperienceTable() {
        String statement = "CREATE TABLE IF NOT EXISTS TEXPERIENCE (" +
                "SERVERID NVARCHAR(128) NOT NULL, " +
                "USERID NVARCHAR(128) NOT NULL, " +
                "EXPERIENCE INTEGER NOT NULL, " +
                "PRIMARY KEY(SERVERID, USERID), " +
                "FOREIGN KEY(SERVERID) REFERENCES TSERVER(DISCORDID), " +
                "FOREIGN KEY(USERID) REFERENCES TUSER(DISCORDID));";
        Database.commitStatement(statement);
    }

    /**
     * Creates a unique index on the TEXPERIENCE table.
     */
    private static void createUniqueIndexExperienceTable() {
        String statement = "CREATE UNIQUE INDEX TEXPERIENCE_INDEX on TEXPERIENCE(SERVERID, USERID);";
        Database.commitStatement(statement);
    }

    /**
     * Creates the TCHATLOG table.
     */
    private static void createChatLogTable() {
        String statement = "CREATE TABLE IF NOT EXISTS TCHATLOG (" +
                "SERVERID NVARCHAR(128) NOT NULL, " +
                "USERID NVARCHAR(128) NOT NULL, " +
                "DISCORDID NVARCHAR(128) NOT NULL," +
                "CHANNEL NVARCHAR(128) NOT NULL, " +
                "DATE NVARCHAR(32) NOT NULL, " +
                "TIME NVARCHAR(32) NOT NULL, " +
                "TYPE NVARCHAR(1) NOT NULL, " +
                "MESSAGE TEXT NOT NULL, " +
                "STATUS NVARCHAR(32), " +
                "PRIMARY KEY(SERVERID, USERID, DISCORDID, STATUS), " +
                "FOREIGN KEY(SERVERID) REFERENCES TSERVER(DISCORDID), " +
                "FOREIGN KEY(USERID) REFERENCES TUSER(DISCORDID));";
        Database.commitStatement(statement);
    }

    /**
     * Creates the TCOMMAND table.
     */
    private static void createCommandTable() {
        String statement = "CREATE TABLE IF NOT EXISTS TCOMMAND (" +
                "SERVERID NVARCHAR(128) NOT NULL, " +
                "NAME NVARCHAR(128) NOT NULL, " +
                "MESSAGE TEXT NOT NULL, " +
                "PRIMARY KEY(SERVERID, NAME), " +
                "FOREIGN KEY(SERVERID) REFERENCES TSERVER(DISCORDID));";
        Database.commitStatement(statement);
    }

    /**
     * Create the TLINKS table.
     */
    private static void createLinksTable() {
        String statement = "CREATE TABLE IF NOT EXISTS TLINKS (" +
                "EXPRESSION TEXT NOT NULL PRIMARY KEY);";
        Database.commitStatement(statement);
    }

    /**
     * Creates the TLISTENER table.
     */
    private static void createListenerTable() {
        String statement = "CREATE TABLE IF NOT EXISTS TLISTENER (" +
                "SERVERID NVARCHAR(128) NOT NULL, " +
                "NAME NVARCHAR(128) NOT NULL, " +
                "ACTIVE BIT NOT NULL, " +
                "PRIMARY KEY(SERVERID, NAME), " +
                "FOREIGN KEY(SERVERID) REFERENCES TSERVER(DISCORDID));";
        Database.commitStatement(statement);
    }

    /**
     * Creates the TLISTENERCHANNEL table.
     */
    private static void createListenerChannelsTable() {
        String statement = "CREATE TABLE IF NOT EXISTS TLISTENERCHANNEL (" +
                "SERVERID NVARCHAR(128) NOT NULL, " +
                "NAME NVARCHAR(128) NOT NULL, " +
                "CHANNEL NVARCHAR(128) NOT NULL, " +
                "PRIMARY KEY(SERVERID, NAME), " +
                "FOREIGN KEY(SERVERID) REFERENCES TLISTENER(SERVERID), " +
                "FOREIGN KEY(NAME) REFERENCES TLISTENER(NAME));";
        Database.commitStatement(statement);
    }

    /**
     * Creates the TPERMISSION table.
     */
    private static void createPermissionTable() {
        String statement = "CREATE TABLE IF NOT EXISTS TPERMISSION (" +
                "ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "SERVERID NVARCHAR(128) NOT NULL, " +
                "NAME NVARCHAR(128) NOT NULL, " +
                "FOREIGN KEY(SERVERID) REFERENCES TSERVER(DISCORDID));";
        Database.commitStatement(statement);
    }

    /**
     * Creates the TPERMISSIONROLE table.
     */
    private static void createPermissionRoleTable() {
        String statement = "CREATE TABLE IF NOT EXISTS TPERMISSIONROLE (" +
                "PERMISSIONID INTEGER NOT NULL, " +
                "ROLEID NVARCHAR(128) NOT NULL, " +
                "PRIMARY KEY(PERMISSIONID, ROLEID), " +
                "FOREIGN KEY(PERMISSIONID) REFERENCES TPERMISSION(ID), " +
                "FOREIGN KEY(ROLEID) REFERENCES TROLE(DISCORDID));";
        Database.commitStatement(statement);
    }

    /**
     * Creates the TPERMISSIONUSER table.
     */
    private static void createPermissionUserTable() {
        String statement = "CREATE TABLE IF NOT EXISTS TPERMISSIONUSER (" +
                "PERMISSIONID INTEGER NOT NULL, " +
                "USERID NVARCHAR(128) NOT NULL, " +
                "PRIMARY KEY(PERMISSIONID, USERID), " +
                "FOREIGN KEY(PERMISSIONID) REFERENCES TPERMISSION(ID), " +
                "FOREIGN KEY(USERID) REFERENCES TUSER(DISCORDID));";
        Database.commitStatement(statement);
    }

    /**
     * Creates the TRSS table.
     */
    private static void createRSSTable() {
        String statement = "CREATE TABLE IF NOT EXISTS TRSS (" +
                "SERVERID NVARCHAR(128) NOT NULL, " +
                "NAME NVARCHAR(128) NOT NULL, " +
                "LINK TEXT NOT NULL, " +
                "PRIMARY KEY(SERVERID, NAME), " +
                "FOREIGN KEY(SERVERID) REFERENCES TSERVER(DISCORDID));";
        Database.commitStatement(statement);
    }

    /**
     * Creates the TRSSLOG table.
     */
    private static void createRSSLogTable() {
        String statement = "CREATE TABLE IF NOT EXISTS TRSSLOG (" +
                "RSSNAME NVARCHAR(128) NOT NULL, " +
                "SERVERID NVARCHAR(128) NOT NULL, " +
                "MESSAGE TEXT NOT NULL, " +
                "PRIMARY KEY(RSSNAME, SERVERID, MESSAGE), " +
                "FOREIGN KEY(RSSNAME) REFERENCES TRSS(NAME), " +
                "FOREIGN KEY(SERVERID) REFERENCES TRSS(SERVERID));";
        Database.commitStatement(statement);
    }
}
