package PPPlayerListener;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PortalSafeTeleporter extends Thread {
	private Player p;
	private Location destination;
	private PortalPlayerListener parent;
	private int attempts;
	PortalSafeTeleporter(Player player, Location l,PortalPlayerListener s)
	{
		super();
		p = player;
		destination = l;
		attempts = 0;
		parent =s;
	}
	public void run()
	{
		int cx = destination.getBlockX() >> 4;
		int cz = destination.getBlockZ() >> 4;
		if(attempts > 10)
		{
			parent.transitComplete(p);
			//Failed miserably
			return;
		}
		if(destination.getWorld().isChunkLoaded(cx, cz))
		{
			if(destination.getY() == 255)
			{
				destination.setY(destination.getWorld().getHighestBlockYAt(destination));
			}
			p.teleport(destination);
			parent.transitComplete(p);
		}
		else
		{
			destination.getWorld().loadChunk(cx, cz);
			if(destination.getWorld().isChunkLoaded(cx, cz))
			{
				if(destination.getY() == 255)
				{
					destination.setY(destination.getWorld().getHighestBlockYAt(destination));
				}
				p.teleport(destination);
				parent.transitComplete(p);
			}
			else
			{
				attempts++;
				try {
					sleep(200);
				} catch (InterruptedException e) {
					//Sleep interrupted must be terminating program
					attempts = 9000;
				}
				run();
			}
		}
	}

}
