package de.fload.database;

import de.fload.core.Settings;
import de.fload.util.TIME;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageEmbedEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static de.fload.database.Database.connect;
import static de.fload.util.FUNCTION.outputException;

/**
 * DatabaseAction class - contains methods for performing various queries on the database.
 */
public class DatabaseAction {

    /**
     * Initializes all the given data and updates everything which comes directly from the discord API.
     *
     * @param event {@link Event} for an unspecified event where all the data comes from.
     */
    public static void init(Event event) {
        for (var guild : event.getJDA().getGuilds()) {
            initUser(guild);
            initRoles(guild);
            updateGuild(guild);
        }
    }

    /**
     * Initialises all the users from a guild or updates their data.
     *
     * @param guild {@link Guild} for the guild which users should be considered.
     */
    private static void initUser(Guild guild) {
        for (var member : guild.getMembers()) {
            updateUser(member);
            addUserToServer(member, guild);
            addLevelToUser(guild, member);
            addExperienceToUser(guild, member);
        }
    }

    /**
     * Updates the user data stored in the database of the given user.
     *
     * @param member {@link Member} for the member whose data should be updated.
     */
    public static void updateUser(Member member) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("REPLACE INTO TUSER(DISCORDID, NAME) VALUES (?, ?);")) {
            pstmt.setString(1, member.getId());
            pstmt.setString(2, member.getUser().getName());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            outputException(e);
        }
    }

    /**
     * Adds a user to a server.
     *
     * @param member {@link Member} for the user.
     * @param guild  {@link Guild} for the server/guild.
     */
    public static void addUserToServer(Member member, Guild guild) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("REPLACE INTO TUSERONSERVER(SERVERID, USERID) VALUES (?, ?);")) {
            pstmt.setString(1, guild.getId());
            pstmt.setString(2, member.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            outputException(e);
        }
    }

    /**
     * Sets the initial level value.
     *
     * @param guild  {@link Guild} for the guild.
     * @param member {@link Member} for the user.
     */
    public static void addLevelToUser(Guild guild, Member member) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("INSERT OR IGNORE INTO TLEVEL(SERVERID, USERID, LEVEL) VALUES(?, ?, ?);")) {
            pstmt.setString(1, guild.getId());
            pstmt.setString(2, member.getId());
            pstmt.setInt(3, 0);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            outputException(e);
        }
    }

    /**
     * Sets the initial experience value.
     *
     * @param guild  {@link Guild} for the guild.
     * @param member {@link Member} for the user.
     */
    public static void addExperienceToUser(Guild guild, Member member) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("INSERT OR IGNORE INTO TEXPERIENCE(SERVERID, USERID, EXPERIENCE) VALUES(?, ?, ?);")) {
            pstmt.setString(1, guild.getId());
            pstmt.setString(2, member.getId());
            pstmt.setInt(3, 0);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            outputException(e);
        }
    }

    /**
     * Initialises all the roles from a guild or updates their data.
     *
     * @param guild {@link Guild} for the guild which roles should be considered.
     */
    public static void initRoles(Guild guild) {
        for (var role : guild.getRoles()) {
            updateRole(role);
            addRoleToServer(role, guild);
        }
    }

    /**
     * Updates the role data stored in the database of the given role.
     *
     * @param role {@link Role} for the role whose data should be updated.
     */
    public static void updateRole(Role role) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("REPLACE INTO TROLE(DISCORDID, NAME) VALUES (?, ?);")) {
            pstmt.setString(1, role.getId());
            pstmt.setString(2, role.getName());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            outputException(e);
        }
    }

    /**
     * Adds a role to a server.
     *
     * @param role  {@link Role} for the role.
     * @param guild {@link Guild} for the server/guild.
     */
    public static void addRoleToServer(Role role, Guild guild) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("REPLACE INTO TROLEONSERVER(SERVERID, ROLEID) VALUES (?, ?);")) {
            pstmt.setString(1, guild.getId());
            pstmt.setString(2, role.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            outputException(e);
        }
    }

    /**
     * Updates the guild data stored in the database of the given guild.
     *
     * @param guild {@link Guild} for the guild whose data should be updated.
     */
    public static void updateGuild(Guild guild) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("INSERT OR IGNORE INTO TSERVER(DISCORDID, NAME, OWNER, SERVERREGION, CMDPREFIX, RSSREPEAT, RSSCHANNEL, VOICELOGCHANNEL, CHANNELMUSICTEXT, CHANNELMUSICVOICE, CHANNELWELCOME) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);")) {
            pstmt.setString(1, guild.getId());
            pstmt.setString(2, guild.getName());
            pstmt.setString(3, guild.getOwnerId());
            pstmt.setString(4, guild.getRegionRaw());
            pstmt.setString(5, Settings.getInstance().getDefault_prefix());
            pstmt.setInt(6, Settings.getInstance().getDefault_rssdelay());
            pstmt.setString(7, guild.getTextChannels().get(0).getId());
            pstmt.setString(8, guild.getTextChannels().get(0).getId());
            pstmt.setString(9, guild.getTextChannels().get(0).getId());
            pstmt.setString(10, guild.getVoiceChannels().get(0).getId());
            pstmt.setString(11, guild.getTextChannels().get(0).getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            outputException(e);
        }
    }

    /**
     * Inserts a new normal chat message.
     *
     * @param event {@link MessageReceivedEvent} for the event of the message.
     */
    public static void insertNewChatMessage(MessageReceivedEvent event) {
        insertMessage(event.getGuild().getId(), event.getAuthor().getId(), event.getMessageId(), event.getChannel().getId(), "n", event.getMessage().getContentRaw(), "sent");
    }

    /**
     * Inserts a update of a normal chat message.
     *
     * @param event {@link MessageUpdateEvent} for the event of the message update.
     */
    public static void insertUpdateChatMessage(MessageUpdateEvent event) {
        insertMessage(event.getGuild().getId(), event.getAuthor().getId(), event.getMessageId(), event.getChannel().getId(), "n", event.getMessage().getContentRaw(), "update");
    }

    /**
     * Inserts an embed message.
     *
     * @param event {@link MessageEmbedEvent} for the event of the embed message.
     * @param text  String of the embed message.
     */
    public static void insertNewEmbedMessage(MessageEmbedEvent event, String text) {
        insertMessage(event.getGuild().getId(), "", event.getMessageId(), event.getChannel().getId(), "e", text, "sent");
    }

    /**
     * Inserts a message to the TCHATLOG table.
     *
     * @param guildId   Id of the guild.
     * @param userId    Id of the author of the message.
     * @param msgId     Id of the message.
     * @param channelId Id of the channel in which the message was sent.
     * @param type      Type of the message: 'n' for a normal one and 'e' for an embed.
     * @param text      String to the content of the message.
     * @param status    String of the status of the flag.
     */
    private static void insertMessage(String guildId, String userId, String msgId, String channelId, String type, String text, String status) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("INSERT INTO TCHATLOG(SERVERID, USERID, DISCORDID, CHANNEL, DATE, TIME, TYPE, MESSAGE, STATUS) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);")) {
            pstmt.setString(1, guildId);
            pstmt.setString(2, userId);
            pstmt.setString(3, msgId);
            pstmt.setString(4, channelId);
            pstmt.setString(5, TIME.getDate());
            pstmt.setString(6, TIME.getTimeSeconds());
            pstmt.setString(7, type);
            pstmt.setString(8, text);
            pstmt.setString(9, status);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            outputException(e);
        }
    }

    /**
     * Checks if a given phrase is on the blacklist of the given guild.
     *
     * @param guild  {@link Guild} for the guild.
     * @param phrase String for the phrase.
     * @return Returns if the phrase is listed on the blacklist of the given guild.
     */
    public static boolean isOnBlacklist(Guild guild, String phrase) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM TBLACKLIST WHERE SERVERID = ? AND PHRASE = ?;")) {
            pstmt.setString(1, guild.getId());
            pstmt.setString(2, phrase.trim());

            ResultSet rs = pstmt.executeQuery();

            return rs.getInt(0) > 0;
        } catch (SQLException e) {
            outputException(e);
        }
        return false;
    }

    /**
     * Adds a phrase to the blacklist of the given guild.
     *
     * @param guild  {@link Guild} for the guild.
     * @param phrase String to the phrase.
     */
    public static void addToBlacklist(Guild guild, String phrase) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("INSERT INTO TBLACKLIST(SERVERID, PHRASE) VALUES (?, ?);")) {
            pstmt.setString(1, guild.getId());
            pstmt.setString(2, phrase.trim());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            outputException(e);
        }
    }

    /**
     * Deletes a phrase from the blacklist of the given guild.
     *
     * @param guild  {@link Guild} for the guild.
     * @param phrase String to the phrase.
     */
    public static void deleteFromBlacklist(Guild guild, String phrase) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("DELETE FROM TBLACKLIST WHERE SERVERID = ? AND PHRASE = ?;")) {
            pstmt.setString(1, guild.getId());
            pstmt.setString(2, phrase.trim());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            outputException(e);
        }
    }

    /**
     * Gets the blacklist of the given guild.
     *
     * @param guild {@link Guild} for the guild.
     * @return Returns a list of all phrases of the blacklist.
     */
    public static List<String> getBlacklistForGuild(Guild guild) {
        List<String> result = new ArrayList<>();
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("SELECT PHRASE FROM TBLACKLIST WHERE SERVERID = ?;")) {
            pstmt.setString(1, guild.getId());

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                result.add(rs.getString("PHRASE"));
            }

        } catch (SQLException e) {
            outputException(e);
        }
        return result;
    }

    /**
     * Gets the RSS entries of the given guild.
     *
     * @param guild {@link Guild} for the guild.
     * @return Returns a map with the name of the RSS as key and with the link as the corresponding value.
     */
    public static Map<String, String> getRss(Guild guild) {
        Map<String, String> result = new LinkedHashMap<>();
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("SELECT NAME, LINK FROM TRSS WHERE SERVERID = ?;")) {
            pstmt.setString(1, guild.getId());

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                result.put(rs.getString("NAME"), rs.getString("LINK"));
            }

        } catch (SQLException e) {
            outputException(e);
        }
        return result;
    }

    /**
     * Gets the RSS repeat delay time for the given guild.
     *
     * @param guild {@link Guild} for the guild.
     * @return Returns the RSS repeat delay time.
     */
    public static Integer getRssRepeat(Guild guild) {
        Integer result = null;
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("SELECT RSSREPEAT FROM TSERVER WHERE DISCORDID = ?;")) {
            pstmt.setString(1, guild.getId());

            ResultSet rs = pstmt.executeQuery();

            result = rs.getInt("RSSREPEAT");
        } catch (SQLException e) {
            outputException(e);
        }
        return result;
    }

    /**
     * Adds a new RSS entry to the given guild.
     *
     * @param guild {@link Guild} for the guild.
     * @param name  String of the name of the RSS entry.
     * @param link  String for the link of the RSS entry.
     */
    public static void insertRSS(Guild guild, String name, String link) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("INSERT INTO TRSS(SERVERID, NAME, LINK) VALUES (?, ?, ?);")) {
            pstmt.setString(1, guild.getId());
            pstmt.setString(2, name);
            pstmt.setString(3, link);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            outputException(e);
        }
    }

    /**
     * Deletes a RSS entry from the given guild.
     *
     * @param guild {@link Guild} for the guild.
     * @param name  String for the name of the RSS entry.
     */
    public static void deleteRSS(Guild guild, String name) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("DELETE FROM TRSS WHERE SERVERID = ? AND NAME = ?;")) {
            pstmt.setString(1, guild.getId());
            pstmt.setString(2, name);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            outputException(e);
        }
    }

    /**
     * Checks if the given name of the RSS entry is on the given guild.
     *
     * @param guild {@link Guild} for the guild.
     * @param name  String for the name of the RSS entry.
     * @return Returns if the name is already a RSS entry.
     */
    public static boolean isInRSS(Guild guild, String name) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM TRSS WHERE SERVERID = ? AND NAME = ?")) {
            pstmt.setString(1, guild.getId());
            pstmt.setString(2, name);

            ResultSet rs = pstmt.executeQuery();

            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            outputException(e);
        }
        return false;
    }

    /**
     * Gets the given column from the given guild.
     *
     * @param guild   {@link Guild} for the guild.
     * @param channel String for the column of the channel in the database.
     * @return Returns the discord channel id of the requested channel.
     */
    private static String getColumn(Guild guild, String channel) {
        String result = null;
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(String.format("SELECT %s FROM TSERVER WHERE DISCORDID = ?;", channel))) {
            pstmt.setString(1, guild.getId());

            ResultSet rs = pstmt.executeQuery();
            result = rs.getString(channel);
        } catch (SQLException e) {
            outputException(e);
        }
        return result;
    }

    /**
     * Sets the given column from the given guild.
     *
     * @param guild  {@link Guild} for the guild.
     * @param column String for the column of the channel in the database.
     * @param value  String for the new value for the column.
     */
    private static void setColumn(Guild guild, String column, String value) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("UPDATE TSERVER SET " + column + " = ? WHERE DISCORDID = ?;")) {
            pstmt.setString(1, value);
            pstmt.setString(2, guild.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            outputException(e);
        }
    }

    /**
     * Gets the autoplay link of the given guild.
     *
     * @param guild {@link Guild} for the guild.
     * @return Return the autoplay link of the given guild.
     */
    public static String getAutoplaylink(Guild guild) {
        return getColumn(guild, "AUTOPLAYLINK");
    }

    /**
     * Sets a new autoplay link for the given guild.
     *
     * @param guild {@link Guild} for the guild.
     * @param link  String for the new link.
     */
    public static void setAutoplaylink(Guild guild, String link) {
        setColumn(guild, "AUTOPLAYLINK", link);
    }

    /**
     * Gets the RSS channel of the given guild.
     *
     * @param guild {@link Guild} for the guild
     * @return Returns the discord channel id of the RSS channel.
     */
    public static String getRSSChannel(Guild guild) {
        return getColumn(guild, "RSSCHANNEL");
    }

    /**
     * Sets a new RSS channel for the given guild.
     *
     * @param guild   {@link Guild} for the guild.
     * @param channel String for the new channel.
     */
    public static void setRSSChannel(Guild guild, String channel) {
        setColumn(guild, "RSSCHANNEL", channel);
    }

    /**
     * Gets the RSS-Log of the given guild.
     *
     * @param guild {@link Guild} for the guild.
     * @return Returns a list of all logged RSS messages.
     */
    public static boolean checkRSSLog(Guild guild, String name, String message) {
        List<String> result = new ArrayList<>();
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("SELECT MESSAGE FROM TRSSLOG WHERE SERVERID = ? AND RSSNAME = ? AND MESSAGE = ?;")) {
            pstmt.setString(1, guild.getId());
            pstmt.setString(2, name);
            pstmt.setString(3, message);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                result.add(rs.getString("MESSAGE"));
            }
        } catch (SQLException e) {
            outputException(e);
        }
        return result.isEmpty();
    }

    /**
     * Inserts a RSS message to the RSS-Log.
     *
     * @param guild   {@link Guild} for the guild.
     * @param name    String for the name of the RSS entry.
     * @param message String for the message of the RSS entry.
     */
    public static void insertRSSLog(Guild guild, String name, String message) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("INSERT INTO TRSSLOG(RSSNAME, SERVERID, MESSAGE ) VALUES (?, ?, ?);")) {
            pstmt.setString(1, name);
            pstmt.setString(2, guild.getId());
            pstmt.setString(3, message);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            outputException(e);
        }
    }

    /**
     * Gets all link parts.
     *
     * @return Returns a list with all linkparts.
     */
    public static List<String> getLinkparts() {
        List<String> result = new ArrayList<>();
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("SELECT EXPRESSION FROM TLINKS")) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                result.add(rs.getString("EXPRESSION"));
            }
        } catch (SQLException e) {
            outputException(e);
        }
        return result;
    }


    /**
     * Gets the command prefix of the given guild.
     *
     * @param guild {@link Guild} for the guild.
     * @return Returns the command prefix of the given guild.
     */
    public static String getCmdPrefix(Guild guild) {
        return getColumn(guild, "CMDPREFIX");
    }

    /**
     * Sets a new command prefix for the given guild.
     *
     * @param guild   {@link Guild} for the guild.
     * @param channel String for the new prefix.
     */
    public static void setCmdPrefix(Guild guild, String channel) {
        setColumn(guild, "CMDPREFIX", channel);
    }


    /**
     * Gets the MusicVoice channel of the given guild.
     *
     * @param guild {@link Guild} for the guild.
     * @return Returns the discord channel id for the music-voice-channel of the given guild.
     */
    public static String getChannelMusicVoice(Guild guild) {
        return getColumn(guild, "CHANNELMUSICVOICE");
    }

    /**
     * Sets a new MusicVoice channel of the given guild.
     *
     * @param guild   {@link Guild} for the guild.
     * @param channel String for the new channel.
     */
    public static void setChannelMusicVoice(Guild guild, String channel) {
        setColumn(guild, "CHANNELMUSICVOICE", channel);
    }

    /**
     * Gets the MusicText channel of the given guild.
     *
     * @param guild {@link Guild} for the guild.
     * @return Returns the discord channel id for the music-text-channel of the given guild.
     */
    public static String getChannelMusicText(Guild guild) {
        return getColumn(guild, "CHANNELMUSICTEXT");
    }

    /**
     * Sets a new MusicText channel for the given guild.
     *
     * @param guild   {@link Guild} for the guild.
     * @param channel String for the new channel.
     */
    public static void setChannelMusicText(Guild guild, String channel) {
        setColumn(guild, "CHANNELMUSICTEXT", channel);
    }

    /**
     * Gets the welcome channel of the given guild.
     *
     * @param guild {@link Guild} for the guild.
     * @return Returns the discord channel id for the welcome-channel of the given guild.
     */
    public static String getChannelWelcome(Guild guild) {
        return getColumn(guild, "CHANNELWELCOME");
    }

    /**
     * Sets a new welcome channel for the given guild.
     *
     * @param guild   {@link Guild} for the guild.
     * @param channel String for the new channel.
     */
    public static void setChannelWelcome(Guild guild, String channel) {
        setColumn(guild, "CHANNELWELCOME", channel);
    }

    /**
     * Gets the voicelog channel of the given guild.
     *
     * @param guild {@link Guild} for the guild.
     * @return Returns the discord channel id for the voicelog channel of the given guild.
     */
    public static String getVoiceLogChannel(Guild guild) {
        return getColumn(guild, "VOICELOGCHANNEL");
    }

    /**
     * Sets a new voicelog channel for the given guild.
     *
     * @param guild   {@link Guild} for the guild.
     * @param channel String for the new channel.
     */
    public static void setVoiceLogChannel(Guild guild, String channel) {
        setColumn(guild, "VOICELOGCHANNEL", channel);
    }

    /**
     * Gets if the voicelog should be shown.
     *
     * @param guild {@link Guild} for the guild.
     * @return Returns if the voicelog should be shown.
     */
    public static boolean showVoicelog(Guild guild) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("SELECT SHOWVOICELOG FROM TSERVER WHERE DISCORDID = ?;")) {
            pstmt.setString(1, guild.getId());

            ResultSet rs = pstmt.executeQuery();

            return rs.getBoolean(0);
        } catch (SQLException e) {
            outputException(e);
        }
        return false;
    }

    /**
     * Sets if voicelog should be shown in given guild.
     *
     * @param guild {@link Guild} for the guild.
     * @param value Boolean value for the topic.
     */
    public static void setShowVoicelog(Guild guild, boolean value) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("UPDATE TSERVER SET SHOWVOICELOG = ? WHERE DISCORDID = ?;")) {
            pstmt.setBoolean(1, value);
            pstmt.setString(2, guild.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            outputException(e);
        }
    }

    /**
     * Sets if music should automatically play in given guild.
     *
     * @param guild {@link Guild} for the guild.
     * @param value Boolean value for the topic.
     */
    public static void setMusicAutoplay(Guild guild, boolean value) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("UPDATE TSERVER SET MUSICAUTOPLAY = ? WHERE DISCORDID = ?;")) {
            pstmt.setBoolean(1, value);
            pstmt.setString(2, guild.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            outputException(e);
        }
    }

    /**
     * Adds a command the given guild.
     *
     * @param guild   {@link Guild} for the guild.
     * @param name    String for the name/handle of the command.
     * @param message String for the message of the command.
     */
    public static void addCommand(Guild guild, String name, String message) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("INSERT INTO TCOMMAND(SERVERID, NAME, MESSAGE) VALUES (?, ?, ?);")) {
            pstmt.setString(1, guild.getId());
            pstmt.setString(2, name);
            pstmt.setString(3, message);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            outputException(e);
        }
    }

    /**
     * Deletes a command from the given guild.
     *
     * @param guild {@link Guild} for the guild.
     * @param name  String for the name/handle of the command.
     */
    public static void deleteCommand(Guild guild, String name) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("DELETE FROM TCOMMAND WHERE SERVERID = ? AND NAME = ?;")) {
            pstmt.setString(1, guild.getId());
            pstmt.setString(2, name);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            outputException(e);
        }
    }

    /**
     * Gets a map of all commands of the given guild.
     *
     * @param guild {@link Guild} for the guild.
     * @return Returns a map with the command name as key and with the command message as value.
     */
    public static Map<String, String> getCommands(Guild guild) {
        Map<String, String> result = new LinkedHashMap<>();
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("SELECT NAME, MESSAGE FROM TCOMMAND WHERE SERVERID = ?")) {
            pstmt.setString(1, guild.getId());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                result.put(rs.getString("NAME"), rs.getString("MESSAGE"));
            }
        } catch (SQLException e) {
            outputException(e);
        }
        return result;
    }

    /**
     * Checks if the given name already exists as a command on the given guild.
     *
     * @param guild {@link Guild} for the guild.
     * @param name  String the name/handle of the command.
     * @return Returns if the given name already exists as a command on the given guild.
     */
    public static boolean isNotCommand(Guild guild, String name) {
        return !getCommands(guild).containsValue(name);
    }

    /**
     * Adds a new permission the given guild.
     *
     * @param guild {@link Guild} for the guild.
     * @param name  String for the name of the permission.
     */
    public static void addPermission(Guild guild, String name) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("INSERT INTO TPERMISSION(SERVERID, NAME) VALUES (?, ?);")) {
            pstmt.setString(1, guild.getId());
            pstmt.setString(2, name);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            outputException(e);
        }
    }

    /**
     * Removes the given permission from the guild.
     *
     * @param guild {@link Guild} for the guild.
     * @param name  String for the name of the permission.
     */
    public static void removePermission(Guild guild, String name) {
        // taking away permission from all roles
        for (var role : guild.getRoles()) {
            removePermissionRole(guild, name, role);
        }

        // taking away permission from all users
        for (var member : guild.getMembers()) {
            removePermissionUser(guild, name, member);
        }

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("DELETE FROM TPERMISSION WHERE SERVERID = ? AND NAME = ?")) {
            pstmt.setString(1, guild.getId());
            pstmt.setString(2, name);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            outputException(e);
        }
    }

    /**
     * Checks if the given permission exists.
     *
     * @param guild {@link Guild} for the guild.
     * @param name  String for the name of the permission.
     * @return Returns if the given permission exists.
     */
    public static boolean checkPermission(Guild guild, String name) {
        List<String> result = new ArrayList<>();

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("SELECT NAME FROM TPERMISSION WHERE SERVERID = ? AND NAME = ?")) {
            pstmt.setString(1, guild.getId());
            pstmt.setString(2, name);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                result.add(rs.getString("NAME"));
            }
        } catch (SQLException e) {
            outputException(e);
        }
        return !result.isEmpty();
    }

    /**
     * Adds a permission to a role.
     *
     * @param guild {@link Guild} for the guild.
     * @param name  String for the name of the permission.
     * @param role  {@link Role} for the role.
     */
    public static void addPermissionRole(Guild guild, String name, Role role) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("INSERT INTO TPERMISSIONROLE(PERMISSIONID, ROLEID) VALUES ((SELECT ID FROM TPERMISSION WHERE SERVERID = ? AND NAME = ? LIMIT 1), ?);")) {
            pstmt.setString(1, guild.getId());
            pstmt.setString(2, name);
            pstmt.setString(3, role.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            outputException(e);
        }
    }

    /**
     * Removes a permission from the given role.
     *
     * @param guild {@link Guild} for the guild.
     * @param name  String for the name of the permission.
     * @param role  {@link Role} for the role.
     */
    public static void removePermissionRole(Guild guild, String name, Role role) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("DELETE FROM TPERMISSIONROLE WHERE PERMISSIONID = (SELECT ID FROM TPERMISSION WHERE SERVERID = ? AND NAME = ? LIMIT 1) AND ROLEID = ?;")) {
            pstmt.setString(1, guild.getId());
            pstmt.setString(2, name);
            pstmt.setString(3, role.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            outputException(e);
        }
    }

    /**
     * Adds a permission to a user.
     *
     * @param guild  {@link Guild} for the guild.
     * @param name   String for the name of the permission.
     * @param member {@link Member} for the user.
     */
    public static void addPermissionUser(Guild guild, String name, Member member) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("INSERT INTO TPERMISSIONUSER(PERMISSIONID, USERID) VALUES ((SELECT ID FROM TPERMISSION WHERE SERVERID = ? AND NAME = ? LIMIT 1), ?);")) {
            pstmt.setString(1, guild.getId());
            pstmt.setString(2, name);
            pstmt.setString(3, member.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            outputException(e);
        }
    }

    /**
     * Removes a permission from the given user.
     *
     * @param guild  {@link Guild} for the guild.
     * @param name   String for the name of the permission.
     * @param member {@link Member} for the user.
     */
    public static void removePermissionUser(Guild guild, String name, Member member) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("DELETE FROM TPERMISSIONUSER WHERE PERMISSIONID = (SELECT ID FROM TPERMISSION WHERE SERVERID = ? AND NAME = ? LIMIT 1) AND USERID = ?;")) {
            pstmt.setString(1, guild.getId());
            pstmt.setString(2, name);
            pstmt.setString(3, member.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            outputException(e);
        }
    }

    /**
     * Get all roles with the given permission.
     *
     * @param guild {@link Guild} for the guild.
     * @param name  String for the name of the permission.
     * @return Returns a list of all role ids with the given permission.
     */
    public static List<String> getPermissionRoles(Guild guild, String name) {
        List<String> result = new ArrayList<>();
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("SELECT pr.ROLEID FROM TPERMISSION p, TPERMISSIONROLE pr WHERE  p.SERVERID = ? AND p.NAME = ? AND p.ID = pr.PERMISSIONID;")) {
            pstmt.setString(1, guild.getId());
            pstmt.setString(2, name);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                result.add(rs.getString("ROLEID"));
            }
        } catch (SQLException e) {
            outputException(e);
        }
        return result;
    }

    /**
     * Get all users with the given permission.
     *
     * @param guild {@link Guild} for the guild.
     * @param name  String for the name of the permission.
     * @return Returns a list fo all user ids with the given permission.
     */
    public static List<String> getPermissionUser(Guild guild, String name) {
        List<String> result = new ArrayList<>();
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("SELECT pr.USERID FROM TPERMISSION p, TPERMISSIONUSER pr WHERE  p.SERVERID = ? AND p.NAME = ? AND p.ID = pr.PERMISSIONID;")) {
            pstmt.setString(1, guild.getId());
            pstmt.setString(2, name);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                result.add(rs.getString("USERID"));
            }
        } catch (SQLException e) {
            outputException(e);
        }
        return result;
    }

    /**
     * Checks if the author of the event has role which is allowed.
     *
     * @param event {@link MessageReceivedEvent} for the event.
     * @param name  String for the name of the permission.
     * @return Returns if the author the event has the permission.
     */
    public static boolean isRoleAllowed(MessageReceivedEvent event, String name) {
        List<String> roles = getPermissionRoles(event.getGuild(), name);
        for (String s : roles) {
            for (Role r : Objects.requireNonNull(event.getMember()).getRoles()) {
                if (r.getName().equals(s)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the author of the event is allowed.
     *
     * @param event {@link MessageReceivedEvent} for the event.
     * @param name  String for the name of the permission.
     * @return Returns if the author of the event has the permission.
     */
    public static boolean isUserAllowed(MessageReceivedEvent event, String name) {
        return getPermissionUser(event.getGuild(), name).contains(event.getAuthor().getId());
    }

    /**
     * Checks if everybody is allowed.
     *
     * @param event {@link MessageReceivedEvent} for the event.
     * @param name  String for the name of the permission.
     * @return Returns if everybody is allowed.
     */
    public static boolean isEverybodyAllowed(MessageReceivedEvent event, String name) {
        return getPermissionUser(event.getGuild(), name).isEmpty() && getPermissionRoles(event.getGuild(), name).isEmpty();
    }

    /**
     * Checks if the author of the event is allowed to perform an action which requires the given permission.
     *
     * @param event {@link MessageReceivedEvent} for the event.
     * @param name  String for the name of the permission.
     * @return Returns if the author of the event has the permission.
     */
    public static boolean isAllowed(MessageReceivedEvent event, String name) {
        return isRoleAllowed(event, name) || isUserAllowed(event, name) || isEverybodyAllowed(event, name);
    }

    /**
     * Updates the RSS repeat value.
     *
     * @param guild {@link Guild} for the guild.
     * @param value Integer value for the new repeat value.
     */
    public static void updateRSSRepeat(Guild guild, int value) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("UPDATE TSERVER SET RSSREPEAT = ? WHERE DISCORDID = ?;")) {
            pstmt.setInt(1, value);
            pstmt.setString(2, guild.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            outputException(e);
        }
    }

    /**
     * Gets the current infotext of the given guild.
     *
     * @param guild {@link Guild} for the guild.
     * @return Returns the infotext of the given guild.
     */
    public static String getInfotext(Guild guild) {
        String result = "";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("SELECT INFOTEXT FROM TSERVER WHERE DISCORDID = ?")) {
            pstmt.setString(1, guild.getId());

            ResultSet rs = pstmt.executeQuery();
            result = rs.getString("INFOTEXT");
        } catch (SQLException e) {
            outputException(e);
        }
        return result;
    }

    /**
     * Sets a new infotext for the given guild.
     *
     * @param guild {@link Guild} for the guild.
     * @param text  String for the new infotext.
     */
    public static void setInfotext(Guild guild, String text) {
        setColumn(guild, "INFOTEXT", text);
    }

    /**
     * Sets the given experience to the author of the event.
     *
     * @param event {@link MessageReceivedEvent} for the event.
     * @param exp   Integer value of the experience which should be set.
     */
    public static void setExperience(MessageReceivedEvent event, int exp) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("REPLACE INTO TEXPERIENCE(SERVERID, USERID, EXPERIENCE) VALUES (?, ?, ?);")) {
            pstmt.setString(1, event.getGuild().getId());
            pstmt.setString(2, event.getAuthor().getId());
            pstmt.setInt(3, exp);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            outputException(e);
        }
    }

    /**
     * Gets the experience of the author of the event.
     *
     * @param event {@link MessageReceivedEvent} for the event.
     * @return Returns the experience of the author of the event.
     */
    public static Integer getExperience(MessageReceivedEvent event) {
        Integer result = null;
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("SELECT EXPERIENCE FROM TEXPERIENCE WHERE SERVERID = ? AND USERID = ?;")) {
            pstmt.setString(1, event.getGuild().getId());
            pstmt.setString(2, event.getAuthor().getId());

            ResultSet rs = pstmt.executeQuery();
            result = rs.getInt("EXPERIENCE");
        } catch (SQLException e) {
            outputException(e);
        }
        return result;
    }

    /**
     * Sets the given level to the author of the event.
     *
     * @param event {@link MessageReceivedEvent} for the event.
     * @param level Integer value for the level which should be set.
     */
    public static void setLevel(MessageReceivedEvent event, int level) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("REPLACE INTO TLEVEL(SERVERID, USERID, LEVEL) VALUES (?, ?, ?);")) {
            pstmt.setString(1, event.getGuild().getId());
            pstmt.setString(2, event.getAuthor().getId());
            pstmt.setInt(3, level);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            outputException(e);
        }
    }

    /**
     * Gets the level of the author of the event.
     *
     * @param event {@link MessageReceivedEvent} for the event.
     * @return Return the level of the author of the event.
     */
    public static Integer getLevel(MessageReceivedEvent event) {
        Integer result = null;
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("SELECT LEVEL FROM TLEVEL WHERE SERVERID = ? AND USERID = ?;")) {
            pstmt.setString(1, event.getGuild().getId());
            pstmt.setString(2, event.getAuthor().getId());

            ResultSet rs = pstmt.executeQuery();
            result = rs.getInt("LEVEL");
        } catch (SQLException e) {
            outputException(e);
        }
        return result;
    }
}
