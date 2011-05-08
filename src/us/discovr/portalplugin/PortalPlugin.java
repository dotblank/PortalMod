package us.discovr.portalplugin;

import java.util.HashMap;

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
    public final HashMap<Player, Location> lastposition = new HashMap<Player, Location>();
    public PluginDescriptionFile pdfFile;

    public void onDisable() { System.out.println("["+pdfFile.getName()+"] disabled!"); }

    public void onEnable() {
    	pdfFile = this.getDescription();
		//etc.getLoader().addListener(PluginLoader.Hook.PLAYER_MOVE, listener ,this,PluginListener.Priority.HIGH);
		//etc.getLoader().addListener(PluginLoader.Hook.ARM_SWING, listener ,this,PluginListener.Priority.HIGH);
		//etc.getLoader().addListener(PluginLoader.Hook.BLOCK_CREATED, listener ,this,PluginListener.Priority.HIGH);

        // Register our events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_MOVE, listener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_QUIT, listener, Priority.Normal, this);
        //pm.registerEvent(Event.Type.BLOCK_PHYSICS, blockListener, Priority.Normal, this);
        //pm.registerEvent(Event.Type.BLOCK_CANBUILD, blockListener, Priority.Normal, this);

        // EXAMPLE: Custom code, here we just output some info so we can check all is well
        int limit = getConfiguration().getInt("border-limit", 1000);
        System.out.println(pdfFile.getName()+" version "+pdfFile.getVersion()+" is enabled!");
        System.out.println("["+pdfFile.getName()+"] portal distance limit: "+limit);
    }
}
