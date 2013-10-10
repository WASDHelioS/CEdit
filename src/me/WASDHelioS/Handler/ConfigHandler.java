/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.WASDHelioS.Handler;

import me.WASDHelioS.Main.Main;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Nick
 */
public class ConfigHandler {

    private Main plugin;

    public ConfigHandler(Main pl) {
        this.plugin = pl;
    }

    /**
     *
     * Checks if this configuration file has empty values or missing paths. If
     * so, it returns true. Otherwise it returns false.
     *
     * @param config - The active configuration file.
     * @return
     */
    public boolean isConfigEmptyValues(FileConfiguration config) {

        if (!config.isSet("cedit.fromcommand")
                || !config.isSet("cedit.tocommand")) {
            return true;
        }
        
        if(config.getStringList("cedit.fromcommand").size() != config.getStringList("cedit.tocommand").size()) {
            return true;
        }



        return false;
    }

    /**
     * Reloads the configuration file.
     */
    public void reloadCommands() {
        plugin.reloadConfigAlt();
        plugin.onEnableEss();
    }

    /**
     * Reloads the configuration file without sending a message.
     */
    public void reloadCommandsAlt() {
        plugin.reloadConfigAlt();
        plugin.onEnableNoSending();
    }
}
