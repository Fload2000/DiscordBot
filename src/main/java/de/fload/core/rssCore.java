package de.fload.core;

import de.fload.atomCore.ATOMFeedParser;
import de.fload.database.DatabaseAction;
import de.fload.rssCore.Feed;
import de.fload.rssCore.FeedMessage;
import de.fload.rssCore.RSSFeedParser;
import de.fload.util.FUNCTION;
import net.dv8tion.jda.api.entities.TextChannel;
import java.util.ArrayList;

import static de.fload.database.DatabaseAction.checkRSSLog;
import static de.fload.util.FUNCTION.outputException;

public class rssCore implements Runnable {
    private String name;
    private TextChannel rssChannel;
    private String link;

    public rssCore(String name, TextChannel rssChannel, String link) {
        this.name = name;
        this.rssChannel = rssChannel;
        this.link = link;
    }

    public void run() {
        RSSFeedParser parser = new RSSFeedParser(link);

        try {
            Feed feed = parser.readFeed();

            try {
                for (FeedMessage message : feed.getMessages()) {
                    String m = "**" + message.getTitle() + "**: *" + message.getDescription() + "*  " + message.getLink();
                    if(checkRSSLog(rssChannel.getGuild(), name, m)) {
                        rssChannel.sendMessage(m).queue();
                        DatabaseAction.insertRSSLog(rssChannel.getGuild(), name,  m);
                        FUNCTION.pause(500);
                    }
                }
            } catch (Exception e) {
                try {
                    ArrayList<String> atom = ATOMFeedParser.parse(link);
                    for (String m : atom) {
                        if(checkRSSLog(rssChannel.getGuild(), name, m)) {
                            rssChannel.sendMessage(m).queue();
                            DatabaseAction.insertRSSLog(rssChannel.getGuild(), name,  m);
                            FUNCTION.pause(500);
                        }
                    }
                } catch (Exception ex) {
                    outputException(ex);
                }
            }
        } catch (Exception e) {
            outputException(e);
        }
    }
}
