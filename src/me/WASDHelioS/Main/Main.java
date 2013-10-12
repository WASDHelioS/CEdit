/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.WASDHelioS.Main;

import java.io.File;
import me.WASDHelioS.Handler.ConfigHandler;
import me.WASDHelioS.Handler.CEditHandler;
import me.WASDHelioS.Listener.CommandPreProcessListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Nick
 */
public class Main extends JavaPlugin {

    private FileConfiguration config;
    private ConfigHandler cfH = new ConfigHandler(this);

    /**
     * refreshes the config without sending a confirm message (Useful in reloading).
     */
    public void onEnableNoSending() {
        createConfig();
        reloadConfig();
        config.options().copyDefaults(true);
        saveDefaultConfig();
    }

    /**
     * Refreshes the config with confirmation.
     */
    public void onEnableEss() {

        createConfig();
        reloadConfig();

        getLogger().info("[CEdit] is now enabled!");

        config.options().copyDefaults(true);
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        config = this.getConfig();
        onEnableEss();

        getServer().getPluginManager().registerEvents(new CommandPreProcessListener(this), this);

        getCommand("CEdit").setExecutor(new CEditHandler(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("[CEdit] is now disabled!");
    }

    /**
     * Gets current configuration.
     * @return Current configuration.
     */
    public FileConfiguration getConfiguration() {
        return config;
    }

    /**
     * Gets this instance of ConfigHandler.
     * @return ConfigHandler.
     */
    public ConfigHandler getConfigHandler() {
        return cfH;
    }

    /**
     * Creates a new config IF IT HAS TO. 
     * Either it be that the current config file contains errors (empty values)
     * Or the config doesn't exist locally.
     */
    private void createConfig() {
        File configfile = new File(getDataFolder(), "config.yml");
        if (!configfile.exists()) {
            this.config.options().copyDefaults();
            this.saveDefaultConfig();

            Bukkit.getServer().broadcast(ChatColor.GREEN + "[CEdit] A NEW CONFIG FILE HAS BEEN CREATED!!", "cedit.*");

        } else if (cfH.isConfigEmptyValues(config)) {
            resetConfig();

            Bukkit.getServer().broadcast(ChatColor.RED + "[CEdit] YOUR CONFIG FILE HAS BEEN RESET DUE TO ERRORS! ", "cedit.*");

        }
    }

    /**
     * Resets the config by deleting the old file and creating a new one.
     */
    public void resetConfig() {
        File conf = new File(getDataFolder(), "config.yml");
        conf.delete();
        saveDefaultConfig();
        reloadConfig();
    }

    /**
     * Reloads the config differently from reloadConfig(). this one gets the
     * file in the plugin directory and loads that one. (In use for runtime
     * reloading.)
     */
    public void reloadConfigAlt() {
        File conf = new File(getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(conf);
    }
}
