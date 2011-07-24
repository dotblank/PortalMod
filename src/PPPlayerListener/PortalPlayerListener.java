package PPPlayerListener;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.World;

import com.precipicegames.portalplugin.PortalPlugin;

public class PortalPlayerListener extends PlayerListener {
    private final PortalPlugin plugin;
    private enum Mode {Legacy,Wool};

    public PortalPlayerListener(PortalPlugin instance) {
	plugin = instance;
    }
	public int getEncodeVal(Mode m,int X, int Z, int Y, World w) {
		int id = w.getBlockTypeIdAt(X, Y, Z);
		if(m == Mode.Legacy)
		{
			//int id = w.getBlockTypeIdAt(X, Y, Z);
			switch (id){
			case 3: return 0;
			case 2: return 0;
			case 5: return 1; 
			case 17: return 2;
			case 4: return 3;
			case 58: return 4;
			case 61: return 5;
			case 54: return 6;
			case 47: return 6;
			case 20: return 7;
			case 35:
			{
				Byte b =  new Byte(w.getBlockAt(X, Y, Z).getData());
				int val = b.intValue();
				if(val < 8 && val > 0)
					return val;
				else
					return 0;
			}
			default: return 0;
			}
		} else if (m == Mode.Wool)
		{	
			//int id = w.getBlockTypeIdAt(X, Y, Z);
			switch (id){
			case 35:
			{
				Byte b =  new Byte(w.getBlockAt(X, Y, Z).getData());
				int val = b.intValue();
				return val;
			}
			default: return 0;
			}
		}
		return 0;
	}
	
	private int readColumn(int x,int z, int y, int depth, Mode m, World w)
	{
		int[] codes = new int[depth];
		for(int i = 0; i < depth; i++)
		{
			codes[i] = getEncodeVal(m,x,z,y-i,w);
		}
		return convertbase(m,codes,depth);
	}
	
    private int find2dir(Mode m,int x, int z , int y, World w) {
    int anchor = 84;
    if(m == Mode.Wool)
    	anchor = 19;
	int dir = -1; //0 is for +x 1 is +z 2 is -x 3 is -z
	if(w.getBlockTypeIdAt(x-1, y, z)==anchor || w.getBlockTypeIdAt(x-2, y, z)==anchor)
		dir = 0;
	if(w.getBlockTypeIdAt(x+1, y, z)==anchor || w.getBlockTypeIdAt(x+2, y, z)==anchor)
		dir = 2;
	if(w.getBlockTypeIdAt(x, y, z+1)==anchor || w.getBlockTypeIdAt(x, y, z+2)==anchor)
		dir = 3;
	if(w.getBlockTypeIdAt(x, y, z-1)==anchor || w.getBlockTypeIdAt(x, y, z-2)==anchor)
		dir = 1;
	return dir;

    }
    private int convertbase(Mode m, int[] code,int codenum) {
	int total = 0;
	int base = 8;
	if(m == Mode.Wool)
		base = 16;
	for(int i = 0; i < codenum; i++) {
	    total += code[i]*Math.pow(base, (codenum-1)-i);
	}
	return (int) Math.ceil(total);
    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
	plugin.lastposition.remove(event.getPlayer());
    }
    
    @Override
    public void onPlayerMove(PlayerMoveEvent event) {

	if(event.isCancelled())
	    return;
	Location to = event.getTo();
	Location lastPos = plugin.lastposition.get(event.getPlayer());

	if(lastPos!=null) {
	    //System.out.println("Player " + event.getPlayer().getName() + " moved to " + to);
	    if(Math.abs(to.getBlockX()-lastPos.getBlockX()) < 1)
		if(Math.abs(to.getBlockZ()-lastPos.getBlockZ()) < 1)
		    if(Math.abs(to.getBlockY()-lastPos.getBlockY()) < 1)
			return;
	}
	World w = event.getPlayer().getWorld();
	plugin.lastposition.put(event.getPlayer(), to);
	//System.out.println("Player " + event.getPlayer().getName() + " moved a block");

    	int modX = (int)(to.getX() < 0 ? to.getX()-1 : to.getX());
    	int modZ = (int)(to.getZ() < 0 ? to.getZ()-1 : to.getZ());

	if(w.getBlockTypeIdAt(modX, (int)to.getY(), modZ)==70 || w.getBlockTypeIdAt(modX, (int)to.getY(), modZ) == 90)
	{

	    //System.out.println("Player pushed a button");
	    int orientationlevel = (int) to.getY()-1;
	    //int oX = modX;
	    //int oZ = modZ;
	    //Change elevation for nether portal
	    int readdirection = -1; //0 is for +x 1 is +z 2 is -x 3 is -z
	    if(w.getBlockTypeIdAt(modX, (int)to.getY(), modZ)==90)
	    {
	    	orientationlevel = (int) to.getY()-2;
	    }
	    

	    Mode mode = Mode.Legacy;
	    
	    if(readdirection == -1)
	    {
		    if(w.getBlockTypeIdAt(modX-1, orientationlevel, modZ)==19)
			readdirection = 0;
		    if(w.getBlockTypeIdAt(modX+1, orientationlevel, modZ)==19)
			readdirection = 2;
		    if(w.getBlockTypeIdAt(modX, orientationlevel, modZ+1)==19)
			readdirection = 3;
		    if(w.getBlockTypeIdAt(modX, orientationlevel, modZ-1)==19)
			readdirection = 1;
		    mode = Mode.Wool;
	    }
	    	

	    if(readdirection == -1)
		return;
	    else
	    {
		float rotation = 0;
		int xcodenum=0;
		int goto1 = 0;
		int goto2 = 0;
		int gotoY = 126;
		switch (readdirection){
		case -1:
		    return; 
		case 0:
			goto1 = -readColumn(modX+1,modZ, orientationlevel-1, 4, mode, w);
			rotation = readColumn(modX-1,modZ, orientationlevel-4, 1, mode, w)*45;
		    xcodenum = 1;
		    if(find2dir(mode,modX, modZ, orientationlevel-3,w) != -1)
		    goto2 = -readColumn(modX,modZ, orientationlevel-1, 4, mode, w);
		    else
			goto2 = readColumn(modX,modZ, orientationlevel-1, 4, mode, w);
		    
		    gotoY = 255 - readColumn(modX-1,modZ, orientationlevel-1, 2, mode, w);
		    break;
		case 1:
			goto1 = -readColumn(modX,modZ+1, orientationlevel-1, 4, mode, w);
			rotation = readColumn(modX,modZ-1, orientationlevel-4, 1, mode, w)*45;
		    xcodenum = 2;
		    if(find2dir(mode,modX, modZ, orientationlevel-3,w) != -1)
		    goto2 = -readColumn(modX,modZ, orientationlevel-1, 4, mode, w);
		    else
			goto2 = readColumn(modX,modZ, orientationlevel-1, 4, mode, w);
		    
		    gotoY = 255 - readColumn(modX,modZ-1, orientationlevel-1, 2, mode, w);
		    break;
		case 2:
			goto1 = readColumn(modX-1,modZ, orientationlevel-1, 4, mode, w);
			rotation = readColumn(modX+1,modZ, orientationlevel-4, 1, mode, w)*45;
		    xcodenum = 1;
		    if(find2dir(mode,modX, modZ, orientationlevel-3,w) != -1)
		    goto2 = readColumn(modX,modZ, orientationlevel-1, 4, mode, w);
		    else
			goto2 = -readColumn(modX,modZ, orientationlevel-1, 4, mode, w);
		    
		    gotoY = 255 - readColumn(modX+1,modZ, orientationlevel-1, 2, mode, w);
		    break;
		case 3:
			goto1 = readColumn(modX,modZ-1, orientationlevel-1, 4, mode, w);
			rotation = readColumn(modX,modZ+1, orientationlevel-4, 1, mode, w)*45;
		    xcodenum = 2;
		    if(find2dir(mode,modX, modZ, orientationlevel-3,w) != -1)
		    goto2 = readColumn(modX,modZ, orientationlevel-1, 4, mode, w);
		    else
			goto2 = -readColumn(modX,modZ, orientationlevel-1, 4, mode, w);
		    
		    gotoY = 255 - readColumn(modX,modZ+1, orientationlevel-1, 2, mode, w);
		    break;
		}
		Location loc = new Location(w, 0,0,0);
		if(xcodenum == 1)
		{
		    loc.setX(modX+goto1);
		    //loc.x = oX+goto1;
		    loc.setZ(modZ+goto2);
		    //loc.z = oZ+goto2;
		}
		if(xcodenum == 2)
		{
		    loc.setX(modX+goto2);
		    //loc.x = oX+goto2;
		    loc.setZ(modZ+goto1);
		    //loc.z = oZ+goto1;
		}
		if(gotoY <= 0)
			gotoY = 2;
		loc.setY(gotoY);
		if(rotation < 360)
		    loc.setYaw(event.getPlayer().getLocation().getYaw() + rotation);
		else
		    loc.setYaw(rotation);
		loc.setPitch(event.getPlayer().getLocation().getPitch());

		//if(loc.x < 0)
		loc.setX(loc.getX() +0.5f);
		//else
		//	loc.x -= 0.5f;
		//if(loc.z < 0)
		loc.setZ(loc.getZ() +0.5f);
		//else
		//	loc.z += 0.5f;
		if(gotoY == 255)
		{
			gotoY = w.getHighestBlockYAt(loc);
			loc.setY(gotoY + 0.5f);
		}

		
		int limit = plugin.getConfiguration().getInt("border-limit", 1000);
		Location center = event.getPlayer().getWorld().getSpawnLocation();
		if(plugin.getConfiguration().getBoolean("center-origin",false))
		{
			center = new Location(event.getPlayer().getWorld(),0,0,0);
		}
		if(Math.abs(loc.getX()-center.getX())<=limit 
			&& Math.abs(loc.getZ()-center.getZ())<=limit)
		{
		    event.getPlayer().sendMessage(ChatColor.DARK_GRAY+"Teleporting to "+loc.getX()+" "+loc.getY()+" "+loc.getZ());
			//event.getPlayer().teleportTo(loc);
			if(!event.getPlayer().teleport(loc)) {
				event.getPlayer().sendMessage(ChatColor.RED+"Something went terribly wrong with the teleport! It didn't work!");
				return;
			}
			event.setTo(loc);
		} else {
			event.getPlayer().sendMessage(ChatColor.RED+"Cannot Teleport you to "+loc.getX()+" "+
					loc.getY()+" "+loc.getZ());
		}

	    }
	}
	return;
    }
}