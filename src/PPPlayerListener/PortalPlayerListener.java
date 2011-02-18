package PPPlayerListener;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.World;
import us.discovr.portalplugin.PortalPlugin;

public class PortalPlayerListener extends PlayerListener {
	private final PortalPlugin plugin;

    public PortalPlayerListener(PortalPlugin instance) {
        plugin = instance;
    }
	public int getEncodeVal(int X, int Z, int Y, World w){
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
		default: return 0;
		}
		
	}
	private int find2dir(int x, int z , int y, World w)
	{
		int dir = -1; //0 is for +x 1 is +z 2 is -x 3 is -z
		if(w.getBlockTypeIdAt(x-1, y, z)==84)
			dir = 0;
		if(w.getBlockTypeIdAt(x+1, y, z)==84)
			dir = 2;
		if(w.getBlockTypeIdAt(x, y, z+1)==84)
			dir = 3;
		if(w.getBlockTypeIdAt(x, y, z-1)==84)
			dir = 1;
		
		if(w.getBlockTypeIdAt(x-2, y, z)==84)
			dir = 0;
		if(w.getBlockTypeIdAt(x+2, y, z)==84)
			dir = 2;
		if(w.getBlockTypeIdAt(x, y, z+2)==84)
			dir = 3;
		if(w.getBlockTypeIdAt(x, y, z-2)==84)
			dir = 1;
		
		return dir;
		
	}
	private int convertbase(int[] code,int codenum)
	{
		int total = 0;
		for(int i = 0; i < codenum; i++)
		{
			total += code[i]*Math.pow(8, (codenum-1)-i);
		}
		return (int) Math.ceil(total);
	}
    public void onPlayerMove(PlayerMoveEvent event)
    {	

    	if(event.isCancelled())
    		return;
    	
    	Location to = event.getTo();
    	World w = event.getPlayer().getWorld();
		/*
		if(to.x < 0)
			to.x -= 1;
		if(to.getZ() < 0)
			to.getZ() -= 1;*/
		if(w.getBlockTypeIdAt((int)to.getX(), (int)to.getY(), (int)to.getZ())==70 || w.getBlockTypeIdAt((int)to.getX(), (int)to.getY(), (int)to.getZ()) == 90)
		{
			
			System.out.println("Player pushed a button");
			int orientationlevel = (int) to.getY()-1;
			int oX = (int)to.getX();
			int oZ = (int) to.getZ();
			int scode1 = 1;
			int scode2 = 1;
			//check for jukebox
			int readdirection = -1; //0 is for +x 1 is +z 2 is -x 3 is -z
			if(w.getBlockTypeIdAt((int)to.getX(), (int)to.getY(), (int)to.getZ())==90)
			{
				orientationlevel = (int) to.getY()-2;
				if(w.getBlockTypeIdAt(oX-1, orientationlevel, oZ)==84)
					readdirection = 0;
				if(w.getBlockTypeIdAt(oX+1, orientationlevel, oZ)==84)
					readdirection = 2;
				if(w.getBlockTypeIdAt(oX, orientationlevel, oZ+1)==84)
					readdirection = 3;
				if(w.getBlockTypeIdAt(oX, orientationlevel, oZ-1)==84)
					readdirection = 1;
				if(readdirection == -1)
					orientationlevel = (int) to.getY()-1;
			}
			if(w.getBlockTypeIdAt(oX-1, orientationlevel, oZ)==84)
				readdirection = 0;
			if(w.getBlockTypeIdAt(oX+1, orientationlevel, oZ)==84)
				readdirection = 2;
			if(w.getBlockTypeIdAt(oX, orientationlevel, oZ+1)==84)
				readdirection = 3;
			if(w.getBlockTypeIdAt(oX, orientationlevel, oZ-1)==84)
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
					code1[0] = getEncodeVal(oX+1,oZ,orientationlevel,w);
					code1[1] = getEncodeVal(oX+1,oZ,orientationlevel-1,w);
					code1[2] = getEncodeVal(oX+1,oZ,orientationlevel-2,w);
					code1[3] = getEncodeVal(oX+1,oZ,orientationlevel-3,w);
					
					rotation = getEncodeVal(oX+1,oZ,orientationlevel-4,w)*45;
					scode1 = -1;
					xcodenum = 1;
					code2[0] = getEncodeVal(oX,oZ,orientationlevel-1,w);
					code2[1] = getEncodeVal(oX,oZ,orientationlevel-2,w);
					code2[2] = getEncodeVal(oX,oZ,orientationlevel-3,w);
					code2[3] = getEncodeVal(oX,oZ,orientationlevel-4,w);
					if(find2dir(oX, oZ, orientationlevel-3,w) != -1)
						scode2 = -1;
					else
						scode2 = 1;
					
					if(w.getBlockTypeIdAt(oX-1, orientationlevel-1, oZ)!=0)
					{
						codeY[0] = getEncodeVal(oX-1,oZ,orientationlevel-1,w);
						codeY[1] = getEncodeVal(oX-1,oZ,orientationlevel-2,w);
						codeY[2] = getEncodeVal(oX-1,oZ,orientationlevel-3,w);
						options = getEncodeVal(oX-1,oZ,orientationlevel-4,w);
						yset =true;
					}
					break;
				case 1:
					code1[0] = getEncodeVal(oX,oZ+1,orientationlevel,w);
					code1[1] = getEncodeVal(oX,oZ+1,orientationlevel-1,w);
					code1[2] = getEncodeVal(oX,oZ+1,orientationlevel-2,w);
					code1[3] = getEncodeVal(oX,oZ+1,orientationlevel-3,w);
					
					rotation = getEncodeVal(oX,oZ+1,orientationlevel-4,w)*45;
					scode1 = -1;
					xcodenum = 2;
					code2[0] = getEncodeVal(oX,oZ,orientationlevel-1,w);
					code2[1] = getEncodeVal(oX,oZ,orientationlevel-2,w);
					code2[2] = getEncodeVal(oX,oZ,orientationlevel-3,w);
					code2[3] = getEncodeVal(oX,oZ,orientationlevel-4,w);
					if(find2dir(oX, oZ, orientationlevel-3,w) != -1)
						scode2 = -1;
					else
						scode2 = 1;
					
					if(w.getBlockTypeIdAt(oX, orientationlevel-1, oZ-1)!=0)
					{
						codeY[0] = getEncodeVal(oX,oZ-1,orientationlevel-1,w);
						codeY[1] = getEncodeVal(oX,oZ-1,orientationlevel-2,w);
						codeY[2] = getEncodeVal(oX,oZ-1,orientationlevel-3,w);
						options = getEncodeVal(oX,oZ-1,orientationlevel-4,w);
						yset =true;
					}
					break;
				case 2:
					code1[0] = getEncodeVal(oX-1,oZ,orientationlevel,w);
					code1[1] = getEncodeVal(oX-1,oZ,orientationlevel-1,w);
					code1[2] = getEncodeVal(oX-1,oZ,orientationlevel-2,w);
					code1[3] = getEncodeVal(oX-1,oZ,orientationlevel-3,w);
					
					rotation = getEncodeVal(oX-1,oZ,orientationlevel-4,w)*45;
					scode1 = 1;
					xcodenum = 1;
					code2[0] = getEncodeVal(oX,oZ,orientationlevel-1,w);
					code2[1] = getEncodeVal(oX,oZ,orientationlevel-2,w);
					code2[2] = getEncodeVal(oX,oZ,orientationlevel-3,w);
					code2[3] = getEncodeVal(oX,oZ,orientationlevel-4,w);
					if(find2dir(oX, oZ, orientationlevel-3,w) != -1)
						scode2 = 1;
					else
						scode2 = -1;
					
					if(w.getBlockTypeIdAt(oX+1, orientationlevel-1, oZ)!=0)
					{
						codeY[0] = getEncodeVal(oX+1,oZ,orientationlevel-1,w);
						codeY[1] = getEncodeVal(oX+1,oZ,orientationlevel-2,w);
						codeY[2] = getEncodeVal(oX+1,oZ,orientationlevel-3,w);
						options = getEncodeVal(oX+1,oZ,orientationlevel-4,w);
						yset =true;
					}
					break;
				case 3:
					code1[0] = getEncodeVal(oX,oZ-1,orientationlevel,w);
					code1[1] = getEncodeVal(oX,oZ-1,orientationlevel-1,w);
					code1[2] = getEncodeVal(oX,oZ-1,orientationlevel-2,w);
					code1[3] = getEncodeVal(oX,oZ-1,orientationlevel-3,w);
					
					
					rotation = getEncodeVal(oX,oZ-1,orientationlevel-4,w)*45;
					scode1 = 1;
					xcodenum = 2;
					code2[0] = getEncodeVal(oX,oZ,orientationlevel-1,w);
					code2[1] = getEncodeVal(oX,oZ,orientationlevel-2,w);
					code2[2] = getEncodeVal(oX,oZ,orientationlevel-3,w);
					code2[3] = getEncodeVal(oX,oZ,orientationlevel-4,w);
					if(find2dir(oX, oZ, orientationlevel-3,w) != -1)
						scode2 = 1;
					else
						scode2 = -1;
					
					if(w.getBlockTypeIdAt(oX, orientationlevel-1, oZ+1)!=0)
					{
						codeY[0] = getEncodeVal(oX,oZ+1,orientationlevel-1,w);
						codeY[1] = getEncodeVal(oX,oZ+1,orientationlevel-2,w);
						codeY[2] = getEncodeVal(oX,oZ+1,orientationlevel-3,w);
						options = getEncodeVal(oX,oZ+1,orientationlevel-4,w);
						yset = true;
					}
					break;
				}
				int goto1 = convertbase(code1,4)*scode1;
				int goto2 = convertbase(code2,4)*scode2;
				Location loc = new Location(w, 0,0,0);
				if(xcodenum == 1)
				{
					loc.setX(oX+goto1);
					//loc.x = oX+goto1;
					loc.setZ(oZ+goto2);
					//loc.z = oZ+goto2;
				}
				if(xcodenum == 2)
				{
					loc.setX(oX+goto2);
					//loc.x = oX+goto2;
					loc.setZ(oZ+goto1);
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
				int limit = plugin.getConfiguration().getInt("border-limit", 1000);
				if(Math.abs(loc.getX()-event.getPlayer().getWorld().getSpawnLocation().getX())<=limit 
						&& Math.abs(loc.getZ()-event.getPlayer().getWorld().getSpawnLocation().getZ())<=limit)
				{
					
					//if(!(options == 1 || options == 3))
						//installImprint(player, player.getLocation());
					event.setTo(loc);
					event.setFrom(loc);
					event.getPlayer().teleportTo(loc);
				}
				
			}
			}
		
		return;
    }
}