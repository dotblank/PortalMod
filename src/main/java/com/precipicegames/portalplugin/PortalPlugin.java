package com.precipicegames.portalplugin;

import java.util.HashMap;
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
    //private PPListenerv2 listener = new PPListenerv2(this); //New version
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

        // EXAMPLE: Custom code, here we just output some info so we can check all is well
        //int limit = getConfiguration().getInt("border-limit", 1000);
        getServer().getLogger().info(pdf.getName() + " version " + pdf.getVersion() + " is enabled!");
        //System.out.println("["+pdf.getName()+"] portal distance limit: "+limit);
        config = getConfig();
    }
}