package de.fload.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static de.fload.database.DatabaseAction.*;
import static de.fload.util.FUNCTION.*;

public class cmdServer implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (!isAllowed(event, "server")) {
            printWarning(event, "You are not allowed to use that command");
            return;
        }

        if (args[0].equals("set")) {
            switch (args[1]) {
                case "channelrss": {
                    setRSSChannel(event.getGuild(), event.getGuild().getTextChannelsByName(args[2], true).get(0).getId());
                    printOk(event, "`channelrss` was updated successfully to `" + args[2] + "`");
                    break;
                }
                case "channelvoicelog": {
                    setVoiceLogChannel(event.getGuild(), event.getGuild().getTextChannelsByName(args[2], true).get(0).getId());
                    printOk(event, "`channelvoicelog` was updated successfully to `" + args[2] + "`");
                    break;
                }
                case "channelmusictext": {
                    setChannelMusicText(event.getGuild(), event.getGuild().getTextChannelsByName(args[2], true).get(0).getId());
                    printOk(event, "`channelmusictext` was updated successfully to `" + args[2] + "`");
                    break;
                }
                case "channelmusicvoice": {
                    setChannelMusicVoice(event.getGuild(), event.getGuild().getVoiceChannelsByName(args[2], true).get(0).getId());
                    printOk(event, "`channelmusicvoice` was updated successfully to `" + args[2] + "`");
                    break;
                }
                case "musicautoplay": {
                    setMusicAutoplay(event.getGuild(), Boolean.parseBoolean(args[2]));
                    printOk(event, "`musicautoplay` was updated successfully to `" + args[2] + "`");
                    break;
                }
                case "musicautoplaylink": {
                    setAutoplaylink(event.getGuild(), args[2]);
                    printOk(event, "`musicautoplaylink` was updated successfully to `" + args[2] + "`");
                    break;
                }
                case "showvoicelog": {
                    setShowVoicelog(event.getGuild(), Boolean.parseBoolean(args[2]));
                    printOk(event, "`showvoicelog` was updated successfully to `" + args[2] + "`");
                    break;
                }
                case "rssrepeat": {
                    var value = getInteger(args[2]);
                    if (value.isPresent()) {
                        updateRSSRepeat(event.getGuild(), value.get());
                        printOk(event, "`rssrepeat` was updated successfully to `" + args[2] + "`");
                    } else {
                        printError(event, "The repeat was not a number. Please enter the repeat delay in seconds.");
                    }
                    break;
                }
                case "infotext": {
                    var builder = new StringBuilder();
                    for (int i = 2; i < args.length; i++) {
                        builder.append(args[i]);
                        if (i + 1 < args.length) {
                            builder.append(" ");
                        }
                    }
                    setInfotext(event.getGuild(), builder.toString());
                    printOk(event, "`infotext` was updated successfully to `" + builder.toString() + "`");
                    break;
                }
                default: {
                    printError(event, "Used command flags are wrong!");
                    break;
                }
            }
        } else {
            printError(event, "Used command flags are wrong!");
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        cmdMsg(event, "server");
    }

    @Override
    public String help() {
        return "Settings command for server settings - Flags: channelrss; channelvoicelog; channelmusictext; channelmusicvoice; musicautoplay; musicautoplaylink; showvoicelog; rssrepeat";
    }
}
