package us.discovr.portalplugin;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.World;
import us.discovr.portalplugin.PortalPlugin;

public class PPListenerv2 extends PlayerListener {
	private final PortalPlugin plugin;

    public PPListenerv2(PortalPlugin instance) { this.plugin = instance; }
    
	public int getEncodeVal(int X, int Z, int Y, World w) {
		int id = w.getBlockTypeIdAt(X, Y, Z);
		switch (id) {
			case 5:  return 1; 
			case 17: return 2; 
			case 4:  return 3;
			case 58: return 4;
			case 61: return 5;
			//case 54: return 6; //Chest is a 6?
			case 47: return 6;
			case 20: return 7;
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
		for(int i = 0; i < codenum; i++)
			{ total += code[i]*Math.pow(8, (codenum-1)-i); }
		return (int) Math.ceil(total);
	}
	
	@Override
	public void onPlayerQuit(PlayerQuitEvent event) {
		plugin.lastposition.remove(event.getPlayer());
	}
	
	@Override
    public void onPlayerMove(PlayerMoveEvent event) {

    	if(event.isCancelled()) return;
    	
    	Location to = event.getTo();

    	World w = event.getPlayer().getWorld();
    	plugin.lastposition.put(event.getPlayer(), to);
    	//System.out.println("Player " + event.getPlayer().getName() + " moved a block");
    	
    	int modX = (int)(to.getX() < 0 ? to.getX()-1 : to.getX());
    	int modZ = (int)(to.getZ() < 0 ? to.getZ()-1 : to.getZ());
    	//event.getPlayer().sendMessage("X: "+modX+" Z: "+modZ);
    	
		if(w.getBlockTypeIdAt(modX, (int)to.getY(), modZ)==70 ||
				w.getBlockTypeIdAt(modX, (int)to.getY(), modZ) == 90) {
			
			//event.getPlayer().sendMessage("Button Pushed");
			//int orientationlevel = (int) to.getY()-1;
			int orientationlevel = (w.getBlockTypeIdAt(modX, (int)(to.getY()), modZ)==90 ?
					(int)(to.getY()-2) : (int)(to.getY()-1)); //improved
			//check for jukebox
			int readdirection = -1; //0 is for +x 1 is +z 2 is -x 3 is -z
			if(w.getBlockTypeIdAt(modX-1, orientationlevel, modZ)==84)
				readdirection = 0;
			if(w.getBlockTypeIdAt(modX+1, orientationlevel, modZ)==84)
				readdirection = 2;
			if(w.getBlockTypeIdAt(modX, orientationlevel, modZ+1)==84)
				readdirection = 3;
			if(w.getBlockTypeIdAt(modX, orientationlevel, modZ-1)==84)
				readdirection = 1;
			//event.getPlayer().sendMessage("read_direction: "+readdirection);
			
			if(readdirection == -1)
				return;
			else {
				float rotation = 0;
				int options = 0;
				boolean yset = false;
				int[] code1 = new int[5];
				int[] code2 = new int[5];
				int[] codeY = new int[5];
				int[] oX = new int[3];
				int[] oZ = new int[3];
				int xcodenum = 0;
				int scode1 = 1;
				int scode2 = 1;
				
				switch (readdirection) {
				case -1: //no real use for this
					return;
				case 0:
					oX[0] = modX+1;
					oZ[0] = modZ;
					oX[1] = modX;
					oZ[1] = modZ;
					oX[2] = modX-1;
					oZ[2] = modZ;
					scode1 = -1;
					xcodenum = 1;
					if(find2dir(oX[1], oZ[1], orientationlevel-3,w) != -1) scode2 = -1; else scode2 = 1;
					if(w.getBlockTypeIdAt(oX[2], orientationlevel-1, modZ)!=0) { yset = true; }
					break;
				case 1:
					oX[0] = modX;
					oZ[0] = modZ+1;
					oX[1] = modX;
					oZ[1] = modZ;
					oX[2] = modX;
					oZ[2] = modZ-1;
					scode1 = -1;
					xcodenum = 2;
					if(find2dir(oX[1], oZ[1], orientationlevel-3,w) != -1) scode2 = -1; else scode2 = 1;
					if(w.getBlockTypeIdAt(modX, orientationlevel-1, oZ[2])!=0) { yset = true; }
					break;
				case 2:
					oX[0] = modX-1;
					oZ[0] = modZ;
					oX[1] = modX;
					oZ[1] = modZ;
					oX[2] = modX+1;
					oZ[2] = modZ;
					scode1 = 1;
					xcodenum = 1;
					if(find2dir(oX[1], oZ[1], orientationlevel-3,w) != -1) scode2 = 1; else scode2 = -1;
					if(w.getBlockTypeIdAt(oX[2], orientationlevel-1, modZ)!=0) { yset = true; }
					break;
				case 3:
					oX[0] = modX;
					oZ[0] = modZ-1;
					oX[1] = modX;
					oZ[1] = modZ;
					oX[2] = modX;
					oZ[2] = modZ+1;
					scode1 = 1;
					xcodenum = 2;
					if(find2dir(oX[1], oZ[1], orientationlevel-3,w) != -1) scode2 = 1; else scode2 = -1;
					if(w.getBlockTypeIdAt(modX, orientationlevel-1, oZ[2])!=0) { yset = true; }
					break;
				}
				//event.getPlayer().sendMessage("oX: "+oX[0]+" "+oX[1]+" "+oX[2]);
				//event.getPlayer().sendMessage("oZ: "+oZ[0]+" "+oZ[1]+" "+oZ[2]);
				code1[0] = getEncodeVal(oX[0], oZ[0], orientationlevel, w);
				code1[1] = getEncodeVal(oX[0], oZ[0], orientationlevel-1, w);
				code1[2] = getEncodeVal(oX[0], oZ[0], orientationlevel-2, w);
				code1[3] = getEncodeVal(oX[0], oZ[0], orientationlevel-3, w);
				
				rotation = getEncodeVal(oX[0], oZ[0], orientationlevel-4, w)*45;
				
				code2[0] = getEncodeVal(oX[1], oZ[1], orientationlevel-1, w);
				code2[1] = getEncodeVal(oX[1], oZ[1], orientationlevel-2, w);
				code2[2] = getEncodeVal(oX[1], oZ[1], orientationlevel-3, w);
				code2[3] = getEncodeVal(oX[1], oZ[1], orientationlevel-4, w);
				
				//event.getPlayer().sendMessage("yset: "+yset);
				if(yset) {
					codeY[0] = getEncodeVal(oX[2], oZ[2], orientationlevel-1, w);
					codeY[1] = getEncodeVal(oX[2], oZ[2], orientationlevel-2, w);
					codeY[2] = getEncodeVal(oX[2], oZ[2], orientationlevel-3, w);
					options  = getEncodeVal(oX[2], oZ[2], orientationlevel-4, w);
				}
				
				//event.getPlayer().sendMessage("code1: "+code1[0]+code1[1]+code1[2]+code1[3]);
				//event.getPlayer().sendMessage("code2: "+code2[0]+code2[1]+code2[2]+code2[3]);
				//event.getPlayer().sendMessage("codeY: "+codeY[0]+codeY[1]+codeY[2]);
				float goto1 = (convertbase(code1,4)*scode1)+0.5f;
				float goto2 = (convertbase(code2,4)*scode2)+0.5f;
				//event.getPlayer().sendMessage("goto1: "+goto1+" goto2: "+goto2);
				Location loc = new Location(w, 0,0,0);
				//event.getPlayer().sendMessage("xcodenum: "+xcodenum);
				if(xcodenum == 1) {
					loc.setX(modX+goto1);
					loc.setZ(modZ+goto2);
				} else if(xcodenum == 2) {
					loc.setX(modX+goto2);
					loc.setZ(modZ+goto1);
				} else return; //Just in case
				int gotoY = 126;
				if(yset) gotoY = 164-(convertbase(codeY,3));
				loc.setY(gotoY);
				//event.getPlayer().sendMessage("X: "+loc.getX()+" Y: "+loc.getY()+" Z: "+loc.getZ());
				loc.setPitch(event.getPlayer().getLocation().getPitch());
				
				if(options == 0 || options == 2) loc.setYaw(event.getPlayer().getLocation().getYaw() + rotation);
				else loc.setYaw(rotation);
				
				int limit = plugin.getConfiguration().getInt("border-limit", 1000);
				if(Math.abs(loc.getX()-event.getPlayer().getWorld().getSpawnLocation().getX())<=limit 
						&& Math.abs(loc.getZ()-event.getPlayer().getWorld().getSpawnLocation().getZ())<=limit) {
					event.getPlayer().sendMessage(ChatColor.DARK_GRAY+"Teleporting to "+loc.getX()+" "+loc.getY()+" "+loc.getZ());
					event.setTo(loc);
					//event.getPlayer().teleportTo(loc);
					if(!event.getPlayer().teleport(loc)) {
						event.getPlayer().sendMessage(ChatColor.RED+"Something went terribly wrong with the teleport! It didn't work!");
					}
				} else {
					event.getPlayer().sendMessage(ChatColor.RED+"Cannot Teleport you to "+loc.getX()+" "+
							loc.getY()+" "+loc.getZ());
				}
			}
		}
		return;
    }
}