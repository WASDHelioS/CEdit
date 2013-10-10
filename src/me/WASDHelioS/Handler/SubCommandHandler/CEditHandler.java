/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.WASDHelioS.Handler.SubCommandHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import me.WASDHelioS.Handler.CommandHandler;
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

    public CEditHandler(Main plugin) {
        super();
        this.plugin = plugin;
    }

    /**
     * Gets the fromcommand list.
     *
     * @param config the current configuration.
     * @return A string list with tocommands.
     */
    private List<String> getFromCommandsList(FileConfiguration config) {
        List<String> fromCommandList = config.getStringList("cedit.fromcommand");
        return fromCommandList;
    }

    /**
     * Gets the tocommand list.
     *
     * @param config the current configuration.
     * @return A String list with tocommands.
     */
    private List<String> getToCommandsList(FileConfiguration config) {
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
        if (loc.equalsIgnoreCase(locfrom)) {
            locSecond = locto;
        } else {
            locSecond = locfrom;
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
        if (loc.equalsIgnoreCase(locfrom)) {
            locSecond = locto;
        } else {
            locSecond = locfrom;
        }
        List<String> listSecond = plugin.getConfiguration().getStringList(locSecond);
        list.remove(index);
        listSecond.remove(index);

        plugin.getConfiguration().set(loc, list);
        plugin.getConfiguration().set(locSecond, listSecond);
    }

    /**
     * Replaces a command.
     *
     * @param cmds List of commands going from oldFromCommand, oldToCommand,
     * newFromCommand, newToCommand.
     */
    private void replaceCommand(List<String> cmds) {
        List<String> fromList = plugin.getConfiguration().getStringList(locfrom);
        List<String> toList = plugin.getConfiguration().getStringList(locto);

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

                plugin.getConfiguration().set(locfrom, fromList);
                plugin.getConfiguration().set(locto, toList);
            }
        }
    }

    private void replaceCommandToC(List<String> cmds) {
        List<String> fromList = getFromCommandsList(plugin.getConfiguration());
        List<String> toList = getToCommandsList(plugin.getConfiguration());

        int index = -1;

        for (int i = 0; i < toList.size(); i++) {
            if (toList.get(i).equalsIgnoreCase(cmds.get(0))) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            fromList.set(index, cmds.get(1));

            plugin.getConfiguration().set(locfrom, fromList);
        }
    }

    /**
     * Replaces a command at a specific index.
     *
     * @param index The index which needs replaced.
     * @param cmds the new commands going from newFromcommand, newTocommand.
     */
    private void replaceCommand(int index, List<String> cmds) {
        List<String> fromList = plugin.getConfiguration().getStringList(locfrom);
        List<String> toList = plugin.getConfiguration().getStringList(locto);

        fromList.set(index, cmds.get(0));
        toList.set(index, cmds.get(1));

        plugin.getConfiguration().set(locfrom, fromList);
        plugin.getConfiguration().set(locto, toList);
    }

    /**
     * Gets a list of CommandArguments, which are seperated by forward-slashes.
     *
     * @param arguments Array of arguments entered by a user.
     * @return A list of arguments split up at the forward-slashes.
     */
    private List<String> getCommandArgs(String[] arguments) {
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
    private boolean checkIfToCommandExists(String toc) {

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
    private boolean checkIfFromCommandExists(String fromc) {

        for (int i = 0; i < getFromCommandsList(plugin.getConfiguration()).size(); i++) {
            if (getFromCommandsList(plugin.getConfiguration()).get(i).equalsIgnoreCase(fromc)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkIfListHasAnEmptyValue(List<String> args) {
        for (String string : args) {
            if (string.equalsIgnoreCase("") || string.equalsIgnoreCase(" ")) {
                return true;
            }
        }
        return false;
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

                    if (sender.hasPermission("cedit.add")) {
                        List<String> commands = getCommandArgs(args);
                        if (!getCommandArgs(args).isEmpty()) {
                            if (!commands.contains("") && !commands.contains(null) && !checkIfListHasAnEmptyValue(commands)) {
                                if (commands.size() == 2) {
                                    if (!checkIfToCommandExists(getCommandArgs(args).get(1))) {

                                        addCommand(commands.get(0), locfrom);
                                        addCommand(commands.get(1), locto);

                                        saveConfiguration(plugin);

                                        sender.sendMessage(ChatColor.GOLD + CEdit + "Command added : from " + commands.get(0) + " to " + commands.get(1));
                                    } else {
                                        sender.sendMessage(ChatColor.GOLD + CEdit + ChatColor.RED + "This ToCommand already exists!");
                                    }
                                } else {
                                    sender.sendMessage(ChatColor.GOLD + CEdit + ChatColor.RED + "Invalid amount of arguments!");
                                }
                            } else {
                                sender.sendMessage(ChatColor.GOLD + CEdit + ChatColor.RED + "You cannot add empty commands!");
                            }
                        } else {
                            sender.sendMessage(ChatColor.GOLD + CEdit + ChatColor.RED + "Invalid amount of arguments! Did you forget a '/'?");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "You do not have permission!");
                    }

                    break;

                //Edit statement.
                case "edit":
                    if (sender.hasPermission("cedit.edit")) {
                        if (args[1].equalsIgnoreCase("index")) {
                            int index = -1;
                            try {
                                index = Integer.parseInt(args[2]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(ChatColor.GOLD + CEdit + ChatColor.RED + "Your index has to be a number!");
                                break;
                            }
                            if (index > -1) {
                                List<String> commands = getCommandArgs(args);
                                if (commands.size() == 2 && !commands.contains(null)) {
                                    if (!checkIfListHasAnEmptyValue(commands)) {
                                        if (!commands.contains("")) {
                                            if (!checkIfToCommandExists(commands.get(1))) {
                                                if (getFromCommandsList(plugin.getConfiguration()).size() >= index - 1) {

                                                    replaceCommand(index - 1, commands);

                                                    saveConfiguration(plugin);

                                                    sender.sendMessage(ChatColor.GOLD + CEdit + "Commands edited on index " + (index) + " to /" + commands.get(0) + " /" + commands.get(1));

                                                } else {
                                                    sender.sendMessage(ChatColor.GOLD + CEdit + ChatColor.RED + "The index is bigger than the size of the list!");
                                                }
                                            } else {
                                                sender.sendMessage(ChatColor.GOLD + CEdit + ChatColor.RED + "This command already exists!");
                                            }
                                        } else {
                                            sender.sendMessage(ChatColor.GOLD + CEdit + ChatColor.RED + "Invalid amount of arguments!");
                                        }
                                    } else {
                                        sender.sendMessage(ChatColor.GOLD + CEdit + ChatColor.RED + "Cannot add empty commands!");
                                    }
                                } else {
                                    sender.sendMessage(ChatColor.GOLD + CEdit + ChatColor.RED + "Invalid amount of arguments!");
                                }

                            } else {
                                sender.sendMessage(ChatColor.GOLD + CEdit + ChatColor.RED + "Negative values aren't allowed!");
                            }

                            //Edit on index
                            //Edit on 4 commands
                        } else {

                            List<String> commands = getCommandArgs(args);
                            if (commands.size() == 4 && !commands.contains(null)) {
                                if (!commands.contains("") && !checkIfListHasAnEmptyValue(commands)) {
                                    if (checkIfFromCommandExists(commands.get(0)) && checkIfToCommandExists(commands.get(1)) && !checkIfToCommandExists(commands.get(3))) {
                                        replaceCommand(commands);

                                        saveConfiguration(plugin);

                                        sender.sendMessage(ChatColor.GOLD + CEdit + "Commands edited from /" + commands.get(0) + " /" + commands.get(1) + " to /" + commands.get(2) + " /" + commands.get(3));
                                    } else {
                                        sender.sendMessage(ChatColor.GOLD + CEdit + ChatColor.RED + "Command does not exist!");
                                    }
                                } else {
                                    sender.sendMessage(ChatColor.GOLD + CEdit + ChatColor.RED + "You cannot add empty commands!");
                                }
                            } else {
                                sender.sendMessage(ChatColor.GOLD + CEdit + ChatColor.RED + "Invalid amount of arguments!");
                            }
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "You do not have permission!");
                    }

                    break;
                //Remove statement.
                case "remove":
                    if (sender.hasPermission("cedit.remove")) {
                        switch (args[1]) {
                            case "fromc":

                                List<String> commandsf = getCommandArgs(args);
                                if (commandsf != null && !commandsf.isEmpty() && !commandsf.contains(null)) {
                                    if (checkIfFromCommandExists(commandsf.get(0))) {
                                        if (!commandsf.get(0).equalsIgnoreCase("")) {

                                            removeCommand(commandsf.get(0), locfrom);
                                            saveConfiguration(plugin);

                                            sender.sendMessage(ChatColor.GOLD + CEdit + "FromCommand " + commandsf.get(0) + " and the corresponding tocommand have been removed!");
                                        } else {
                                            sender.sendMessage(ChatColor.GOLD + CEdit + ChatColor.RED + "Too few arguments!");
                                        }
                                    } else {
                                        sender.sendMessage(ChatColor.GOLD + CEdit + ChatColor.RED + "This FromCommand is not registered!");
                                    }
                                } else {
                                    sender.sendMessage(ChatColor.GOLD + CEdit + ChatColor.RED + "Too few arguments! Did you forget to use a '/' in your command?");
                                }

                                break;
                            case "toc":

                                List<String> commandst = getCommandArgs(args);
                                plugin.getLogger().log(Level.INFO, "{0}", commandst.size());
                                if (commandst != null && !commandst.isEmpty() && !commandst.contains(null)) {
                                    if (checkIfToCommandExists(commandst.get(0))) {
                                        if (!commandst.get(0).equalsIgnoreCase("")) {

                                            removeCommand(commandst.get(0), locto);
                                            saveConfiguration(plugin);

                                            sender.sendMessage(ChatColor.GOLD + CEdit + "ToCommand " + commandst.get(0) + " and the corresponding fromcommand have been removed!");
                                        } else {
                                            sender.sendMessage(ChatColor.GOLD + CEdit + ChatColor.RED + "Too few arguments!");
                                        }
                                    } else {
                                        sender.sendMessage(ChatColor.GOLD + CEdit + ChatColor.RED + "This ToCommand is not registered!");
                                    }
                                } else {
                                    sender.sendMessage(ChatColor.GOLD + CEdit + ChatColor.RED + "Too few arguments! Did you forget to use a '/' in your command?");
                                }

                                break;
                            case "index":

                                int index = -1;
                                try {
                                    index = Integer.parseInt(args[2]);
                                } catch (NumberFormatException e) {
                                    sender.sendMessage(ChatColor.GOLD + CEdit + ChatColor.RED + "Your index has to be a number!");
                                    break;
                                }
                                if (index - 1 <= getFromCommandsList(plugin.getConfiguration()).size()) {
                                    if (index > -1) {
                                        removeCommand(index - 1, locto);
                                        saveConfiguration(plugin);

                                        sender.sendMessage(ChatColor.GOLD + CEdit + "The command at index " + index + " has been removed!");
                                    } else {
                                        sender.sendMessage(ChatColor.GOLD + CEdit + ChatColor.RED + "Negative values aren't allowed!");
                                    }
                                } else {
                                    sender.sendMessage(ChatColor.GOLD + CEdit + ChatColor.RED + "The index is bigger than the size of the list!");
                                }
                                break;
                            default:
                                sender.sendMessage(ChatColor.RED + "This command does not exist!");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "You do not have permission!");
                    }

                    break;
                //Remap statement.
                case "remap":
                    if (sender.hasPermission(
                            "cedit.edit")) {
                        List<String> commands = getCommandArgs(args);
                        if (commands != null && !commands.isEmpty() && !commands.contains(null) && !checkIfListHasAnEmptyValue(commands)) {
                            if (checkIfToCommandExists(commands.get(0))) {
                                if (!commands.get(0).equalsIgnoreCase("")) {
                                    if (commands.size() == 2) {
                                        replaceCommandToC(commands);
                                        saveConfiguration(plugin);

                                        sender.sendMessage(ChatColor.GOLD + CEdit + "The command " + commands.get(0) + " has been remapped to execute " + commands.get(1) + "!");
                                    } else {
                                        sender.sendMessage(ChatColor.GOLD + CEdit + ChatColor.RED + "Too few arguments! Did you forget a '/'?");
                                    }
                                } else {
                                    sender.sendMessage(ChatColor.GOLD + CEdit + ChatColor.RED + "Too few arguments! Did you forget a '/'?");
                                }
                            } else {
                                sender.sendMessage(ChatColor.GOLD + CEdit + ChatColor.RED + "This toCommand does not exist!");
                            }
                        } else {
                            sender.sendMessage(ChatColor.GOLD + CEdit + ChatColor.RED + "Too few arguments! Did you forget a '/'?");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "You do not have permission!");
                    }

                    break;
                default:
                    sender.sendMessage(ChatColor.RED
                            + "This command does not exist!");

                    break;
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
        sender.sendMessage(ChatColor.GOLD + "/CEdit edit /<fromcommand> /<tocommand> /<new fromcommand> /<new tocommand> : edits a command. first from and to : "
                + "the registered commands. second from and to : the command to replace them with.");
        sender.sendMessage(ChatColor.GOLD + "/CEdit edit index <index> /<new fromcommand> /<new tocommand> : edits the command at the specified index (retrievable by list)");
        sender.sendMessage(ChatColor.GOLD + "/CEdit remap /<existing tocommand> /<new fromcommand> : Maps the existing tocommand to a new fromcommand. (Replaces the old one.)");
        sender.sendMessage(ChatColor.GOLD + "/CEdit remove fromc <fromCommand> : removes the command based on the fromcommand.");
        sender.sendMessage(ChatColor.GOLD + "/CEdit remove toc <toCommand> : removes the command based on the toCommand.");
        sender.sendMessage(ChatColor.GOLD + "/CEdit remove index <index> : Removes the command based on the index. (retrievable by list)");
    }
}
