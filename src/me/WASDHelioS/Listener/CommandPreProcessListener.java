/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.WASDHelioS.Listener;

import java.util.ArrayList;
import java.util.List;
import me.WASDHelioS.Main.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

/**
 *
 * @author Nick
 */
public class CommandPreProcessListener implements Listener {

    private List<String> tocommand;
    private List<String> fromcommand;
    private Main plugin;

    public CommandPreProcessListener(Main plugin) {


        this.plugin = plugin;
    }

    /**
     *
     * EventHandler which handles the CommandEditor.
     *
     * @param ev PlayerCommandPreprocessEvent (Happens when a command is about
     * to be processed.)
     */
    @EventHandler
    public void beforeCommandProcessing(PlayerCommandPreprocessEvent ev) {

        tocommand = plugin.getConfiguration().getStringList("cedit.tocommand");
        fromcommand = plugin.getConfiguration().getStringList("cedit.fromcommand");

        String input = ev.getMessage();

        for (int i = 0; i < tocommand.size(); i++) {
            if (input.equalsIgnoreCase("/" + tocommand.get(i))) {


                if (fromcommand.size() == tocommand.size()) {

                    Player player = ev.getPlayer();
                    Bukkit.dispatchCommand(player, fromcommand.get(i));
                    ev.setCancelled(true);
                    return;
                } else if (fromcommand.size() > tocommand.size()) {
                    plugin.getLogger().info("Fromcommand's list is bigger than tocommand's list! Check your config file to make sure everything is okay."
                            + "You possibly mapped to-commands to the wrong from-command or vice-versa.");
                    return;
                } else if (fromcommand.size() < tocommand.size()) {
                    plugin.getLogger().info("Fromcommand's list is smaller than tocommand's list! chack your config file to make sure everything is okay."
                            + "You possibly mapped to-commands to the wrong from-command or vice-versa.");
                    return;
                }

            }
        }
    }

    /**
     * Gets all commands which are currently in use. NOT DONE.
     *
     * @return A list of commands which are in use.
     */
    private ArrayList<String> getCommandPage() {
        List<String> commands = new ArrayList<>();
        List<String> usage = new ArrayList<>();

        Plugin[] plugins = plugin.getServer().getPluginManager().getPlugins();
        PluginDescriptionFile file = null;
        try {
            for (Plugin p : plugins) {
                file = p.getDescription();

                for (String command : file.getCommands().keySet()) {
                    commands.add(command);
                }

                for (String s : commands) {
                    if (file.getCommands().get(s).get("usage") != null) {
                        usage.add((String) file.getCommands().get(s).get("usage"));
                    } else {
                        usage.add("");
                    }
                }
            }
        } catch (Exception e) {
        }



        ArrayList<String> results = new ArrayList<>();
        for (int i = 0; i < commands.size(); i++) {

            results.add(usage.get(i));
        }

        return results;
    }
}
