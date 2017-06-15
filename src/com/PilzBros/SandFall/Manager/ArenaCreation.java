package com.PilzBros.SandFall.Manager;



import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.PilzBros.SandFall.SandFall;
import com.PilzBros.SandFall.Item.Arena;
import com.PilzBros.SandFall.Plugin.Setting;
import com.PilzBros.SandFall.Plugin.Settings;

public class ArenaCreation 
{
	private int state;
	public static HashMap<String,CreationPlayer> players = new HashMap<String,CreationPlayer>();
	
	public ArenaCreation()
	{
		state = 0;
	}
	
	public static void selectstart(Player player, String name)
	{
		if (players.containsKey(player.getName()))
		{
			players.remove(player.getName());
		}
		
		player.sendMessage(ChatColor.AQUA + "----------SandFall Arena Creation----------");
		player.sendMessage(ChatColor.WHITE + "To make block selections, ensure your cross hair is on the desired block and execute the command" + ChatColor.GREEN + ChatColor.BOLD + " /sfa here");
		player.sendMessage("");
		player.sendMessage(ChatColor.WHITE + "First, select the highest point of the walls of the arena ");
		player.sendMessage(ChatColor.AQUA + "--------------------------------------");
		players.put(player.getName(), new CreationPlayer());
		players.get(player.getName()).name = name.toLowerCase();
	}
	
	public static void select(Player player, Location loc)
	{
		switch (players.get(player.getName()).state)
		{
			case 1:
				firstpoint(player,loc);
				break;
			case 2:
				secondpoint(player,loc);
				break;
			case 3:
				telepoint(player);
				break;
			case 4:
				sandpoint1(player,loc);
				break;
			case 5:
				sandpoint2(player,loc);
				break;
			case 6:
				woodpoint1(player,loc);
				break;
			case 7:
				woodpoint2(player,loc);
				break;
			case 8:
				freepoint(player);
				break;
			
			
		}
	}
	
	private static void firstpoint(Player player, Location loc)
	{
		player.sendMessage(ChatColor.YELLOW + "---------- SandFall Arena Creation ----------");
		player.sendMessage(ChatColor.GREEN + "Highest wall point selected. Now select the lowest point of the walls in the arena.");
		player.sendMessage(ChatColor.YELLOW + "---------------------------------------");
		CreationPlayer cr = players.get(player.getName());
		cr.X1 = loc.getX();
		cr.Y1 = loc.getY();
		cr.Z1 = loc.getZ();
		cr.state++;
		
	}

	private static void secondpoint(Player player, Location loc)
	{
		player.sendMessage(ChatColor.YELLOW + "---------- SandFall Arena Creation ----------");
		player.sendMessage(ChatColor.GREEN + "Lowest wall point selected. Now go inside the arena, and execute the selection command to select your current location as the arena's starting point.");
		player.sendMessage(ChatColor.YELLOW + "----------------------------------------");
		CreationPlayer cr = players.get(player.getName());
		cr.X2 = loc.getX();
		cr.Y2 = loc.getY();
		cr.Z2 = loc.getZ();
		cr.state++;
		
	}
	
	
	private static void telepoint(Player player)
	{
		player.sendMessage(ChatColor.GOLD + "---------- SandFall Arena Creation ----------");
		player.sendMessage(ChatColor.GREEN + "Starting point selected. Now select the highest point of the sand on top of the arena.");
		player.sendMessage(ChatColor.GOLD + "----------------------------------------");
		CreationPlayer cr = players.get(player.getName());
		cr.teleX = player.getLocation().getX();
		cr.teleY = player.getLocation().getY();
		cr.teleZ = player.getLocation().getZ();
		cr.teleWorld = player.getWorld().getName();
		cr.state++;
		
	}
	
	private static void sandpoint1(Player player, Location loc)
	{
		player.sendMessage(ChatColor.GOLD + "---------- SandFall Arena Creation ----------");
		player.sendMessage(ChatColor.GREEN + "Highest sand point selected. Now the lowest point of the sand on top of the arena.");
		player.sendMessage(ChatColor.GOLD + "----------------------------------------");
		CreationPlayer cr = players.get(player.getName());
		cr.sand1X = loc.getX();
		cr.sand1Y = loc.getY();
		cr.sand1Z = loc.getZ();
		cr.state++;
		
	}
	private static void sandpoint2(Player player, Location loc)
	{
	player.sendMessage(ChatColor.BLUE + "---------- SandFall Arena Creation ----------");
	player.sendMessage(ChatColor.GREEN + "Lowest sand point selected. Select first wood point.");
	player.sendMessage(ChatColor.BLUE + "----------------------------------------");
	CreationPlayer cr = players.get(player.getName());
	cr.sand2X = loc.getX();
	cr.sand2Y = loc.getY();
	cr.sand2Z = loc.getZ();
	cr.state++;

}
	private static void woodpoint1(Player player, Location loc)
	{
		player.sendMessage(ChatColor.BLUE + "---------- SandFall Arena Creation ----------");
		player.sendMessage(ChatColor.GREEN + "First wood point selected. Select second wood point.");
		player.sendMessage(ChatColor.BLUE + "----------------------------------------");
		CreationPlayer cr = players.get(player.getName());
		cr.wood1X = loc.getX();
		cr.wood1Y = loc.getY();
		cr.wood1Z = loc.getZ();
		cr.state++;
		
	}
	private static void woodpoint2(Player player, Location loc)
	{
		player.sendMessage(ChatColor.RED + "---------- SandFall Arena Creation ----------");
		player.sendMessage(ChatColor.GREEN + "Second wood point selected. Go outside of the arena and execute the selection command to mark your current location as where players will be teleported after each game.");
		player.sendMessage(ChatColor.RED + "----------------------------------------");
		CreationPlayer cr = players.get(player.getName());
		cr.wood2X = loc.getX();
		cr.wood2Y = loc.getY();
		cr.wood2Z = loc.getZ();
		cr.state++;
		
	}
	private static void freepoint(Player player)
	{
		
		CreationPlayer cr = players.get(player.getName());
		cr.freeX = player.getLocation().getX();
		cr.freeY = player.getLocation().getY();
		cr.freeZ = player.getLocation().getZ();
		cr.freeWorld = player.getWorld().getName();
		cr.state++;
		
		SandFall.gameController.addArena(new Arena(cr.name, cr.X1, cr.Y1, cr.Z1, cr.X2, cr.Y2, cr.Z2, cr.teleX, cr.teleY, cr.teleZ, cr.freeX, cr.freeY, cr.freeZ, cr.teleWorld, cr.freeWorld, cr.sand1X, cr.sand1Y, cr.sand1Z, cr.sand2X, cr.sand2Y, cr.sand2Z, cr.wood1X, cr.wood1Y, cr.wood1Z, cr.wood2X, cr.wood2Y, cr.wood2Z));
		SandFall.IO.storeArena(cr.name, cr.X1, cr.Y1, cr.Z1, cr.X2, cr.Y2, cr.Z2, cr.teleX, cr.teleY, cr.teleZ, cr.freeX, cr.freeY, cr.freeZ, cr.teleWorld, cr.freeWorld, cr.sand1X, cr.sand1Y, cr.sand1Z, cr.sand2X, cr.sand2Y, cr.sand2Z, cr.wood1X, cr.wood1Y, cr.wood1Z, cr.wood2X, cr.wood2Y, cr.wood2Z);		
		player.sendMessage(SandFall.pluginAdminPrefix + ChatColor.GREEN + "SandFall arena ["+ ChatColor.GREEN + cr.name + ChatColor.WHITE + "] setup successfully!!");
		
		players.remove(player.getName());
	}
	
	private static class CreationPlayer
	{
		public int state;
		
		private String name;
		private double X1;
		private double Y1;
		private double Z1;
		private double X2;
		private double Y2;
		private double Z2;
		private double teleX;
		private double teleY;
		private double teleZ;
		private double freeX;
		private double freeY;
		private double freeZ;
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
		private String teleWorld;
		private String freeWorld;
		
		public CreationPlayer()
		{
			state = 1;
		}
}

}
