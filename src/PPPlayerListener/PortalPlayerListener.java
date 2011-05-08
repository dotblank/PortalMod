package PPPlayerListener;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.World;
import us.discovr.portalplugin.PortalPlugin;

public class PortalPlayerListener extends PlayerListener {
    private final PortalPlugin plugin;

    public PortalPlayerListener(PortalPlugin instance) {
	plugin = instance;
    }
	public int getEncodeVal(int X, int Z, int Y, World w) {
		int id = w.getBlockTypeIdAt(X, Y, Z);
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
			byte val = w.getBlockAt(X, Y, Z).getData();
			if(val < 8 && val >0)
				return val;
			else
				return 0;
		}
		default: return 0;
		}
	}
	
    private int find2dir(int x, int z , int y, World w) {
	int dir = -1; //0 is for +x 1 is +z 2 is -x 3 is -z
	if(w.getBlockTypeIdAt(x-1, y, z)==84 || w.getBlockTypeIdAt(x-2, y, z)==84)
		dir = 0;
	if(w.getBlockTypeIdAt(x+1, y, z)==84 || w.getBlockTypeIdAt(x+2, y, z)==84)
		dir = 2;
	if(w.getBlockTypeIdAt(x, y, z+1)==84 || w.getBlockTypeIdAt(x, y, z+2)==84)
		dir = 3;
	if(w.getBlockTypeIdAt(x, y, z-1)==84 || w.getBlockTypeIdAt(x, y, z-2)==84)
		dir = 1;
	return dir;

    }
    private int convertbase(int[] code,int codenum) {
	int total = 0;
	for(int i = 0; i < codenum; i++) {
	    total += code[i]*Math.pow(8, (codenum-1)-i);
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
	    int scode1 = 1;
	    int scode2 = 1;
	    //check for jukebox
	    int readdirection = -1; //0 is for +x 1 is +z 2 is -x 3 is -z
	    if(w.getBlockTypeIdAt((int)to.getX(), (int)to.getY(), (int)to.getZ())==90)
	    {
		orientationlevel = (int) to.getY()-2;
		if(w.getBlockTypeIdAt(modX-1, orientationlevel, modZ)==84)
		    readdirection = 0;
		if(w.getBlockTypeIdAt(modX+1, orientationlevel, modZ)==84)
		    readdirection = 2;
		if(w.getBlockTypeIdAt(modX, orientationlevel, modZ+1)==84)
		    readdirection = 3;
		if(w.getBlockTypeIdAt(modX, orientationlevel, modZ-1)==84)
		    readdirection = 1;
		if(readdirection == -1)
		    orientationlevel = (int) to.getY()-1;
	    }
	    if(w.getBlockTypeIdAt(modX-1, orientationlevel, modZ)==84)
		readdirection = 0;
	    if(w.getBlockTypeIdAt(modX+1, orientationlevel, modZ)==84)
		readdirection = 2;
	    if(w.getBlockTypeIdAt(modX, orientationlevel, modZ+1)==84)
		readdirection = 3;
	    if(w.getBlockTypeIdAt(modX, orientationlevel, modZ-1)==84)
		readdirection = 1;

	    if(readdirection == -1)
		return;
	    else
	    {
		float rotation = 0;
		int options = 0;
		boolean yset =false;
		int[] code1 = new int[5];
		int[] code2 = new int[5];
		int[] codeY = new int[5];
		int xcodenum=0;
		switch (readdirection){
		case -1:
		    return; 
		case 0:
		    code1[0] = getEncodeVal(modX+1,modZ,orientationlevel,w);
		    code1[1] = getEncodeVal(modX+1,modZ,orientationlevel-1,w);
		    code1[2] = getEncodeVal(modX+1,modZ,orientationlevel-2,w);
		    code1[3] = getEncodeVal(modX+1,modZ,orientationlevel-3,w);

		    rotation = getEncodeVal(modX+1,modZ,orientationlevel-4,w)*45;
		    scode1 = -1;
		    xcodenum = 1;
		    code2[0] = getEncodeVal(modX,modZ,orientationlevel-1,w);
		    code2[1] = getEncodeVal(modX,modZ,orientationlevel-2,w);
		    code2[2] = getEncodeVal(modX,modZ,orientationlevel-3,w);
		    code2[3] = getEncodeVal(modX,modZ,orientationlevel-4,w);
		    if(find2dir(modX, modZ, orientationlevel-3,w) != -1)
			scode2 = -1;
		    else
			scode2 = 1;

		    if(w.getBlockTypeIdAt(modX-1, orientationlevel-1, modZ)!=0)
		    {
			codeY[0] = getEncodeVal(modX-1,modZ,orientationlevel-1,w);
			codeY[1] = getEncodeVal(modX-1,modZ,orientationlevel-2,w);
			codeY[2] = getEncodeVal(modX-1,modZ,orientationlevel-3,w);
			options = getEncodeVal(modX-1,modZ,orientationlevel-4,w);
			yset =true;
		    }
		    break;
		case 1:
		    code1[0] = getEncodeVal(modX,modZ+1,orientationlevel,w);
		    code1[1] = getEncodeVal(modX,modZ+1,orientationlevel-1,w);
		    code1[2] = getEncodeVal(modX,modZ+1,orientationlevel-2,w);
		    code1[3] = getEncodeVal(modX,modZ+1,orientationlevel-3,w);

		    rotation = getEncodeVal(modX,modZ+1,orientationlevel-4,w)*45;
		    scode1 = -1;
		    xcodenum = 2;
		    code2[0] = getEncodeVal(modX,modZ,orientationlevel-1,w);
		    code2[1] = getEncodeVal(modX,modZ,orientationlevel-2,w);
		    code2[2] = getEncodeVal(modX,modZ,orientationlevel-3,w);
		    code2[3] = getEncodeVal(modX,modZ,orientationlevel-4,w);
		    if(find2dir(modX, modZ, orientationlevel-3,w) != -1)
			scode2 = -1;
		    else
			scode2 = 1;

		    if(w.getBlockTypeIdAt(modX, orientationlevel-1, modZ-1)!=0)
		    {
			codeY[0] = getEncodeVal(modX,modZ-1,orientationlevel-1,w);
			codeY[1] = getEncodeVal(modX,modZ-1,orientationlevel-2,w);
			codeY[2] = getEncodeVal(modX,modZ-1,orientationlevel-3,w);
			options = getEncodeVal(modX,modZ-1,orientationlevel-4,w);
			yset =true;
		    }
		    break;
		case 2:
		    code1[0] = getEncodeVal(modX-1,modZ,orientationlevel,w);
		    code1[1] = getEncodeVal(modX-1,modZ,orientationlevel-1,w);
		    code1[2] = getEncodeVal(modX-1,modZ,orientationlevel-2,w);
		    code1[3] = getEncodeVal(modX-1,modZ,orientationlevel-3,w);

		    rotation = getEncodeVal(modX-1,modZ,orientationlevel-4,w)*45;
		    scode1 = 1;
		    xcodenum = 1;
		    code2[0] = getEncodeVal(modX,modZ,orientationlevel-1,w);
		    code2[1] = getEncodeVal(modX,modZ,orientationlevel-2,w);
		    code2[2] = getEncodeVal(modX,modZ,orientationlevel-3,w);
		    code2[3] = getEncodeVal(modX,modZ,orientationlevel-4,w);
		    if(find2dir(modX, modZ, orientationlevel-3,w) != -1)
			scode2 = 1;
		    else
			scode2 = -1;

		    if(w.getBlockTypeIdAt(modX+1, orientationlevel-1, modZ)!=0)
		    {
			codeY[0] = getEncodeVal(modX+1,modZ,orientationlevel-1,w);
			codeY[1] = getEncodeVal(modX+1,modZ,orientationlevel-2,w);
			codeY[2] = getEncodeVal(modX+1,modZ,orientationlevel-3,w);
			options = getEncodeVal(modX+1,modZ,orientationlevel-4,w);
			yset =true;
		    }
		    break;
		case 3:
		    code1[0] = getEncodeVal(modX,modZ-1,orientationlevel,w);
		    code1[1] = getEncodeVal(modX,modZ-1,orientationlevel-1,w);
		    code1[2] = getEncodeVal(modX,modZ-1,orientationlevel-2,w);
		    code1[3] = getEncodeVal(modX,modZ-1,orientationlevel-3,w);


		    rotation = getEncodeVal(modX,modZ-1,orientationlevel-4,w)*45;
		    scode1 = 1;
		    xcodenum = 2;
		    code2[0] = getEncodeVal(modX,modZ,orientationlevel-1,w);
		    code2[1] = getEncodeVal(modX,modZ,orientationlevel-2,w);
		    code2[2] = getEncodeVal(modX,modZ,orientationlevel-3,w);
		    code2[3] = getEncodeVal(modX,modZ,orientationlevel-4,w);
		    if(find2dir(modX, modZ, orientationlevel-3,w) != -1)
			scode2 = 1;
		    else
			scode2 = -1;

		    if(w.getBlockTypeIdAt(modX, orientationlevel-1, modZ+1)!=0)
		    {
			codeY[0] = getEncodeVal(modX,modZ+1,orientationlevel-1,w);
			codeY[1] = getEncodeVal(modX,modZ+1,orientationlevel-2,w);
			codeY[2] = getEncodeVal(modX,modZ+1,orientationlevel-3,w);
			options = getEncodeVal(modX,modZ+1,orientationlevel-4,w);
			yset = true;
		    }
		    break;
		}
		int goto1 = convertbase(code1,4)*scode1;
		int goto2 = convertbase(code2,4)*scode2;
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
		int gotoY = 126;
		if(yset)
		    gotoY = 164-convertbase(codeY,3);
		loc.setY(gotoY);
		if(options == 0 || options == 2)
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
		/*
		int limit = plugin.getConfiguration().getInt("border-limit", 1000);
		if(Math.abs(loc.getX()-event.getPlayer().getWorld().getSpawnLocation().getX())<=limit 
			&& Math.abs(loc.getZ()-event.getPlayer().getWorld().getSpawnLocation().getZ())<=limit)
		{
*/
		    event.getPlayer().sendMessage(ChatColor.DARK_GRAY+"Teleporting to "+loc.getX()+" "+loc.getY()+" "+loc.getZ());
			//event.getPlayer().teleportTo(loc);
			if(!event.getPlayer().teleport(loc)) {
				event.getPlayer().sendMessage(ChatColor.RED+"Something went terribly wrong with the teleport! It didn't work!");
				return;
			}
			event.setTo(loc);
		/*
		} else {
			event.getPlayer().sendMessage(ChatColor.RED+"Cannot Teleport you to "+loc.getX()+" "+
					loc.getY()+" "+loc.getZ());
*/
		}

	    }
	//}

	return;
    }
}