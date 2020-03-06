package de.fload.core;

import de.fload.util.FUNCTION;
import net.dv8tion.jda.api.JDA;
import org.json.simple.JSONObject;

/**
 * Settings class - singleton for storing preferences of the program.
 */
public class Settings {

    private static Settings instance;

    // jda
    private JDA jda;

    // general
    private String discordToken;
    private boolean discordTokenEncrypted;
    private String owaAPIKey;
    private int default_rssdelay;
    private String default_prefix;

    // paths
    private String dbPath;

    // email
    private String smtpHost;
    private String smtpPort;
    private String fromEmail;
    private String password;
    private String toEmail;

    private Settings() { }

    /**
     * Getter for the instance of this class. If the instance is  {@code null} a new one will be created.
     *
     * @return Returns the instance of this class.
     */
    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    public void load() {
        JSONObject object = FUNCTION.getData();

        // general
        JSONObject general = (JSONObject) object.get("general");
        discordToken = general.get("token").toString();
        discordTokenEncrypted = Boolean.parseBoolean(general.get("token_encrypted").toString());
        owaAPIKey = general.get("owa-apikey").toString();
        default_prefix = general.get("default_prefix").toString();

        var rssdelayOptional = FUNCTION.getInteger(general.get("default_rssdelay").toString());
        rssdelayOptional.ifPresent(integer -> default_rssdelay = integer);

        // paths
        JSONObject paths = (JSONObject) object.get("paths");
        dbPath = paths.get("database").toString();

        // email
        JSONObject email = (JSONObject) object.get("email");
        smtpHost = email.get("smtpHost").toString();
        smtpPort = email.get("smtpPort").toString();
        fromEmail = email.get("fromEmail").toString();
        password = email.get("password").toString();
        toEmail = email.get("toEmail").toString();
    }

    public JDA getJda() {
        return jda;
    }

    public void setJda(JDA jda) {
        this.jda = jda;
    }

    public String getDbPath() {
        return dbPath;
    }

    public void setDbPath(String dbPath) {
        this.dbPath = dbPath;
    }

    public String getDiscordToken() {
        return discordToken;
    }

    public void setDiscordToken(String discordToken) {
        this.discordToken = discordToken;
    }

    public boolean isDiscordTokenEncrypted() {
        return discordTokenEncrypted;
    }

    public void setDiscordTokenEncrypted(boolean discordTokenEncrypted) {
        this.discordTokenEncrypted = discordTokenEncrypted;
    }

    public String getOwaAPIKey() {
        return owaAPIKey;
    }

    public void setOwaAPIKey(String owaAPIKey) {
        this.owaAPIKey = owaAPIKey;
    }

    public int getDefault_rssdelay() {
        return default_rssdelay;
    }

    public void setDefault_rssdelay(int default_rssdelay) {
        this.default_rssdelay = default_rssdelay;
    }

    public String getDefault_prefix() {
        return default_prefix;
    }

    public void setDefault_prefix(String default_prefix) {
        this.default_prefix = default_prefix;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }
}
