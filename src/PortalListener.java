import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PortalListener extends PluginListener {
	
	public final Logger a = Logger.getLogger("Minecraft");
	public int getEncodeVal(int X, int Z, int Y){
		int id = etc.getServer().getBlockIdAt(X, Y, Z);
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
	private int find2dir(int x, int z , int y)
	{
		int dir = -1; //0 is for +x 1 is +z 2 is -x 3 is -z
		if(etc.getServer().getBlockIdAt(x-1, y, z)==84)
			dir = 0;
		if(etc.getServer().getBlockIdAt(x+1, y, z)==84)
			dir = 2;
		if(etc.getServer().getBlockIdAt(x, y, z+1)==84)
			dir = 3;
		if(etc.getServer().getBlockIdAt(x, y, z-1)==84)
			dir = 1;
		
		if(etc.getServer().getBlockIdAt(x-2, y, z)==84)
			dir = 0;
		if(etc.getServer().getBlockIdAt(x+2, y, z)==84)
			dir = 2;
		if(etc.getServer().getBlockIdAt(x, y, z+2)==84)
			dir = 3;
		if(etc.getServer().getBlockIdAt(x, y, z-2)==84)
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
	/*public boolean onBlockCreate(Player player, Block blockPlaced, Block block, int itemInHand)
	{

		if(block.getType() == 89)
		{
			//System.out.println("Player hit brightstone");
			boolean createPortal = false;
			if(etc.getServer().getBlockIdAt(block.getX()+1, block.getY(), block.getZ()) == 84)
				createPortal = true;
			if(etc.getServer().getBlockIdAt(block.getX(), block.getY(), block.getZ()-1) == 84)
				createPortal = true;
			if(etc.getServer().getBlockIdAt(block.getX(), block.getY(), block.getZ()+1) == 84)
				createPortal = true;
			if(etc.getServer().getBlockIdAt(block.getX()-1, block.getY(), block.getZ()) == 84)
				createPortal = true;
			if(createPortal)
			{
				if(etc.getServer().getBlockIdAt(block.getX(), block.getY()+2, block.getZ()) == 0 && etc.getServer().getBlockIdAt(block.getX(), block.getY()+1, block.getZ()) == 0)
				{
					int x,y,z;
					x = block.getX();
					y =block.getY();
					z = block.getZ();
					Block p1 = new Block();
					p1.setX(x);
					p1.setY(y+1);
					p1.setZ(z);
					p1.setType(90);
					Block p2 = new Block();
					p2.setX(x);
					p2.setY(y+2);
					p2.setZ(z);
					p2.setType(90);
					
					etc.getServer().setBlock(p1);
					//etc.getServer().setBlock(p2);
					System.out.println("Created Portal");
				}
			}
		}
		
		return false;
	}*/
	public void onPlayerMove(Player player, Location from, Location to)
	{
		/*
		if(to.x < 0)
			to.x -= 1;
		if(to.z < 0)
			to.z -= 1;*/
		if(etc.getServer().getBlockIdAt((int)to.x, (int)to.y, (int)to.z)==70 || etc.getServer().getBlockIdAt((int)to.x, (int)to.y, (int)to.z) == 90)
		{
			
			System.out.println("Player pushed a button");
			int orientationlevel = (int) to.y-1;
			int oX = (int)to.x;
			int oZ = (int) to.z;
			int scode1 = 1;
			int scode2 = 1;
			//check for jukebox
			int readdirection = -1; //0 is for +x 1 is +z 2 is -x 3 is -z
			if(etc.getServer().getBlockIdAt((int)to.x, (int)to.y, (int)to.z)==90)
			{
				orientationlevel = (int) to.y-2;
				if(etc.getServer().getBlockIdAt(oX-1, orientationlevel, oZ)==84)
					readdirection = 0;
				if(etc.getServer().getBlockIdAt(oX+1, orientationlevel, oZ)==84)
					readdirection = 2;
				if(etc.getServer().getBlockIdAt(oX, orientationlevel, oZ+1)==84)
					readdirection = 3;
				if(etc.getServer().getBlockIdAt(oX, orientationlevel, oZ-1)==84)
					readdirection = 1;
				if(readdirection == -1)
					orientationlevel = (int) to.y-1;
			}
			if(etc.getServer().getBlockIdAt(oX-1, orientationlevel, oZ)==84)
				readdirection = 0;
			if(etc.getServer().getBlockIdAt(oX+1, orientationlevel, oZ)==84)
				readdirection = 2;
			if(etc.getServer().getBlockIdAt(oX, orientationlevel, oZ+1)==84)
				readdirection = 3;
			if(etc.getServer().getBlockIdAt(oX, orientationlevel, oZ-1)==84)
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
					code1[0] = getEncodeVal(oX+1,oZ,orientationlevel);
					code1[1] = getEncodeVal(oX+1,oZ,orientationlevel-1);
					code1[2] = getEncodeVal(oX+1,oZ,orientationlevel-2);
					code1[3] = getEncodeVal(oX+1,oZ,orientationlevel-3);
					
					rotation = getEncodeVal(oX+1,oZ,orientationlevel-4)*45;
					scode1 = -1;
					xcodenum = 1;
					code2[0] = getEncodeVal(oX,oZ,orientationlevel-1);
					code2[1] = getEncodeVal(oX,oZ,orientationlevel-2);
					code2[2] = getEncodeVal(oX,oZ,orientationlevel-3);
					code2[3] = getEncodeVal(oX,oZ,orientationlevel-4);
					if(find2dir(oX, oZ, orientationlevel-3) != -1)
						scode2 = -1;
					else
						scode2 = 1;
					
					if(etc.getServer().getBlockIdAt(oX-1, orientationlevel-1, oZ)!=0)
					{
						codeY[0] = getEncodeVal(oX-1,oZ,orientationlevel-1);
						codeY[1] = getEncodeVal(oX-1,oZ,orientationlevel-2);
						codeY[2] = getEncodeVal(oX-1,oZ,orientationlevel-3);
						options = getEncodeVal(oX-1,oZ,orientationlevel-4);
						yset =true;
					}
					break;
				case 1:
					code1[0] = getEncodeVal(oX,oZ+1,orientationlevel);
					code1[1] = getEncodeVal(oX,oZ+1,orientationlevel-1);
					code1[2] = getEncodeVal(oX,oZ+1,orientationlevel-2);
					code1[3] = getEncodeVal(oX,oZ+1,orientationlevel-3);
					
					rotation = getEncodeVal(oX,oZ+1,orientationlevel-4)*45;
					scode1 = -1;
					xcodenum = 2;
					code2[0] = getEncodeVal(oX,oZ,orientationlevel-1);
					code2[1] = getEncodeVal(oX,oZ,orientationlevel-2);
					code2[2] = getEncodeVal(oX,oZ,orientationlevel-3);
					code2[3] = getEncodeVal(oX,oZ,orientationlevel-4);
					if(find2dir(oX, oZ, orientationlevel-3) != -1)
						scode2 = -1;
					else
						scode2 = 1;
					
					if(etc.getServer().getBlockIdAt(oX, orientationlevel-1, oZ-1)!=0)
					{
						codeY[0] = getEncodeVal(oX,oZ-1,orientationlevel-1);
						codeY[1] = getEncodeVal(oX,oZ-1,orientationlevel-2);
						codeY[2] = getEncodeVal(oX,oZ-1,orientationlevel-3);
						options = getEncodeVal(oX,oZ-1,orientationlevel-4);
						yset =true;
					}
					break;
				case 2:
					code1[0] = getEncodeVal(oX-1,oZ,orientationlevel);
					code1[1] = getEncodeVal(oX-1,oZ,orientationlevel-1);
					code1[2] = getEncodeVal(oX-1,oZ,orientationlevel-2);
					code1[3] = getEncodeVal(oX-1,oZ,orientationlevel-3);
					
					rotation = getEncodeVal(oX-1,oZ,orientationlevel-4)*45;
					scode1 = 1;
					xcodenum = 1;
					code2[0] = getEncodeVal(oX,oZ,orientationlevel-1);
					code2[1] = getEncodeVal(oX,oZ,orientationlevel-2);
					code2[2] = getEncodeVal(oX,oZ,orientationlevel-3);
					code2[3] = getEncodeVal(oX,oZ,orientationlevel-4);
					if(find2dir(oX, oZ, orientationlevel-3) != -1)
						scode2 = 1;
					else
						scode2 = -1;
					
					if(etc.getServer().getBlockIdAt(oX+1, orientationlevel-1, oZ)!=0)
					{
						codeY[0] = getEncodeVal(oX+1,oZ,orientationlevel-1);
						codeY[1] = getEncodeVal(oX+1,oZ,orientationlevel-2);
						codeY[2] = getEncodeVal(oX+1,oZ,orientationlevel-3);
						options = getEncodeVal(oX+1,oZ,orientationlevel-4);
						yset =true;
					}
					break;
				case 3:
					code1[0] = getEncodeVal(oX,oZ-1,orientationlevel);
					code1[1] = getEncodeVal(oX,oZ-1,orientationlevel-1);
					code1[2] = getEncodeVal(oX,oZ-1,orientationlevel-2);
					code1[3] = getEncodeVal(oX,oZ-1,orientationlevel-3);
					
					
					rotation = getEncodeVal(oX,oZ-1,orientationlevel-4)*45;
					scode1 = 1;
					xcodenum = 2;
					code2[0] = getEncodeVal(oX,oZ,orientationlevel-1);
					code2[1] = getEncodeVal(oX,oZ,orientationlevel-2);
					code2[2] = getEncodeVal(oX,oZ,orientationlevel-3);
					code2[3] = getEncodeVal(oX,oZ,orientationlevel-4);
					if(find2dir(oX, oZ, orientationlevel-3) != -1)
						scode2 = 1;
					else
						scode2 = -1;
					
					if(etc.getServer().getBlockIdAt(oX, orientationlevel-1, oZ+1)!=0)
					{
						codeY[0] = getEncodeVal(oX,oZ+1,orientationlevel-1);
						codeY[1] = getEncodeVal(oX,oZ+1,orientationlevel-2);
						codeY[2] = getEncodeVal(oX,oZ+1,orientationlevel-3);
						options = getEncodeVal(oX,oZ+1,orientationlevel-4);
						yset = true;
					}
					break;
				}
				int goto1 = convertbase(code1,4)*scode1;
				int goto2 = convertbase(code2,4)*scode2;
				Location loc = new Location();
				if(xcodenum == 1)
				{
					loc.x = oX+goto1;
					loc.z = oZ+goto2;
				}
				if(xcodenum == 2)
				{
					loc.x = oX+goto2;
					loc.z = oZ+goto1;
				}
				int gotoY = 126;
				if(yset)
					gotoY = 164-convertbase(codeY,3);
				loc.y = gotoY;
				if(options == 0 || options == 2)
					loc.rotX = player.getLocation().rotX + rotation;
				else
					loc.rotX = rotation;
				loc.rotY = player.getLocation().rotY;
				//if(loc.x < 0)
					loc.x += 0.5f;
				//else
				//	loc.x -= 0.5f;
				//if(loc.z < 0)
					loc.z += 0.5f;
				//else
				//	loc.z += 0.5f;
				a.log(Level.INFO,player.getName() + " used a portal!");
				if(Math.abs(loc.x-etc.getServer().getSpawnLocation().x)<=1910 && Math.abs(loc.z-etc.getServer().getSpawnLocation().z)<=1910)
				{
					//if(!(options == 1 || options == 3))
						//installImprint(player, player.getLocation());
					player.teleportTo(loc);
				}
				
			}
			

			}
		return;
	}
	private void installImprint(Player p, Location loc)
	{
		boolean addtolist = false;;
		Inventory cinv = p.getCraftingTable();
		Inventory inv = p.getInventory();
		if(!inv.hasItem(347, 1, 1) && !cinv.hasItem(347, 1, 1))
		{
			cinv.addItem(new Item(347,1));
			cinv.updateInventory();
			addtolist = true;
		}
		
		if(addtolist)
			backList.add(new PortBack(p.getName(),loc));
		return;
	}
	private void recall(Player p, PortBack b)
	{
		//System.out.println("recall test");
		if(System.currentTimeMillis() - b.time <= 120000)
			p.teleportTo(b.loc);
		else
			p.sendMessage("Portal reversal can only last 2 minutes");
		b.t.cancel();
	}
	public void onArmSwing(Player p)
	{
		if(p.getItemInHand() == 347)
		{
			p.getInventory().removeItem(new Item(347,1));
			p.getInventory().updateInventory();
			Iterator<PortBack>  i = backList.iterator();
			while(i.hasNext())
			{
				PortBack pback = i.next();
				if(pback.name == p.getName())
				{
					recall(p,pback);
					i.remove();
					return;
				}
			}
		}
	}
	public static ArrayList<PortBack> backList = new ArrayList<PortBack>();
	public class PortBack
	{
		public String name;
		public long time;
		public Location loc;
		public Timer t;
		public PortBack(String user,Location l)
		{
			name = user;
			time = System.currentTimeMillis();
			loc = l;
		}
		public PortBack(String user,Location l, long ti)
		{
			name = user;
			time = ti;
			loc = l;
		}
	}
	
/*
	private int translate(int rx, int rz, int y, int dir, int oX, int oZ) {
		int tmpx =0;
		int tmpz =0;
		if(dir == 0)
		{
			tmpx = oX+rz;
			tmpz = oZ-rx;
		}
		if(dir == 1)
		{
			tmpx = oX+rx;
			tmpz = oZ+rz;
		}
		if(dir == 2)
		{
			tmpx = oX-rz;
			tmpz = oZ+rx;
		}
		if(dir == 3)
		{
			tmpx = oX-rx;
			tmpz = oZ-rz;
		}
		return getEncodeVal(tmpx,tmpz,y);
	}
*/
}
