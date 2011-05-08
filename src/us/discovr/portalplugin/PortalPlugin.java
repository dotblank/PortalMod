package us.discovr.portalplugin;

import java.util.HashMap;
import java.util.Properties;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import PPPlayerListener.PortalPlayerListener;

public class PortalPlugin extends JavaPlugin {
    private final PortalPlayerListener listener = new PortalPlayerListener(this);
    //private PPListenerv2 listener = new PPListenerv2(this); //New version
    public final HashMap<Player, Location> lastposition = new HashMap<Player, Location>();
    public PluginDescriptionFile pdf;
    protected Properties config;

    public void onDisable() { System.out.println("["+pdf.getName()+"] disabled!"); }

    public void onEnable() {
	pdf = this.getDescription();

	// Register our events
	PluginManager pm = getServer().getPluginManager();
	pm.registerEvent(Event.Type.PLAYER_MOVE, listener, Priority.Monitor, this);
	pm.registerEvent(Event.Type.PLAYER_QUIT, listener, Priority.Normal, this);

	// EXAMPLE: Custom code, here we just output some info so we can check all is well
	//int limit = getConfiguration().getInt("border-limit", 1000);
	System.out.println(pdf.getName()+" version "+pdf.getVersion()+" is enabled!");
	//System.out.println("["+pdf.getName()+"] portal distance limit: "+limit);
    }
}
