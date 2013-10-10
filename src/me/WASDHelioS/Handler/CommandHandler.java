/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.WASDHelioS.Handler;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.WASDHelioS.Handler.SubCommandHandler.CEditHandler;
import me.WASDHelioS.Main.Main;

/**
 *
 * @author Nick
 */
public abstract class CommandHandler {

    public CommandHandler() {
    }
    
    /**
     * Saves the current state of the plugin. Required for runtime editing.
     * @param plugin Main class.
     */
    protected void saveConfiguration(Main plugin) {
        File file = new File(plugin.getDataFolder(), "config.yml");
        try {
            plugin.getConfiguration().save(file);


        } catch (IOException ex) {
            Logger.getLogger(CEditHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        plugin.reloadConfigAlt();
    }
}
