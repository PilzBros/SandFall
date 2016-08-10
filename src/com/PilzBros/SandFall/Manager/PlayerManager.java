package com.PilzBros.SandFall.Manager;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.PilzBros.SandFall.SandFall;
import com.PilzBros.SandFall.Plugin.Setting;
import com.PilzBros.SandFall.Plugin.Settings;

public class PlayerManager 
{
	private GameManager gm;
	public static HashMap<String, Inventory> playerInventory;
	
	public PlayerManager(GameManager pgm)
	{
		gm = pgm;
		playerInventory = new HashMap<String, Inventory>();
	}
	
	/**
     * Check if a user is currently playing SandFall
     *
     * @param player Player playing
     * @return boolean Wether player is playing or not
     * 
     */
	public boolean isPlaying(Player player)
	{
		if (gm.playing.contains(player.getName()))
			return true;
		else
			return false;
	}
	
	/**
     * Remove player from playing list
     *
     * @param player Player to remove
     * 
     */
	public void leavePlaying(Player player)
	{
		while (gm.playing.contains(player.getName()))
		{
			gm.playing.remove(player.getName());
			SandFall.gameController.removePlayer(player.getName());
		}
		
		if (!gm.inProgress())
		{
			gm.gameOver();
		}
	}
	

	
	
	
	public void teleportBack(Player player)
	{
		player.teleport(gm.arena.getGameOverTeleportLocation());
	}
	
	public void playerRespawn(Player player)
	{
		restorePlayer(player);
	}
	
	
	/**
     * Prepares player for game play
     *
     * @param player Player entity to prepare
     * 
     */
	protected void preparePlayer(Player player)
	{
		
		//Teleport
		player.teleport(gm.arena.getStartLocation());
		
		//Health / Food
		gm.playing.add(player.getName());
		player.setHealth(1);
		player.setFoodLevel(Settings.getGlobalInt(Setting.FoodLevel));
		
		//Game Mode
		player.setGameMode(GameMode.SURVIVAL);
		
		//Flint and Steel
		storeInventory(player);
		
		//Add to alive
		gm.scoreboardPlaying++;
		
		//Update signs
		gm.arena.signController.updateSigns();
		
		//Waiting Message
		player.sendMessage(SandFall.pluginPrefix + ChatColor.GOLD + Settings.getGlobalString(Setting.WaitingMessage));
	}
	
	/**
     * Restores player to state before game
     *
     * @param player Player entity to restore
     * 
     */
	protected void restorePlayer(Player player)
	{
		teleportBack(player);
		gm.scoreboard.removeBoard(player);
		player.setFoodLevel(20);
		restoreInventory(player);
	}
	
	/**
     * Sends supplied user a message notifying them game has begun
     *
     * @param player Player entity to notify
     * 
     */
	protected void beginGameMessage(Player player)
	{
		player.sendMessage(SandFall.pluginPrefix + ChatColor.RED + Settings.getGlobalString(Setting.GameBeginMessage));
	}
	
	/**
     * Sends supplied user a message notifying them of server reload
     *
     * @param player Player entity to notify
     * 
     */
	protected void reloadNotify(Player player)
	{
		//notify players of boot on reload
		player.sendMessage(SandFall.pluginPrefix + ChatColor.GOLD + Settings.getGlobalString(Setting.ReloadMessage));
	}
	
	/**
     * Sends supplied user a message notifying them they won SandFall
     *
     * @param player Player entity to notify
     * 
     */
	protected void winNotify(Player player)
	{
		player.sendMessage(SandFall.pluginPrefix + ChatColor.GOLD + Settings.getGlobalString(Setting.WinMessage));
	}

	/**
     * Sends supplied user a message notifying them their game was force ended
     *
     * @param player Player entity to notify
     * 
     */
	protected void forceEndNotify(Player player)
	{
		player.sendMessage(SandFall.pluginPrefix + ChatColor.RED + "An admin has forced your game of SandFall to end");
	}
	
	/**
     * Sends supplied user a message notifying them how long until game begins
     *
     * @param player Player entity to notify
     * 
     */
	protected void countdownNotify(Player player)
	{
		try
		{
			player.sendMessage(SandFall.pluginPrefix + ChatColor.GREEN + "SandFall begins in " + gm.countdownSeconds);
		}
		catch (Exception e)
		{
			//
		}
		
	}
	
	/**
     * Stores user current inventory
     *
     * @param player Player entity whos inventory to store
     * 
     */
	private void storeInventory(Player player)
	{
		PlayerInventory inv = player.getInventory();
		Inventory temp = Bukkit.getServer().createInventory(null, InventoryType.PLAYER);
		for (ItemStack stack : inv.getContents()) 
		{
			if (stack != null)
		    {
				temp.addItem(stack);
		    }
		}
		
		
		playerInventory.put(player.getName(), temp);
		player.getInventory().clear();
		
		ItemStack flint = new ItemStack(Material.FLINT_AND_STEEL, 1);
		flint.addEnchantment(Enchantment.DURABILITY, 3);
		
		ItemStack itemstack = new ItemStack(Material.FIRE, Settings.getGlobalInt(Setting.FireQuantity));
		player.getInventory().addItem(itemstack);
		inv.addItem(itemstack);
		inv.addItem(flint);
		player.setItemInHand(flint);
		player.updateInventory();

	}
	
	/**
     * Restores user inventory
     *
     * @param player Player whos inventory to restore
     * 
     */
	private void restoreInventory(Player player)
	{
		if (playerInventory.containsKey(player.getName()))
		{
			player.getInventory().clear();
			Inventory inv = playerInventory.get(player.getName());			
			for (ItemStack stack : inv.getContents()) 
			{
			    if (stack != null)
			    {
			    	player.getInventory().addItem(stack);
			    }
			}
			player.updateInventory();
		}
		else
		{
			player.sendMessage(SandFall.pluginPrefix + ChatColor.RED + "There was an error while attempting to restore your inventory");
		}
	}

}
