/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.WASDHelioS.Handler.SubCommandHandler;

import java.util.List;
import java.util.logging.Level;
import me.WASDHelioS.Handler.CEditHandler;
import me.WASDHelioS.Main.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Nick
 */
public class CEditRemoveHandler {

    private Main plugin;
    private CEditHandler ch;

    public CEditRemoveHandler(Main plugin, CEditHandler ch) {
        this.plugin = plugin;
        this.ch = ch;
    }

    /**
     *
     * Removes a command.
     *
     * @param cmd command which is the input.
     * @param loc Location of which list it is (if fromc, loc = locfrom; if toc,
     * loc = locto)
     */
    private void removeCommand(String cmd, String loc) {
        List<String> list = plugin.getConfiguration().getStringList(loc);
        String locSecond;
        if (loc.equalsIgnoreCase(ch.getLocfrom())) {
            locSecond = ch.getLocto();
        } else {
            locSecond = ch.getLocfrom();
        }
        List<String> listsecond = plugin.getConfiguration().getStringList(locSecond);

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equalsIgnoreCase(cmd)) {
                list.remove(i);
                listsecond.remove(i);
            }
        }
        plugin.getConfiguration().set(loc, list);
        plugin.getConfiguration().set(locSecond, listsecond);
    }

    /**
     * Removes a command based on the index.
     *
     * @param index index of the command
     * @param loc Location of which list it is(if fromc, loc = locFrom, if toc,
     * loc = locTo)
     */
    private void removeCommand(int index, String loc) {
        List<String> list = plugin.getConfiguration().getStringList(loc);
        String locSecond;
        if (loc.equalsIgnoreCase(ch.getLocfrom())) {
            locSecond = ch.getLocto();
        } else {
            locSecond = ch.getLocfrom();
        }
        List<String> listSecond = plugin.getConfiguration().getStringList(locSecond);
        list.remove(index);
        listSecond.remove(index);

        plugin.getConfiguration().set(loc, list);
        plugin.getConfiguration().set(locSecond, listSecond);
    }

    public void handleRemoveCommand(CommandSender sender, String[] args) {
        if (sender.hasPermission("cedit.remove")) {
            switch (args[1]) {
                case "fromc":

                    List<String> commandsf = ch.getCommandArgs(args);
                    if (commandsf != null && !commandsf.isEmpty() && !commandsf.contains(null)) {
                        if (ch.checkIfFromCommandExists(commandsf.get(0))) {
                            if (!commandsf.get(0).equalsIgnoreCase("")) {

                                removeCommand(commandsf.get(0), ch.getLocfrom());
                                ch.saveConfig(plugin);

                                sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + "FromCommand " + commandsf.get(0) + " and the corresponding tocommand have been removed!");
                            } else {
                                sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + ChatColor.RED + "Too few arguments!");
                            }
                        } else {
                            sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + ChatColor.RED + "This FromCommand is not registered!");
                        }
                    } else {
                        sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + ChatColor.RED + "Too few arguments! Did you forget to use a '/' in your command?");
                    }

                    break;
                case "toc":

                    List<String> commandst = ch.getCommandArgs(args);
                    plugin.getLogger().log(Level.INFO, "{0}", commandst.size());
                    if (commandst != null && !commandst.isEmpty() && !commandst.contains(null)) {
                        if (ch.checkIfToCommandExists(commandst.get(0))) {
                            if (!commandst.get(0).equalsIgnoreCase("")) {

                                removeCommand(commandst.get(0), ch.getLocto());
                                ch.saveConfig(plugin);

                                sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + "ToCommand " + commandst.get(0) + " and the corresponding fromcommand have been removed!");
                            } else {
                                sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + ChatColor.RED + "Too few arguments!");
                            }
                        } else {
                            sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + ChatColor.RED + "This ToCommand is not registered!");
                        }
                    } else {
                        sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + ChatColor.RED + "Too few arguments! Did you forget to use a '/' in your command?");
                    }

                    break;
                case "index":

                    int index = -1;
                    try {
                        index = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + ChatColor.RED + "Your index has to be a number!");
                        break;
                    }
                    if (index - 1 <= ch.getFromCommandsList(plugin.getConfiguration()).size()) {
                        if (index > 0) {
                            removeCommand(index - 1, ch.getLocto());
                            ch.saveConfig(plugin);

                            sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + "The command at index " + index + " has been removed!");
                        } else {
                            sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + ChatColor.RED + "Negative values aren't allowed!");
                        }
                    } else {
                        sender.sendMessage(ChatColor.GOLD + ch.getCEdit() + ChatColor.RED + "The index is bigger than the size of the list!");
                    }
                    break;
                default:
                    sender.sendMessage(ChatColor.RED + "This command does not exist!");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You do not have permission!");
        }
    }
}
