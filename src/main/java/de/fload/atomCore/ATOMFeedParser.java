package de.fload.atomCore;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndLink;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ArrayList;

import static de.fload.util.FUNCTION.outputException;

public class ATOMFeedParser {
    @NotNull
    public static ArrayList<String> parse(String url) {
        ArrayList<String> output = new ArrayList<>();
        try {
            URL feedUrl = new URL(url);

            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));

            for (var entry : feed.getEntries()) {
                StringBuilder help = new StringBuilder("**" + ((SyndEntry) entry).getTitle() + "**");

                for (var link : ((SyndEntry) entry).getLinks()) {
                    help.append(" ").append(((SyndLink) link).getHref());
                }
                output.add(help.toString());
            }
        } catch (Exception e) {
            outputException(e);
        }
        return output;
    }
}
