package de.fload.jobs;

import de.fload.core.Settings;
import de.fload.core.rssCore;
import de.fload.database.DatabaseAction;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Map;

/**
 * jobRSS class
 */
public class jobRSS implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JDA jda = Settings.getInstance().getJda();
        String guildId = jobExecutionContext.getJobDetail().getKey().getName();
        Guild guild = jda.getGuildById(guildId);
        if (guild != null) {
            Map<String, String> rssData = DatabaseAction.getRss(guild);
            TextChannel rssChannel = jda.getTextChannelById(DatabaseAction.getRSSChannel(guild));
            if(rssChannel != null) {
                for (var rss : rssData.keySet()) {
                    new Thread(new rssCore(rss, rssChannel, rssData.get(rss))).start();
                }
            }
        }
    }
}
