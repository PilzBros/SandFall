package com.PilzBros.SandFall.Manager;



import java.util.HashMap;

import org.bukkit.ChatColor;
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
		player.sendMessage(ChatColor.GREEN + "First, select the first point of the walls of the arena ");
		player.sendMessage(ChatColor.AQUA + "--------------------------------------");
		players.put(player.getName(), new CreationPlayer());
		players.get(player.getName()).name = name.toLowerCase();
	}
	
	public static void select(Player player, Block block)
	{
		switch (players.get(player.getName()).state)
		{
			case 1:
				firstpoint(player,block);
				break;
			case 2:
				secondpoint(player,block);
				break;
			case 3:
				telepoint(player);
				break;
			case 4:
				sandpoint1(player,block);
				break;
			case 5:
				sandpoint2(player,block);
				break;
			case 6:
				woodpoint1(player,block);
				break;
			case 7:
				woodpoint2(player,block);
				break;
			case 8:
				freepoint(player);
				break;
			
			
		}
	}
	
	private static void firstpoint(Player player, Block block)
	{
		player.sendMessage(ChatColor.YELLOW + "---------- SandFall Arena Creation ----------");
		player.sendMessage(ChatColor.GREEN + "First point selected. Now select the second point.");
		player.sendMessage(ChatColor.YELLOW + "---------------------------------------");
		CreationPlayer cr = players.get(player.getName());
		cr.X1 = block.getX();
		cr.Y1 = block.getY();
		cr.Z1 = block.getZ();
		cr.state++;
		
	}

	private static void secondpoint(Player player, Block block)
	{
		player.sendMessage(ChatColor.YELLOW + "---------- SandFall Arena Creation ----------");
		player.sendMessage(ChatColor.GREEN + "Second point selected. Now go inside the arena, and click anywhere to set your current location as the starting point!");
		player.sendMessage(ChatColor.YELLOW + "----------------------------------------");
		CreationPlayer cr = players.get(player.getName());
		cr.X2 = block.getX();
		cr.Y2 = block.getY();
		cr.Z2 = block.getZ();
		cr.state++;
		
	}
	
	
	private static void telepoint(Player player)
	{
		player.sendMessage(ChatColor.GOLD + "---------- SandFall Arena Creation ----------");
		player.sendMessage(ChatColor.GREEN + "Starting point selected. Now select the first point of the sand on top of the arena");
		player.sendMessage(ChatColor.GOLD + "----------------------------------------");
		CreationPlayer cr = players.get(player.getName());
		cr.teleX = player.getLocation().getX();
		cr.teleY = player.getLocation().getY();
		cr.teleZ = player.getLocation().getZ();
		cr.teleWorld = player.getWorld().getName();
		cr.state++;
		
	}
	
	private static void sandpoint1(Player player, Block block)
	{
		player.sendMessage(ChatColor.GOLD + "---------- SandFall Arena Creation ----------");
		player.sendMessage(ChatColor.GREEN + "First sand point selected. Now the second point of the sand");
		player.sendMessage(ChatColor.GOLD + "----------------------------------------");
		CreationPlayer cr = players.get(player.getName());
		cr.sand1X = block.getX();
		cr.sand1Y = block.getY();
		cr.sand1Z = block.getZ();
		cr.state++;
		
	}
	private static void sandpoint2(Player player, Block block)
	{
		player.sendMessage(ChatColor.BLUE + "---------- SandFall Arena Creation ----------");
		player.sendMessage(ChatColor.GREEN + "Second sand point selected. Select first wood point");
		player.sendMessage(ChatColor.BLUE + "----------------------------------------");
		CreationPlayer cr = players.get(player.getName());
		cr.sand2X = block.getX();
		cr.sand2Y = block.getY();
		cr.sand2Z = block.getZ();
		cr.state++;
		
	}
	private static void woodpoint1(Player player, Block block)
	{
		player.sendMessage(ChatColor.BLUE + "---------- SandFall Arena Creation ----------");
		player.sendMessage(ChatColor.GREEN + "First wood point selected. Select second wood point");
		player.sendMessage(ChatColor.BLUE + "----------------------------------------");
		CreationPlayer cr = players.get(player.getName());
		cr.wood1X = block.getX();
		cr.wood1Y = block.getY();
		cr.wood1Z = block.getZ();
		cr.state++;
		
	}
	private static void woodpoint2(Player player, Block block)
	{
		player.sendMessage(ChatColor.RED + "---------- SandFall Arena Creation ----------");
		player.sendMessage(ChatColor.GREEN + "Second wood point selected. Select anywhere to set your current location as where players will be sent after the game");
		player.sendMessage(ChatColor.RED + "----------------------------------------");
		CreationPlayer cr = players.get(player.getName());
		cr.wood2X = block.getX();
		cr.wood2Y = block.getY();
		cr.wood2Z = block.getZ();
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
