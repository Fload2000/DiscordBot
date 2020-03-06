package de.fload.commands;

import de.fload.database.DatabaseAction;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static de.fload.database.DatabaseAction.isAllowed;
import static de.fload.database.DatabaseAction.isNotCommand;
import static de.fload.util.FUNCTION.*;

public class cmdPermission implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (!isAllowed(event, "permission")) {
            printWarning(event, "You are not allowed to use that command");
            return;
        }

        if (args.length < 4) {
            printError(event, "Not enough flags set!");
        }

        switch (args[0]) {
            case "add": {
                addPermission(event, args);
                break;
            }
            case "remove": {
                if (isNotCommand(event.getGuild(), args[2])) {
                    printError(event, "Unknown command name");
                    break;
                }
                removePermission(event, args);
                break;
            }
            case "user": {
                userPermission(event, args);
                break;
            }
            case "role": {
                rolePermission(event, args);
                break;
            }
            default:
                printError(event, "Used command flags are wrong!");
                break;
        }
    }

    private void addPermission(MessageReceivedEvent event, String[] args) {
        if (isNotCommand(event.getGuild(), args[1])) {
            printError(event, "Unknown command name");
        } else {
            DatabaseAction.addPermission(event.getGuild(), args[1]);
            printOk(event, "Permission successfully added!");
        }
    }

    private void removePermission(MessageReceivedEvent event, String[] args) {
        if (isNotCommand(event.getGuild(), args[1])) {
            printError(event, "Unknown command name");
        } else {
            DatabaseAction.removePermission(event.getGuild(), args[1]);
        }
    }

    private void userPermission(MessageReceivedEvent event, String[] args) {
        if (event.getGuild().getMembersByName(args[1], true).isEmpty()) {
            printError(event, "This user does not exist.");
        } else if (DatabaseAction.checkPermission(event.getGuild(), args[3])) {
            switch (args[2]) {
                case "add": {
                    DatabaseAction.addPermissionUser(event.getGuild(), args[3], event.getGuild().getMembersByName(args[1], true).get(0));
                    printOk(event, "Permission " + args[3] + " successfully added to " + args[1] + ".");
                    break;
                }
                case "remove": {
                    DatabaseAction.removePermissionUser(event.getGuild(), args[3], event.getGuild().getMembersByName(args[1], true).get(0));
                    printOk(event, "Permission " + args[3] + " successfully removed from " + args[1] + ".");
                    break;
                }
                default: {
                    printError(event, "Used command flags are wrong!");
                    break;
                }
            }
        }
    }

    private void rolePermission(MessageReceivedEvent event, String[] args) {
        if (event.getGuild().getRolesByName(args[1], true).isEmpty()) {
            printError(event, "This user does not exist.");
        } else if (DatabaseAction.checkPermission(event.getGuild(), args[3])) {
            switch (args[2]) {
                case "add": {
                    DatabaseAction.addPermissionRole(event.getGuild(), args[3], event.getGuild().getRolesByName(args[1], true).get(0));
                    printOk(event, "Permission " + args[3] + " successfully added to " + args[1] + ".");
                    break;
                }
                case "remove": {
                    DatabaseAction.removePermissionRole(event.getGuild(), args[3], event.getGuild().getRolesByName(args[1], true).get(0));
                    printOk(event, "Permission " + args[3] + " successfully removed from " + args[1] + ".");
                    break;
                }
                default: {
                    printError(event, "Used command flags are wrong!");
                    break;
                }
            }
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        cmdMsg(event, "permission");
    }

    @Override
    public String help() {
        return "Settings for the permission system - Flags: add user/role; remove user/role";
    }
}
