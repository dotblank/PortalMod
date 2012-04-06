package com.precipicegames.portalplugin;

import java.util.HashMap;
import java.util.logging.Level;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import PPPlayerListener.PortalPlayerListener;
import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;

public class PortalPlugin extends JavaPlugin {

    public FileConfiguration config;
    private File configFile = new File(this.getDataFolder(), "config.yml");
    private final PortalPlayerListener listener = new PortalPlayerListener(this);

    public final HashMap<Player, Location> lastposition = new HashMap<Player, Location>();
    public PluginDescriptionFile pdf;

    @Override
    public void onDisable() {
        System.out.println("[" + pdf.getName() + "] disabled!");
    }

    @Override
    public void onEnable() {
        pdf = this.getDescription();

        // Register our events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(listener, this);


        getServer().getLogger().log(Level.INFO, "{0} version {1} is enabled!", new Object[]{pdf.getName(), pdf.getVersion()});
        //System.out.println("["+pdf.getName()+"] portal distance limit: "+limit);
        config = getConfig();
    }
}