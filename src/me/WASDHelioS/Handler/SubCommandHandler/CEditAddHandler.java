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
public class CEditAddHandler {

    private Main plugin;
    private CEditHandler ch;

    public CEditAddHandler(Main plugin, CEditHandler ch) {
        this.plugin = plugin;
        this.ch = ch;
    }

    /**
     * adds a command to the specified location list (Config file)
     *
     * @param cmd command
     * @param loc location of the list in the config file.
     */
    private void addCommand(String cmd, String loc) {
        List<String> list = plugin.getConfiguration().getStringList(loc);
        list.add(cmd);
        plugin.getConfiguration().set(loc, list);
    }

    public void handleAddCommand(CommandSender sender, String[] args) {
        if (sender.hasPermission("cedit.add")) {
            List<String> commands = ch.getCommandArgs(args);
            if (!ch.getCommandArgs(args).isEmpty()) {
                if (!commands.contains("") && !commands.contains(null) && !ch.checkIfListHasAnEmptyValue(commands)) {
                    if (commands.size() == 2) {
                        if (!ch.checkIfToCommandExists(ch.getCommandArgs(args).get(0))) {

                            addCommand(commands.get(1), ch.getLocfrom());
                            addCommand(commands.get(0), ch.getLocto());

                            ch.saveConfig(plugin);

                            sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + "Command added : to " + commands.get(0) + " from " + commands.get(1));
                        } else {
                            sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + ChatColor.RED + "This ToCommand already exists!");
                        }
                    } else if (ch.checkIfListHasAnAndChar(commands)) {
                        if (!ch.checkIfToCommandExists(commands.get(0))) {
                            if (!args[0].equalsIgnoreCase("&") && !args[1].equalsIgnoreCase("&") && !args[2].equalsIgnoreCase("&")) {
                                addCommand(commands.get(0), ch.getLocto());
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
                                addCommand(commandfrom, ch.getLocfrom());
                                ch.saveConfig(plugin);
                                sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + "Command added : to " + commands.get(0) + " from " + commandfrom);
                            } else {
                                sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + ChatColor.RED + "Faulty formatting! Did you misplace an '&'?");
                            }
                        } else {
                            sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + ChatColor.RED + "This ToCommand already exists!");
                        }
                    } else {
                        sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + ChatColor.RED + "Invalid amount of arguments!");
                    }
                } else {
                    sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + ChatColor.RED + "You cannot add empty commands!");
                }
            } else {
                sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + ChatColor.RED + "Invalid amount of arguments! Did you forget a '/'?");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You do not have permission!");
        }
    }
}
