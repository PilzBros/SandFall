package com.PilzBros.SandFall.Listener;


import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.PilzBros.SandFall.SandFall;
import com.PilzBros.SandFall.Item.Arena;
import com.PilzBros.SandFall.Manager.ArenaCreation;
import com.PilzBros.SandFall.Plugin.Setting;
import com.PilzBros.SandFall.Plugin.Settings;

@SuppressWarnings("deprecation")
public class PlayerListener implements Listener
{
	private SandFall sf;
	
	public PlayerListener (SandFall sf)
	{
		this.sf = sf;
	}
	
	@EventHandler()
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getPlayer().getItemInHand().getTypeId() == Settings.getGlobalInt(Setting.SelectionTool))
		{
			if ( ArenaCreation.players.containsKey(event.getPlayer().getName()))
			{
				ArenaCreation.select(event.getPlayer(), event.getClickedBlock());
				event.setCancelled(true);
			}
			else if (ArenaCreation.players.containsKey(event.getPlayer().getName()))
			{
				ArenaCreation.select(event.getPlayer(), event.getClickedBlock());
				event.setCancelled(true);
			}
		}
		else if (SandFall.gameController.playerPlaying(event.getPlayer()))
		{
			if (SandFall.gameController.players.get(event.getPlayer().getName()).gameManager.inWaiting)
			{
				event.setCancelled(true);
			}
			else if (SandFall.gameController.players.get(event.getPlayer().getName()).gameManager.inProgress)
			{
				/*
				if (!event.getClickedBlock().getType().equals(Material.WOOD))
					event.setCancelled(true);
				*/
			}
		}
	}
	
	
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent evt) 
	{
		Player player = evt.getPlayer();
		if (player.hasPermission("SandFall.*") || player.hasPermission("SandFall.admin"))
		{
			if (SandFall.updateChecker.isUpdateNeeded()) {
				evt.getPlayer().sendMessage(SandFall.pluginAdminPrefix + ChatColor.YELLOW + "Update available!" + ChatColor.WHITE + " You are currently running v" + ChatColor.RED + SandFall.pluginVersion + ChatColor.WHITE + " and the most recent version is v" + ChatColor.GREEN + SandFall.updateChecker.getLatestVersion() + ChatColor.WHITE + " Please visit " + ChatColor.YELLOW + SandFall.pluginWebsite + ChatColor.WHITE + " to update");
			}
		}
		
		if (Settings.getGlobalBoolean(Setting.NotifyOnAustinPilz))
		{
			//If setting is enabled, notifies the user if I join the server
			if (player.getName().equalsIgnoreCase("austinpilz"))
			{
				for(Player p : Bukkit.getServer().getOnlinePlayers())
				{
					if (p.hasPermission("SandFall.*") || p.hasPermission("SandFall.admin"))
					{
						p.sendMessage(SandFall.pluginAdminPrefix + ChatColor.WHITE + "SandFall creator " + ChatColor.GREEN + "austinpilz" + ChatColor.WHITE + " has joined the server!");
					}
						
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent evt)
	{
		if (SandFall.gameController.playerPlaying(evt.getPlayer()))
		{
			SandFall.gameController.playerLogoff(evt.getPlayer());
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDieEvent(EntityDamageEvent event) 
	{
		if(event.getEntity() instanceof Player){}
		else
			return;
		Player player = (Player)event.getEntity();
		if (SandFall.gameController.playerPlaying(player.getName()))
		{
			if(player.getHealth() <= event.getDamage())
			{
				event.setCancelled(true);
				player.setHealth(player.getMaxHealth());
				player.setFoodLevel(20);
				player.setFireTicks(0);
				SandFall.gameController.playerDeath(player);
			}
		}
	}
	
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		//removePlayer(event.getPlayer().getName());
		if (SandFall.gameController.playerToRespawn(event.getPlayer()))
		{
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SandFall.instance, new Runnable()
			{	
				public void run() 
				{
					for(Entry<String, Arena> player : SandFall.gameController.respawn.entrySet()) 
					{
						player.getValue().gameManager.pm.playerRespawn(Bukkit.getPlayer(player.getKey()));
						SandFall.gameController.respawn.remove(player.getKey());
					}
					//SandFall.gameController.arenas.containsKey("hi");
					//SandFall.gameManager.pm.playerRespawn(tmp.gameManager.tempRespawn);
				}
				
			}, 1); 
		}
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDamage(EntityDamageEvent e) 
	{
		if(e instanceof EntityDamageByEntityEvent)
		{
			EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
            
			if (event.getEntity() instanceof Player)
			{
				Player damaged = (Player)event.getEntity();
				if (SandFall.gameController.playerPlaying(damaged))
				{
					if(event.getDamager() instanceof Player)
		            {
		            	Player damager = (Player) event.getDamager();
		            	
		            	if (!damager.getItemInHand().getType().equals(Material.BLAZE_ROD))
		            	{
		            		damager.sendMessage(SandFall.pluginPrefix + ChatColor.RED + "You cannot hit other players!");
		            		event.setCancelled(true);
		            	}
		            }
				}
			}
		}
	}
	
	
	
	@EventHandler()
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
	{
		if (SandFall.gameController.playerPlaying(event.getPlayer()))
		{
			if (!event.getPlayer().hasPermission("SandFall.*") || !event.getPlayer().hasPermission("SandFall.admin"))
			{
				if (!Settings.getGlobalBoolean(Setting.InGameCommands))
				{
					if (!event.getMessage().toLowerCase().startsWith("/sandfall"))
					{
						event.setCancelled(true);
						event.getPlayer().sendMessage(SandFall.pluginPrefix + ChatColor.RED + Settings.getGlobalString(Setting.CommandDisabledMessage));
					}
				}
			}
		}
	}
	
	@EventHandler()
	public void onPlayerTeleport(PlayerTeleportEvent event)
	{
		if (SandFall.gameController.playerPlaying(event.getPlayer()))
		{
			if (!Settings.getGlobalBoolean(Setting.InGameTeleport))
			{
				if (!event.getPlayer().hasPermission("SandFall.*") || !event.getPlayer().hasPermission("SandFall.admin"))
				{
					event.setCancelled(true);
					event.getPlayer().sendMessage(SandFall.pluginPrefix + ChatColor.RED + "You cannot teleport while playing SandFall. Type /sandfall quit to leave the game");
				}
			}
		}
	}
	
	@EventHandler()
	public void onBlockBreak(BlockBreakEvent event)
	{
		if (SandFall.gameController.playerPlaying(event.getPlayer()))
		{
			if (SandFall.gameController.players.get(event.getPlayer().getName()).isInside(event.getPlayer().getLocation()))
			{
				event.setCancelled(true);
			}
		}
	}
	
}
