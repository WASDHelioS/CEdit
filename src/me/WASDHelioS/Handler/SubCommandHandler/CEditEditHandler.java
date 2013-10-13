/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.WASDHelioS.Handler.SubCommandHandler;

import java.util.List;
import me.WASDHelioS.Handler.CEditHandler;
import me.WASDHelioS.Main.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Nick
 */
public class CEditEditHandler {

    private Main plugin;
    private CEditHandler ch;

    public CEditEditHandler(Main plugin, CEditHandler ch) {
        this.plugin = plugin;
        this.ch = ch;
    }

    /**
     * Replaces a command.
     *
     * @param cmds List of commands going from oldFromCommand, oldToCommand,
     * newFromCommand, newToCommand.
     */
    private void replaceCommand(List<String> cmds) {
        List<String> fromList = plugin.getConfiguration().getStringList(ch.getLocfrom());
        List<String> toList = plugin.getConfiguration().getStringList(ch.getLocto());

        int index = -1;
        int iCheck = -1;
        for (int i = 0; i < fromList.size(); i++) {
            if (fromList.get(i).equalsIgnoreCase(cmds.get(0))) {
                index = i;
                break;
            }
        }
        for (int i = 0; i < toList.size(); i++) {
            if (toList.get(i).equalsIgnoreCase(cmds.get(1))) {
                iCheck = i;
                break;
            }
        }

        if (index != -1 && iCheck != -1) {
            if (index == iCheck) {
                fromList.set(index, cmds.get(2));
                toList.set(index, cmds.get(3));

                plugin.getConfiguration().set(ch.getLocfrom(), fromList);
                plugin.getConfiguration().set(ch.getLocto(), toList);
            }
        }
    }

    private void replaceCommandToC(List<String> cmds) {
        List<String> fromList = ch.getFromCommandsList(plugin.getConfiguration());
        List<String> toList = ch.getToCommandsList(plugin.getConfiguration());

        int index = -1;

        for (int i = 0; i < toList.size(); i++) {
            if (toList.get(i).equalsIgnoreCase(cmds.get(0))) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            fromList.set(index, cmds.get(1));

            plugin.getConfiguration().set(ch.getLocfrom(), fromList);
        }
    }

    /**
     * Replaces a command at a specific index.
     *
     * @param index The index which needs replaced.
     * @param cmds the new commands going from newFromcommand, newTocommand.
     */
    public void replaceCommand(int index, List<String> cmds) {
        List<String> fromList = plugin.getConfiguration().getStringList(ch.getLocfrom());
        List<String> toList = plugin.getConfiguration().getStringList(ch.getLocto());

        fromList.set(index, cmds.get(1));
        toList.set(index, cmds.get(0));

        plugin.getConfiguration().set(ch.getLocfrom(), fromList);
        plugin.getConfiguration().set(ch.getLocto(), toList);
    }

    public void handleEditCommand(CommandSender sender, String[] args) {

        if (sender.hasPermission("cedit.edit")) {
            if (args[1].equalsIgnoreCase("index")) {
                int index = -1;
                try {
                    index = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + ChatColor.RED + "Your index has to be a number!");

                }
                if (index > -1) {
                    List<String> commands = ch.getCommandArgs(args);
                    if (commands.size() == 2 && !commands.contains(null)) {
                        if (!ch.checkIfListHasAnEmptyValue(commands)) {
                            if (!commands.contains("")) {
                                if (!ch.checkIfToCommandExists(commands.get(0))) {
                                    if (ch.getFromCommandsList(plugin.getConfiguration()).size() > index - 1) {

                                        replaceCommand(index - 1, commands);

                                        ch.saveConfig(plugin);

                                        sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + "Commands edited on index :" + (index) + " to " + commands.get(0) + " from " + commands.get(1));

                                    } else {
                                        sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + ChatColor.RED + "The index is bigger than the size of the list!");
                                    }
                                } else {
                                    sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + ChatColor.RED + "This command already exists!");
                                }
                            } else {
                                sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + ChatColor.RED + "Invalid amount of arguments!");
                            }
                        } else {
                            sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + ChatColor.RED + "Cannot add empty commands!");
                        }
                    } else if (ch.checkIfListHasAnAndChar(commands)) {
                        if (!ch.checkIfListHasAnEmptyValue(commands)) {
                            if (!args[0].equalsIgnoreCase("&") && !args[1].equalsIgnoreCase("&") && !args[2].equalsIgnoreCase("&") && !args[3].equalsIgnoreCase("&") && !args[4].equalsIgnoreCase("&")) {
                                String commandfrom = null;
                                for (int i = 1; i < commands.size(); i++) {
                                    if (commands.get(i) != null) {
                                        if (commandfrom == null) {
                                            commandfrom = commands.get(i);
                                        } else {
                                            commandfrom = commandfrom + commands.get(i);
                                        }
                                    }
                                }
                                commands.set(1, commandfrom);
                                replaceCommand(index - 1, commands);
                                ch.saveConfig(plugin);

                                sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + "Commands edited on index : " + index + " to " + commands.get(0) + " from " + commands.get(1));
                            } else {
                                sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + ChatColor.RED + "Faulty formatting! Did you misplace an '&'?");
                            }
                        } else {
                            sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + ChatColor.RED + "You cannot add empty commands!");
                        }
                    } else {
                        sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + ChatColor.RED + "Invalid amount of arguments!");
                    }

                } else {
                    sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + ChatColor.RED + "Negative or 0 values aren't allowed!");
                }
            } else {
                sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + ChatColor.RED + "This command does not exist!");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You do not have permission!");
        }
    }

    public void handleRemapCommand(CommandSender sender, String[] args) {
        if (sender.hasPermission(
                "cedit.edit")) {
            List<String> commands = ch.getCommandArgs(args);
            if (commands != null && !commands.isEmpty() && !commands.contains(null) && !ch.checkIfListHasAnEmptyValue(commands)) {
                if (ch.checkIfToCommandExists(commands.get(0))) {
                    if (!commands.get(0).equalsIgnoreCase("")) {
                        if (commands.size() == 2) {
                            replaceCommandToC(commands);
                            ch.saveConfig(plugin);

                            sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + "The command " + commands.get(0) + " has been remapped to execute " + commands.get(1) + "!");
                        } else if (ch.checkIfListHasAnAndChar(commands)) {
                            if (!args[0].equalsIgnoreCase("&") && !args[1].equalsIgnoreCase("&") && !args[2].equalsIgnoreCase("&")) {
                                String commandfrom = null;
                                for (int i = 1; i < commands.size(); i++) {
                                    if (commands.get(i) != null) {
                                        if (commandfrom == null) {
                                            commandfrom = commands.get(i);
                                        } else {
                                            commandfrom = commandfrom + commands.get(i);
                                        }
                                    }
                                }
                                commands.set(1, commandfrom);
                                replaceCommandToC(commands);
                                ch.saveConfig(plugin);

                                sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + "The command " + commands.get(0) + " has been remapped to execute " + commands.get(1) + "!");
                            } else {
                                sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + ChatColor.RED + "Faulty formatting! Did you misplace an '&'?");
                            }
                        } else {
                            sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + ChatColor.RED + "Too few arguments! Did you forget a '/'?");
                        }
                    } else {
                        sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + ChatColor.RED + "Too few arguments! Did you forget a '/'?");
                    }
                } else {
                    sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + ChatColor.RED + "This toCommand does not exist!");
                }
            } else {
                sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + ChatColor.RED + "Too few arguments! Did you forget a '/'?");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You do not have permission!");
        }
    }
}
