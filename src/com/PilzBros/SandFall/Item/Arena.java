package com.PilzBros.SandFall.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import com.PilzBros.SandFall.SandFall;
import com.PilzBros.SandFall.Controller.SignController;
import com.PilzBros.SandFall.Manager.GameManager;




public class Arena 
{
	public String name;
	private double X1;
	private double Y1;
	private double Z1;
	private double X2;
	private double Y2;
	private double Z2;
	private double wood1X;
	private double wood1Y;
	private double wood1Z;
	private double wood2X;
	private double wood2Y;
	private double wood2Z;
	private double sand1X;
	private double sand1Y;
	private double sand1Z;
	private double sand2X;
	private double sand2Y;
	private double sand2Z;
	private double teleX;
	private double teleY;
	private double teleZ;
	private double freeX;
	private double freeY;
	private double freeZ;
	private String teleWorldname;
	private String freeWorldname;
	public GameManager gameManager;
	public SignController signController;
	private List<Sign> signs;
	private boolean enabled;
	
	public Arena(String pname, double pX1, double pY1, double pZ1, double pX2, double pY2, double pZ2, double pteleX, double pteleY, double pteleZ, double pfreeX, double pfreeY, double pfreeZ, String pteleWorld, String pfreeWorld, double ps1X, double ps1Y, double ps1Z, double ps2X, double ps2Y, double ps2Z, double pw1X, double pw1Y, double pw1Z, double pw2X, double pw2Y, double pw2Z)
	{
		name = pname.toLowerCase();
		X1 = pX1;
		Y1 = pY1;
		Z1 = pZ1;
		X2 = pX2;
		Y2 = pY2;
		Z2 = pZ2;
		teleX = pteleX;
		teleY = pteleY;
		teleZ = pteleZ;
		freeX = pfreeX;
		freeY = pfreeY;
		freeZ = pfreeZ;
		teleWorldname = pteleWorld;
		freeWorldname = pfreeWorld;
		sand1X = ps1X;
		sand1Y = ps1Y;
		sand1Z = ps1Z;
		sand2X = ps2X;
		sand2Y = ps2Y;
		sand2Z = ps2Z;
		wood1X = pw1X;
		wood1Y = pw1Y;
		wood1Z = pw1Z;
		wood2X = pw2X;
		wood2Y = pw2Y;
		wood2Z = pw2Z;
		gameManager = new GameManager(this);
		signController = new SignController(this);
		this.signs = new ArrayList<Sign>();
		enabled = true;
	}
	
	public void autoCheck()
	{
		//will call auto check of game manager
		gameManager.autoCheck();
	}
	
	public List<Sign> getSigns()
	{
		return signs;
	}
	
	public void addSign(Sign sign)
	{
		signs.add(sign);
	}
	
	public int numSigns()
	{
		return signs.size();
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public void setEnabled(Boolean value)
	{
		if (value)
		{
			enabled = true;
		}
		else
		{
			enabled = false;
		}
		signController.updateSigns();
	}
	
	public Boolean isInside(Location loc)
	{
		if (loc.getWorld().getName().equalsIgnoreCase(teleWorldname) && isBetween(X1,X2, loc.getX()) && isBetween(Y1,Y2, loc.getY()) && isBetween(Z1,Z2, loc.getZ()) )
			return true;
		return false;
	}
	
	public boolean isSand(Block block)
	{
		Location loc = block.getLocation();
		if (loc.getWorld().getName().equalsIgnoreCase(teleWorldname) && isBetween(sand1X,sand2X, loc.getX()) && isBetween(sand1Y,sand2Y, loc.getY()) && isBetween(sand1Z,sand2Z, loc.getZ()) )
			return true;
		return false;
	}
	
	private Boolean isBetween(double x, double y, double n)
	{
		x++;
		y++;
		n++;
		if ((x < y) && x <= n && y >= n )
			return true;
		else if ((x > y) && x >= n && y <= n )
			return true;
		return false;
		
			
	}
	
	public Location getStartLocation()
	{
		
		return (new Location(SandFall.instance.getServer().getWorld(teleWorldname), teleX, teleY, teleZ));
	}
	
	public Location getGameOverTeleportLocation()
	{
		return (new Location(SandFall.instance.getServer().getWorld(freeWorldname), freeX, freeY, freeZ));
	}
	
	public void setTeleportLocation(Location input)
	{
		teleX = input.getX();
		teleY = input.getY();
		teleZ = input.getZ();
		teleWorldname = input.getWorld().getName();
	}
	
	public void setReleaseTeleportLocation(Location input)
	{
		freeX = input.getX();
		freeY = input.getY();
		freeZ = input.getZ();
		freeWorldname = input.getWorld().getName();
	}
	
	public Location getFirstCorner()
	{
		return (new Location(SandFall.instance.getServer().getWorld(teleWorldname), X1, Y1, Z1));
	}
	
	public Location getSecondCorner()
	{
		return (new Location(SandFall.instance.getServer().getWorld(teleWorldname), X2, Y2, Z2));
	}
	
	public void setFirstCorner(Location input)
	{
		X1 = input.getX();
		Y1 = input.getY();
		Z1 = input.getZ();
	}
	
	public void setSecondCorner(Location input)
	{
		X2 = input.getX();
		Y2 = input.getY();
		Z2 = input.getZ();
	}
	
	public void reset()
	{
		clearArena();
		setWood();
		setSand();
	}
	
	private void setSand()
	{
		double temp;
		
		if (sand1X > sand2X)
		{
			temp = sand1X;
			sand1X = sand2X;
			sand2X = temp;
			
		}
		if (sand1Y > sand2Y)
		{
			temp = sand1Y;
			sand1Y = sand2Y;
			sand2Y = temp;
		}
		if (sand1Z > sand2Z)
		{
			temp = sand1Z;
			sand1Z = sand2Z;
			sand2Z = temp;
		}
			
		
		for (double x = sand1X; x <= sand2X; x++)
		{
			for (double y = sand1Y; y <= sand2Y; y++)
			{
				for (double z = sand1Z; z <= sand2Z; z++)
				{
					try
					{
						Location bl = new Location(Bukkit.getServer().getWorld(teleWorldname), x, y, z);
						bl.getBlock().setType(Material.SAND);
					}
					catch (Exception e)
					{
						//
					}
				}
			}
		}
		
		
	}
	
	/**
	 * Makes the wood disappear so all remaining sand will fall and kill the players
	 */
	public void makeItRain()
	{
double temp;
		
		if (wood1X > wood2X)
		{
			temp = wood1X;
			wood1X = wood2X;
			wood2X = temp;
			
		}
		if (wood1Y > wood2Y)
		{
			temp = wood1Y;
			wood1Y = wood2Y;
			wood2Y = temp;
		}
		if (wood1Z > wood2Z)
		{
			temp = wood1Z;
			wood1Z = wood2Z;
			wood2Z = temp;
		}
			
		for (double x = wood1X; x <= wood2X; x++)
		{
			for (double y = wood1Y; y <= wood2Y; y++)
			{
				for (double z = wood1Z; z <= wood2Z; z++)
				{
					try
					{
						Location bl = new Location(Bukkit.getServer().getWorld(teleWorldname), x, y, z);
						if (bl.getBlock().getType() != Material.AIR)
						{
							bl.getBlock().setType(Material.AIR);
							try 
							{
							    Thread.sleep(250);                 //1000 milliseconds is one second.
							} 
							catch(InterruptedException ex) 
							{
							    Thread.currentThread().interrupt();
							}
						}
					}
					catch (Exception e)
					{
						//
					}
				}
			}
		}
	}
	
	private void setWood()
	{
		double temp;
		
		if (wood1X > wood2X)
		{
			temp = wood1X;
			wood1X = wood2X;
			wood2X = temp;
			
		}
		if (wood1Y > wood2Y)
		{
			temp = wood1Y;
			wood1Y = wood2Y;
			wood2Y = temp;
		}
		if (wood1Z > wood2Z)
		{
			temp = wood1Z;
			wood1Z = wood2Z;
			wood2Z = temp;
		}
			
		for (double x = wood1X; x <= wood2X; x++)
		{
			for (double y = wood1Y; y <= wood2Y; y++)
			{
				for (double z = wood1Z; z <= wood2Z; z++)
				{
					try
					{
						Location bl = new Location(Bukkit.getServer().getWorld(teleWorldname), x, y, z);
						bl.getBlock().setType(Material.BEDROCK);
					}
					catch (Exception e)
					{
						//
					}
				}
			}
		}
		
		for (double x = wood1X; x <= wood2X; x++)
		{
			for (double y = wood1Y; y <= wood2Y; y++)
			{
				for (double z = wood1Z; z <= wood2Z; z++)
				{
					try
					{
						Location bl = new Location(Bukkit.getServer().getWorld(teleWorldname), x, y, z);
						bl.getBlock().setType(Material.WOOD);
					}
					catch (Exception e)
					{
						//
					}
				}
			}
		}
		
	}
	
	private void clearArena()
	{
		double temp;
		
		if (X1 > X2)
		{
			temp = X1;
			X1 = X2;
			X2 = temp;
			
		}
		if (Y1 > Y2)
		{
			temp = Y1;
			Y1 = Y2;
			Y2 = temp;
		}
		if (Z1 > Z2)
		{
			temp = Z1;
			Z1 = Z2;
			Z2 = temp;
		}
			
		
	
			
			for (double x = X1; x <= X2; x++)
			{
				for (double y = Y1; y <= Y2; y++)
				{
					for (double z = Z1; z <= Z2; z++)
					{
						
						try
						{
							Location bl = new Location(Bukkit.getServer().getWorld(teleWorldname), x, y, z);
							if (bl.getBlock().getType() == Material.SAND)
							{
								bl.getBlock().setType(Material.AIR);
							}
						}
						catch (Exception e)
						{
							//
						}
						
				
							
					}
				}
			}
		
	}
}
