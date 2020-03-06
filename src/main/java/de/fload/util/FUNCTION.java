package de.fload.util;

import com.google.common.base.Preconditions;
import de.fload.database.DatabaseAction;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.FileReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static de.fload.util.mail.Email.sendError;

public class FUNCTION {
    private final static Logger logger = LoggerFactory.getLogger(FUNCTION.class);

    /**
     * Parses an {@link String} to an {@link Integer}. Returns {@code null} if
     * string is not numeric.
     *
     * @param input String which should be parsed.
     * @return Returns a {@link Optional}. If the {@link String} could be parsed
     * this will contain the {@link Integer} value otherwise is will
     * empty.
     * @see Optional
     */
    public static Optional<Integer> getInteger(final String input) {
        Preconditions.checkNotNull(input);

        Optional<Integer> out = Optional.empty();
        try {
            out = Optional.of(Integer.valueOf(input));
        } catch (NumberFormatException ex) {
            outputException(ex);
        }
        return out;
    }

    public static void pause(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            outputException(e);
        }
    }

    @NotNull
    public static ArrayList<String> splitString(@NotNull String input) {
        ArrayList<String> temp = new ArrayList<>();
        int latest = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == ' ') {
                temp.add(input.substring(latest, i));
                latest = i + 1;
            }
        }
        temp.add(input.substring(latest));

        return temp;
    }

    public static JSONObject getData() {
        JSONObject object = new JSONObject();
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(("config/settings.json")));
            object = (JSONObject) obj;
        } catch (java.io.IOException | ParseException e) {
            outputException(e);
        }
        return object;
    }

    @NotNull
    public static String getFavicon(String url) {
        Pattern p = Pattern.compile("(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b");
        Matcher m = p.matcher(url);
        String domain = "";
        while (m.find()) {
            domain = m.group();
        }
        return "https://www.google.com/s2/favicons?domain=" + domain;
    }

    public static void printError(@NotNull MessageReceivedEvent event, String errormessage) {
        event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.RED).setDescription(":warning: " + errormessage + " :warning:").build()).queue();
    }

    public static void printWarning(@NotNull MessageReceivedEvent event, String message) {
        event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.YELLOW).setDescription(":warning: " + message + " :warning:").build()).queue();
    }

    public static void printError(MessageReceivedEvent event, JSONObject object, String key) {
        try {
            event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.RED).setDescription(":warning: " + object.get(key).toString() + " :warning:").build()).queue();
        } catch (Exception e) {
            printError(event, "ERROR-MESSAGE FAULT - Not a valid JSON-key");
            outputException(e);
        }
    }

    public static void printOk(@NotNull MessageReceivedEvent event, String message) {
        event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.GREEN).setDescription(":white_check_mark:" + message + ":white_check_mark:").build()).queue();
    }

    public static void printInfo(@NotNull MessageReceivedEvent event, String message) {
        event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.decode("#3b88c3")).setDescription(":information_source: " + message + " :information_source:").build()).queue();
    }

    public static void printInfo(MessageReceivedEvent event, JSONObject object, String key) {
        try {
            event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.decode("#3b88c3")).setDescription(":information_source: " + object.get(key).toString() + " :information_source:").build()).queue();
        } catch (Exception e) {
            printError(event, "INFO-MESSAGE FAULT - Not a valid JSON-key");
            outputException(e);
        }
    }

    public static void outputException(@NotNull Throwable e) {
        e.printStackTrace();
        sendError(e.getMessage(), ExceptionUtils.getStackTrace(e));
    }

    public static void openLink(String link) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(link));
            } catch (Exception e) {
                outputException(e);
            }
        }
    }

    public static void cmdMsg(@NotNull MessageReceivedEvent event, String cmd) {
        logger.info(TIME.getTimestamp() + "[" + event.getGuild().getName() + "][" + event.getMessage().getTextChannel().getName() + "] Command '" + DatabaseAction.getCmdPrefix(event.getGuild()) + cmd + "' was executed by " + event.getMessage().getAuthor().getName() + "#" + event.getMessage().getAuthor().getDiscriminator() + " (ID: " + event.getMessage().getAuthor().getId() + ")");
    }

    public static void expMsg(@NotNull MessageReceivedEvent event, int exp) {
        logger.info(TIME.getTimestamp() + "[" + event.getGuild().getName() + "][" + event.getMessage().getTextChannel().getName() + "] " + event.getMessage().getAuthor().getName() + "#" + event.getMessage().getAuthor().getDiscriminator() + " (ID: " + event.getMessage().getAuthor().getId() + ") gain " + exp + "xp");
    }

    public static void lvlMsg(MessageReceivedEvent event, int lvl) {
        logger.info(TIME.getTimestamp() + "[" + event.getGuild().getName() + "][" + event.getMessage().getTextChannel().getName() + "] " + event.getMessage().getAuthor().getName() + "#" + event.getMessage().getAuthor().getDiscriminator() + " (ID: " + event.getMessage().getAuthor().getId() + ") is now level " + lvl);
    }

    public static long getDifference(String date1, String date2) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        long diff = -1;

        try {
            Date d1 = format.parse(date1);
            Date d2 = format.parse(date2);
            diff = (d2.getTime() - d1.getTime());
        } catch (Exception e) {
            outputException(e);
        }
        return diff;
    }
}
