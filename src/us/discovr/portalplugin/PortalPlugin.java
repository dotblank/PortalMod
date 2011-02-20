package us.discovr.portalplugin;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import PPPlayerListener.PortalPlayerListener;

public class PortalPlugin extends JavaPlugin {
    private final PortalPlayerListener listener = new PortalPlayerListener(this);
    public final HashMap<Player, Location> lastposition = new HashMap<Player, Location>();

    public PortalPlugin(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File folder, File plugin, ClassLoader cLoader) {
        super(pluginLoader, instance, desc, folder, plugin, cLoader);
        // TODO: Place any custom initialisation code here

        // NOTE: Event registration should be done in onEnable not here as all events are unregistered when a plugin is disabled
    }

    public void onDisable() {
        // TODO: Place any custom disable code here

        // NOTE: All registered events are automatically unregistered when a plugin is disabled

        // EXAMPLE: Custom code, here we just output some info so we can check all is well
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is disabled!" );
    }

    public void onEnable() {
		//etc.getLoader().addListener(PluginLoader.Hook.PLAYER_MOVE, listener ,this,PluginListener.Priority.HIGH);
		//etc.getLoader().addListener(PluginLoader.Hook.ARM_SWING, listener ,this,PluginListener.Priority.HIGH);
		//etc.getLoader().addListener(PluginLoader.Hook.BLOCK_CREATED, listener ,this,PluginListener.Priority.HIGH);
        // TODO: Place any custom enable code here including the registration of any events

        // Register our events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_MOVE, listener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_QUIT, listener, Priority.Normal, this);
        //pm.registerEvent(Event.Type.BLOCK_PHYSICS, blockListener, Priority.Normal, this);
        //pm.registerEvent(Event.Type.BLOCK_CANBUILD, blockListener, Priority.Normal, this);

        // EXAMPLE: Custom code, here we just output some info so we can check all is well
        PluginDescriptionFile pdfFile = this.getDescription();
        int limit = getConfiguration().getInt("border-limit", 1000);
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
        System.out.println( pdfFile.getName() + " portal distance limit: " + limit );
    }
}
