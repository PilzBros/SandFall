package com.PilzBros.SandFall.Listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.PilzBros.SandFall.SandFall;
import com.PilzBros.SandFall.Item.Arena;

public class SignListener implements Listener
{
	
	@EventHandler
	public void blockPlaced(SignChangeEvent event)
	{
		if (event.getBlock().getType() == Material.WALL_SIGN || event.getBlock().getType() == Material.SIGN_POST || event.getBlock().getType() == Material.SIGN)
		{
			String[] signline = event.getLines();
			
			if (signline[0].equalsIgnoreCase("[SandFall]") )
			{
				if (event.getPlayer().hasPermission("SandFall.admin") || event.getPlayer().hasPermission("SandFall.*"))
				{	
					if (SandFall.gameController.arenaExist(signline[1]))
					{
						SandFall.signManager.addSign(event.getBlock(), SandFall.gameController.getArena(signline[1]));
						event.setCancelled(true);
						event.getPlayer().sendMessage(SandFall.pluginAdminPrefix + ChatColor.GREEN + "Sandfall sign created!");
					}
					else
					{
						event.setCancelled(true);
						event.getPlayer().sendMessage(SandFall.pluginAdminPrefix + ChatColor.GREEN + "That arena doesn't exist");
					}
				}
				else
				{
					event.getPlayer().sendMessage(SandFall.pluginPrefix + ChatColor.RED + "You don't have permissions to make SandFall signs");
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void clickHandler(PlayerInteractEvent e)
	{
		
		if((e.getAction()==Action.RIGHT_CLICK_BLOCK) || (e.getAction()==Action.LEFT_CLICK_BLOCK)) 
		{
		
			Block clickedBlock = e.getClickedBlock(); 
			
			if((clickedBlock.getType() == Material.SIGN || clickedBlock.getType() == Material.SIGN_POST || clickedBlock.getType() == Material.WALL_SIGN)) 
			{
				Sign thisSign = (Sign) clickedBlock.getState();
				String[] lines = thisSign.getLines();
				
				
				if(lines[0].equalsIgnoreCase(SandFall.signPrefix)) 
				{
					if (!SandFall.gameController.playerPlaying(e.getPlayer()))
					{
						if (SandFall.gameController.arenaExist(lines[1]))
						{
							Arena arena = SandFall.gameController.getArena(lines[1]);
							if (arena.isEnabled())
							{	
								if(!arena.gameManager.inProgress && !arena.gameManager.inWaiting) 
								{
									SandFall.gameController.playerStart(e.getPlayer().getName(), arena.getName());
								}
								else if(arena.gameManager.inWaiting) 
								{
									SandFall.gameController.playerStart(e.getPlayer().getName(), arena.getName());
								}
								else if(arena.gameManager.inProgress) 
								{
									e.getPlayer().sendMessage(SandFall.pluginPrefix + "The game in this arena is already in progress. Please wait until the game ends");
								}
							}
							else
							{
								e.getPlayer().sendMessage(SandFall.pluginPrefix + ChatColor.RED + "This arena has been disabled!");
							}
						}
						else
						{
							e.getPlayer().sendMessage(SandFall.pluginPrefix + "That arena does not exist!");
						}
					}
					else
					{
						e.getPlayer().sendMessage(SandFall.pluginPrefix + ChatColor.RED + "You're already playing SandFall! To leave your current game, type /sandfall quit");
					}
				}
			}
		}
		
		
		
	}
	
	@EventHandler
	public void onBlockDestroy(BlockBreakEvent event)
	{
		if (event.getBlock().getType() == Material.WALL_SIGN || event.getBlock().getType() == Material.SIGN_POST)
		{
			Sign thisSign = (Sign) event.getBlock().getState();
			String[] lines = thisSign.getLines();
			
			if(lines[0].equalsIgnoreCase(SandFall.signPrefix)) 
			{
				if (event.getPlayer().hasPermission("SandFall.admin") || event.getPlayer().hasPermission("SandFall.*"))
				{
					SandFall.signManager.removeSign(event);
					event.getPlayer().sendMessage(SandFall.pluginAdminPrefix + ChatColor.GREEN + "SandFall sign removed!");
				}
				else
				{
					event.setCancelled(true);
					event.getPlayer().sendMessage(SandFall.pluginPrefix + ChatColor.RED + "You don't have permission to destroy SandFall signs!");
				}
			}
		}
	}

}
