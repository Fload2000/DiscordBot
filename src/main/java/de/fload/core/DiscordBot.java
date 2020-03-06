package de.fload.core;

import de.fload.commands.*;
import de.fload.database.DatabaseAction;
import de.fload.jobs.jobRSS;
import de.fload.listeners.*;
import de.fload.listeners.channel.category.categoryListener;
import de.fload.listeners.channel.category.categoryupdateListener;
import de.fload.listeners.channel.text.textListener;
import de.fload.listeners.channel.text.textupdateListener;
import de.fload.listeners.channel.voice.voicechannelListener;
import de.fload.listeners.channel.voice.voicechannelupdateListener;
import de.fload.listeners.emote.emoteListener;
import de.fload.listeners.emote.emoteupdateListener;
import de.fload.listeners.guild.guildListener;
import de.fload.listeners.guild.memberListener;
import de.fload.listeners.guild.updateListener;
import de.fload.listeners.guild.voiceListener;
import de.fload.listeners.message.messageListener;
import de.fload.util.Encryptor;
import de.fload.util.TIME;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import org.jetbrains.annotations.NotNull;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.LinkedHashMap;
import java.util.Map;

import static de.fload.util.FUNCTION.outputException;
import static de.fload.util.FUNCTION.pause;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * DiscordBot Class
 */
public class DiscordBot {

    private final static Logger logger = LoggerFactory.getLogger(DiscordBot.class);

    private static JDABuilder builder;
    private static JDA jda;

    /**
     * CTOR
     */
    public DiscordBot() {
        // do nothing
    }

    public static void init() {
        logger.info(TIME.getTimestamp() + "Initialize the Discord-Bot");
        builder = new JDABuilder(AccountType.BOT);
        builder.setToken(getToken(Settings.getInstance().getDiscordToken()));
        builder.setAutoReconnect(true);
        builder.setStatus(OnlineStatus.ONLINE);
        addCommands();
        addListeners();
        startBot();
        Settings.getInstance().setJda(jda);
    }

    /**
     * Extracts the token.
     *
     * @return Returns the token.
     */
    private static String getToken(@NotNull String token) {
        if (Settings.getInstance().isDiscordTokenEncrypted()) {
            try {
                token = Encryptor.decrypt(token);
            } catch (Exception e) {
                outputException(e);
            }
        }
        return token;
    }

    /**
     * Launches the bot
     */
    public static void startBot() {
        logger.info(TIME.getTimestamp() + "The Discord-Bot starts...");
        try {
            jda = builder.build();
            pause(5000);
            startJobs();
        } catch (LoginException e) {
            outputException(e);
        }
    }

    /**
     * Restarts the bot
     */
    public static void restartBot() {
        stopBot();
        pause(5000);
        startBot();
    }

    /**
     * Stops the bot
     */
    public static void stopBot() {
        logger.info(TIME.getTimestamp() + "The Discord Bot stops...");
        jda.shutdownNow();
        System.exit(0);
    }

    /**
     * Add all the listener classes to the builder
     */
    private static void addListeners() {
        builder.addEventListeners(new readyListener());

        builder.addEventListeners(new CommandListener());

        builder.addEventListeners(new voiceListener());
        builder.addEventListeners(new updateListener());
        builder.addEventListeners(new memberListener());
        builder.addEventListeners(new guildListener());

        builder.addEventListeners(new emoteListener());
        builder.addEventListeners(new emoteupdateListener());

        builder.addEventListeners(new categoryListener());
        builder.addEventListeners(new categoryupdateListener());
        builder.addEventListeners(new textListener());
        builder.addEventListeners(new textupdateListener());
        builder.addEventListeners(new voicechannelListener());
        builder.addEventListeners(new voicechannelupdateListener());

        builder.addEventListeners(new messageListener());

        builder.addEventListeners(new blacklistListener());

        builder.addEventListeners(new linkListener());

        builder.addEventListeners(new musicListener());

        builder.addEventListeners(new levelListener());
    }

    /**
     * Add all the command classes to the commandHandler.
     */
    private static void addCommands() {
        commandHandler.commands.put("ping", new cmdPing());
        commandHandler.commands.put("weather", new cmdWeather());
        commandHandler.commands.put("time", new cmdTime());
        commandHandler.commands.put("help", new cmdHelp());
        commandHandler.commands.put("hello", new cmdHello());
        commandHandler.commands.put("clear", new cmdClear());
        commandHandler.commands.put("music", new cmdMusic());
        commandHandler.commands.put("blacklist", new cmdBlacklist());
        commandHandler.commands.put("rss", new cmdRSS());
        commandHandler.commands.put("server", new cmdServer());
        commandHandler.commands.put("command", new cmdCommand());
        commandHandler.commands.put("bot", new cmdBot());
        commandHandler.commands.put("permission", new cmdPermission());
        commandHandler.commands.put("raffle", new cmdRaffle());
        commandHandler.commands.put("info", new cmdInfo());
        commandHandler.commands.put("debug", new cmdDebug());
        commandHandler.commands.put("level", new cmdLevel());
    }

    /**
     * Starts the jobs.
     */
    private static void startJobs() {
        Map<JobDetail, Trigger> jobs = new LinkedHashMap<>();

        for (var g : jda.getGuilds()) {
            JobDetail job = newJob(jobRSS.class)
                    .withIdentity(g.getId(), "rssJobs")
                    .build();
            Trigger trigger = newTrigger()
                    .withIdentity(g.getId(), "rssTrigger")
                    .startNow()
                    .withSchedule(simpleSchedule()
                            .withIntervalInMinutes(DatabaseAction.getRssRepeat(g))
                            .repeatForever())
                    .build();
            jobs.put(job, trigger);
        }

        try {
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();

            Scheduler scheduler = schedulerFactory.getScheduler();

            scheduler.start();

            for (var job : jobs.keySet()) {
                scheduler.scheduleJob(job, jobs.get(job));
            }

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
