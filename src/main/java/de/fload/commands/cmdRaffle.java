package de.fload.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static de.fload.database.DatabaseAction.isAllowed;
import static de.fload.util.FUNCTION.*;

public class cmdRaffle implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (!isAllowed(event, "raffle")) {
            printWarning(event, "You are not allowed to use that command");
            return;
        }

        int result = 0;
        boolean error = false;
        switch (args.length) {
            case 0: {
                result = (int) (Math.random() * 6) + 1;
                break;
            }
            case 1: {
                try {
                    result = (int) (Math.random() * Integer.parseInt(args[0])) + 1;
                } catch (NumberFormatException e) {
                    printError(event, "Flag was not a number!");
                    error = true;
                    outputException(e);
                }
                break;
            }
            case 2: {
                try {
                    result = (int) (Math.random() * (Integer.parseInt(args[1]) - Integer.parseInt(args[0]))) + Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    printError(event, "Flag was not a number!");
                    error = true;
                    outputException(e);
                }
                break;
            }
            default: {
                printError(event, "Too much flags set");
                error = true;
                break;
            }
        }

        if (!error) {
            event.getTextChannel().sendMessage(result + "").queue();
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        cmdMsg(event, "raffle");
    }

    @Override
    public String help() {
        return "Raffles for you";
    }
}
