/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.WASDHelioS.Handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.WASDHelioS.Handler.SubCommandHandler.CEditAddHandler;
import me.WASDHelioS.Handler.SubCommandHandler.CEditEditHandler;
import me.WASDHelioS.Handler.SubCommandHandler.CEditRemoveHandler;
import me.WASDHelioS.Main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Nick
 */
public class CEditHandler extends CommandHandler implements CommandExecutor {

    private static final String locfrom = "cedit.fromcommand";
    private static final String locto = "cedit.tocommand";
    private Main plugin;
    private String CEdit = "[CEdit] ";
    private CEditRemoveHandler remove;
    private CEditAddHandler add;
    private CEditEditHandler edit;

    public CEditHandler(Main plugin) {
        super();
        this.plugin = plugin;

        remove = new CEditRemoveHandler(plugin, this);
        add = new CEditAddHandler(plugin, this);
        edit = new CEditEditHandler(plugin, this);
    }

    /**
     * Gets the fromcommand list.
     *
     * @param config the current configuration.
     * @return A string list with tocommands.
     */
    public List<String> getFromCommandsList(FileConfiguration config) {
        List<String> fromCommandList = config.getStringList("cedit.fromcommand");
        return fromCommandList;
    }

    /**
     * Gets the tocommand list.
     *
     * @param config the current configuration.
     * @return A String list with tocommands.
     */
    public List<String> getToCommandsList(FileConfiguration config) {
        List<String> toCommandList = config.getStringList("cedit.tocommand");
        return toCommandList;
    }

    /**
     * sends a joined list of commands.
     *
     * @param sender The sender of the initial command.
     */
    private void sendList(CommandSender sender) {
        FileConfiguration config = plugin.getConfig();
        List<String> fromCommandList = getFromCommandsList(config);
        List<String> toCommandList = getToCommandsList(config);

        if (fromCommandList.size() == toCommandList.size()) {
            for (int i = 0; i < fromCommandList.size(); i++) {
                sender.sendMessage(ChatColor.GOLD + CEdit + ChatColor.GRAY + (i + 1) + " from -  " + fromCommandList.get(i) + " to - " + toCommandList.get(i));
            }
        } else {
            sender.sendMessage(ChatColor.RED + CEdit + "ERROR! FromCommand and ToCommand aren't the same size. check the config file and fix this!");
        }
    }

    /**
     * Gets a list of CommandArguments, which are seperated by forward-slashes.
     *
     * @param arguments Array of arguments entered by a user.
     * @return A list of arguments split up at the forward-slashes.
     */
    public List<String> getCommandArgs(String[] arguments) {
        ArrayList<String> returnList = new ArrayList<>();
        List<String> args = Arrays.asList(arguments);
        String command = null;


        for (int i = 0; i < args.size(); i++) {
            if (args.get(i).startsWith("/")) {
                if (command != null) {
                    returnList.add(command);
                }
                command = args.get(i).substring(1);

            } else {
                if (command != null) {
                    command = command + " " + args.get(i);
                }
            }
        }
        if (command != null) {
            returnList.add(command);
        }

        for (int i = 0; i < returnList.size(); i++) {
            returnList.set(i, returnList.get(i).trim().replaceAll(" +", " "));
        }

        return returnList;
    }

    /**
     * Checks if the tocommand actually exists in the list of cedit.tocommand.
     * if so, it returns true. else it will return false.
     *
     * @param toc The command from tocommand you want checked.
     * @return true if it exists; false if it doesnt.
     */
    public boolean checkIfToCommandExists(String toc) {

        for (int i = 0; i < getToCommandsList(plugin.getConfiguration()).size(); i++) {
            if (getToCommandsList(plugin.getConfiguration()).get(i).equalsIgnoreCase(toc)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the fromcommand actually exists in the list of
     * cedit.fromcommand. if so, it returns true. else it will return false.
     *
     * @param fromc The command from fromcommand you want checked.
     * @return true if it exists; false if it doesnt.
     */
    public boolean checkIfFromCommandExists(String fromc) {

        for (int i = 0; i < getFromCommandsList(plugin.getConfiguration()).size(); i++) {
            if (getFromCommandsList(plugin.getConfiguration()).get(i).equalsIgnoreCase(fromc)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkIfListHasAnEmptyValue(List<String> args) {
        for (String string : args) {
            if (string.equalsIgnoreCase("") || string.equalsIgnoreCase(" ")) {
                return true;
            }
        }
        return false;
    }

    public boolean checkIfListHasAnAndChar(List<String> args) {
        for (String string : args) {
            if (string.contains("&")) {
                return true;
            }
        }
        return false;
    }

    public String getCEdit() {
        return CEdit;
    }

    public void saveConfig(Main plugin) {
        saveConfiguration(plugin);
    }

    public String getLocto() {
        return locto;
    }

    public String getLocfrom() {
        return locfrom;
    }

    private void handleCEditCommand(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.GOLD + CEdit + "CEdit command editor. type /CEdit help or /CEdit ? for commands.");
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
                sendCEditHelpMessage(sender);
            } else if (args[0].equalsIgnoreCase("list")) {
                sendList(sender);
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("cedit.reload")) {
                    Bukkit.getServer().broadcast(ChatColor.GOLD + CEdit + ChatColor.DARK_RED + "Reloading..", "cedit.*");

                    plugin.getConfigHandler().reloadCommandsAlt();

                    Bukkit.getServer().broadcast(ChatColor.GOLD + CEdit + ChatColor.DARK_GREEN + "Reloaded!", "cedit.*");

                } else {
                    sender.sendMessage(ChatColor.RED + "You do not have permission!");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "This command does not exist!");
            }
        } else if (args.length > 1) {
            switch (args[0].toLowerCase()) {
                //Add statement.
                case "add":
                    add.handleAddCommand(sender, args);
                    break;

                //Edit statement.
                case "edit":
                    edit.handleEditCommand(sender, args);

                    break;
                //Remove statement.
                case "remove":
                    remove.handleRemoveCommand(sender, args);

                    break;
                //Remap statement.
                case "remap":
                    edit.handleRemapCommand(sender, args);
                    break;
                default:
                    sender.sendMessage(ChatColor.RED
                            + "This command does not exist!");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "This command does not exist!");
        }
        plugin.getConfigHandler().reloadCommandsAlt();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if (cmd.getName().equalsIgnoreCase("CEdit")) {

            this.handleCEditCommand(sender, args);
            return true;
        }
        return false;
    }

    private void sendCEditHelpMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "---------------CEdit Commands---------------");
        sender.sendMessage(ChatColor.GOLD + "/CEdit : sends a useless message.");
        sender.sendMessage(ChatColor.GOLD + "/CEdit help : Shows this help message.");
        sender.sendMessage(ChatColor.GOLD + "/CEdit ? : Alias for help.");
        sender.sendMessage(ChatColor.GOLD + "/CEdit list : returns a list of all from and to commands.");
        sender.sendMessage(ChatColor.GOLD + "/CEdit reload : Reloads this plugin.");
        sender.sendMessage(ChatColor.GOLD + "/CEdit add /<from command> /<to command> : Adds a command.");
        sender.sendMessage(ChatColor.GOLD + "/CEdit add /<to command> /<from command> & /<from command> etc : adds multiple commands to one.");
        sender.sendMessage(ChatColor.GOLD + "/CEdit edit /<fromcommand> /<tocommand> /<new fromcommand> /<new tocommand> : edits a command. first from and to : "
                + "the registered commands. second from and to : the command to replace them with.");
        sender.sendMessage(ChatColor.GOLD + "/CEdit edit index <index> /<new fromcommand> /<new tocommand> : edits the command at the specified index (retrievable by list)");
        sender.sendMessage(ChatColor.GOLD + "/CEdit remap /<existing tocommand> /<new fromcommand> : Maps the existing tocommand to a new fromcommand. (Replaces the old one.)");
        sender.sendMessage(ChatColor.GOLD + "/CEdit remove fromc <fromCommand> : removes the command based on the fromcommand.");
        sender.sendMessage(ChatColor.GOLD + "/CEdit remove toc <toCommand> : removes the command based on the toCommand.");
        sender.sendMessage(ChatColor.GOLD + "/CEdit remove index <index> : Removes the command based on the index. (retrievable by list)");
    }
}
