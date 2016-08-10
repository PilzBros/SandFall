package com.PilzBros.SandFall.Command;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.PilzBros.SandFall.SandFall;
import com.PilzBros.SandFall.Item.Arena;
import com.PilzBros.SandFall.Manager.ArenaCreation;
import com.PilzBros.SandFall.Plugin.Setting;
import com.PilzBros.SandFall.Plugin.Settings;

public class SFAdminCommand implements CommandExecutor
{
	private SandFall sf;
	
	public SFAdminCommand(SandFall sf)
	{
		this.sf = sf;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender.hasPermission("sandfall.admin") || sender.hasPermission("sandfall.*"))
		{
			//Have Permissions
			if (args.length < 1)
			{
				sender.sendMessage(SandFall.pluginAdminPrefix + "SandFall v" + SandFall.pluginVersion);
			}
			else if (args.length == 1)
			{
				if (args[0].equalsIgnoreCase("reset"))
				{
					SandFall.gameController.arenasLoaded();
					sender.sendMessage(SandFall.pluginAdminPrefix + ChatColor.GREEN + "Arenas Reset!");
				}
				else if (args[0].equalsIgnoreCase("update"))
				{
					//Display update information
					if (SandFall.updateChecker.isUpdateNeeded())
					{
						sender.sendMessage(SandFall.pluginAdminPrefix + ChatColor.YELLOW + "Update available!" + ChatColor.WHITE + " You are currently running v" + ChatColor.RED + SandFall.pluginVersion + ChatColor.WHITE + " and the most recent version is v" + ChatColor.GREEN + SandFall.updateChecker.getLatestVersion() + ChatColor.WHITE + " Please visit " + ChatColor.YELLOW + SandFall.pluginWebsite + ChatColor.WHITE + " to update");
					}
					else
					{
						sender.sendMessage(SandFall.pluginAdminPrefix + ChatColor.GREEN + "SandFall v" + SandFall.pluginVersion + " is up to date");
					}
				}
				else if (args[0].equalsIgnoreCase("reload"))
				{
					//Reloads config files
					SandFall.IO.LoadSettings();
					SandFall.IO.prepareDB();
					SandFall.gameController.serverReload();
					SandFall.gameController.reloadController();
					SandFall.IO.loadArena();
					SandFall.IO.loadSigns();
					sender.sendMessage(SandFall.pluginAdminPrefix + ChatColor.GREEN + "SandFall reloaded!");
					
				}
				else if (args[0].equalsIgnoreCase("arenas"))
				{
					if (SandFall.gameController.arenasExist())
					{
						sender.sendMessage(SandFall.pluginAdminPrefix + "-- SandFall Arenas --");
						
						int count = 1;
						for(Entry<String, Arena> a : SandFall.gameController.arenas.entrySet()) 
						{
							sender.sendMessage(count + ". " + a.getValue().getName());
							count++;
						}
					}
					else
					{
						sender.sendMessage(SandFall.pluginAdminPrefix + ChatColor.GOLD + "There are no SandFall arenas!");
					}
				}
				else if (args[0].equalsIgnoreCase("setup"))
				{
					sender.sendMessage(SandFall.pluginAdminPrefix + ChatColor.RED + "Arena name not supplied. To setup an arena: " + ChatColor.GOLD + "/sandfalladmin setup [arena]");
				}
				else if (args[0].equalsIgnoreCase("delete"))
				{
					sender.sendMessage(SandFall.pluginAdminPrefix + ChatColor.RED + "Arena name not supplied. To delete an arena: " + ChatColor.GOLD + "/sandfalladmin delete [arena]");
				}
				else if (args[0].equalsIgnoreCase("enable"))
				{
					sender.sendMessage(SandFall.pluginAdminPrefix + ChatColor.RED + "Arena name not supplied. To enable/disable an arena: " + ChatColor.GOLD + "/sandfalladmin setup [arena]");
				}
				else if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?"))
				{
					sender.sendMessage(SandFall.pluginAdminPrefix + "SandFall v" + SandFall.pluginVersion);
					sender.sendMessage(ChatColor.AQUA + "-----------------");
					sender.sendMessage(ChatColor.GOLD + "/sandfalladmin setup [arena]" + ChatColor.AQUA + "     Arena Creation");
					sender.sendMessage(ChatColor.GOLD + "/sandfalladmin delete [arena]" + ChatColor.AQUA + "    Arena Delete");
					sender.sendMessage(ChatColor.GOLD + "/sandfalladmin arenas" + ChatColor.AQUA + "     List Arenas");
					sender.sendMessage(ChatColor.GOLD + "/sandfalladmin arena [arena]" + ChatColor.AQUA + "     Arena Info");
					sender.sendMessage(ChatColor.GOLD + "/sandfalladmin end [arena]" + ChatColor.AQUA + "     End Game");
					sender.sendMessage(ChatColor.GOLD + "/sandfalladmin kick [player]" + ChatColor.AQUA + "      Kick player");
					sender.sendMessage(ChatColor.GOLD + "/sandfalladmin enable [arena]" + ChatColor.AQUA + "     Enable/Disable Arena");
					sender.sendMessage(ChatColor.GOLD + "/sandfalladmin reset" + ChatColor.AQUA + "     Reset Arenas");
					sender.sendMessage(ChatColor.GOLD + "/sandfalladmin reload" + ChatColor.AQUA + "   Reload SandFall");
					sender.sendMessage(ChatColor.GOLD + "/sandfalladmin update" + ChatColor.AQUA + "     Display Update Info");
				}
				else
				{
					sender.sendMessage(SandFall.pluginAdminPrefix + ChatColor.RED + "Unknown SandFall Admin command. Type " + ChatColor.YELLOW + ChatColor.BOLD + "/sandfalladmin help");
				}
			}
			else if (args.length == 2)
			{
				 if (args[0].equalsIgnoreCase("kick"))
				 {
					 if (SandFall.gameController.playerPlaying(args[1]))
					 {
						 SandFall.gameController.kickPlayer(args[1]);
						 sender.sendMessage(SandFall.pluginAdminPrefix + ChatColor.GREEN + "Player has been kicked from SandFall");
					 }
					 else
					 {
						 sender.sendMessage(SandFall.pluginAdminPrefix + ChatColor.RED + "Player is not currently playing SandFall");
					 }
				 }
				 else if (args[0].equalsIgnoreCase("setup"))
				 {
					if (!SandFall.gameController.arenaExist(args[1]))
						ArenaCreation.selectstart((Player) sender, args[1]);
					else
						sender.sendMessage(SandFall.pluginAdminPrefix + ChatColor.RED + "Arena " + args[1] + " already exists! Please choose another name or delete " + args[1]);
				 }
				 else if (args[0].equalsIgnoreCase("arena"))
				 {
					 if (SandFall.gameController.arenaExist(args[1]))
					 {
						 Arena arena = SandFall.gameController.getArena(args[1]);
						 
						 sender.sendMessage(SandFall.pluginAdminPrefix + ChatColor.GREEN + "---- " + arena.getName() + " ----");
						 if (arena.isEnabled())
							 sender.sendMessage("Status: " + ChatColor.GREEN + "Enabled");
						 else
							 sender.sendMessage("Status: " + ChatColor.RED + "Disabled");
						 
						 sender.sendMessage("Signs: " + ChatColor.BLUE + arena.numSigns());
						 if (arena.gameManager.inWaiting)
						 {
							 sender.sendMessage("Game: " + ChatColor.AQUA + "Waiting... " + arena.gameManager.countdownSeconds + " seconds left");
						 }
						 else if (arena.gameManager.inProgress)
						 {
							 sender.sendMessage("Game: " + ChatColor.RED + "In Progress");
							 sender.sendMessage("Alive: " + ChatColor.GREEN + arena.gameManager.scoreboardPlaying);
							 sender.sendMessage("Dead: " + ChatColor.RED + arena.gameManager.scoreboardDead);
						 }
						 else
						 {
							 sender.sendMessage("Game: " + ChatColor.GREEN + "Idle");
						 }
					 }
					 else
					 {
						 sender.sendMessage(SandFall.pluginAdminPrefix + ChatColor.RED + "Arena " + args[1] + " does not exist!");
					 }
				 }
				 else if (args[0].equalsIgnoreCase("enable"))
				 {
					 if (SandFall.gameController.arenaExist(args[1]))
					 {
						 Arena arena = SandFall.gameController.getArena(args[1]);
						 if (arena.isEnabled())
						 {
							 arena.setEnabled(false);
							 sender.sendMessage(SandFall.pluginAdminPrefix + ChatColor.RED + "Arena " + args[1] + " disabled!");
						 }
						 else
						 {
							 arena.setEnabled(true);
							 sender.sendMessage(SandFall.pluginAdminPrefix + ChatColor.GREEN + "Arena " + args[1] + " enabled!");
						 }
					 }
				 }
				 else if (args[0].equalsIgnoreCase("delete"))
				 {
					 if (SandFall.gameController.arenaExist(args[1]))
					 { 
						 Arena arena = SandFall.gameController.getArena(args[1]);
						 
						 if (arena.gameManager.inProgress || arena.gameManager.inWaiting)
						 {
							 arena.gameManager.forceEnd();
							 arena.gameManager.bootup();
						 }
						 
						 SandFall.gameController.removeArena(arena);
						 sender.sendMessage(SandFall.pluginAdminPrefix + ChatColor.GREEN + "Arena " + args[1] + " has been removed!");
						 	
					 }
					 else
					 {
						 sender.sendMessage(SandFall.pluginAdminPrefix + ChatColor.RED + "Arena " + args[1] + " does not exist!");
					 }
				 }
				 else if (args[0].equalsIgnoreCase("end"))
				 {
					 if (SandFall.gameController.getArena(args[1]).gameManager.inProgress || SandFall.gameController.getArena(args[1]).gameManager.inWaiting)
					 {
						SandFall.gameController.getArena(args[1]).gameManager.forceEnd();
						SandFall.gameController.getArena(args[1]).gameManager.bootup();
						sender.sendMessage(SandFall.pluginAdminPrefix + ChatColor.GREEN + "Current game in " + args[1] + " ended!");
					}
					else
					{
						sender.sendMessage(SandFall.pluginAdminPrefix + ChatColor.GOLD + "There is not a current game going on in arena " + args[1]);
					}
					}
				 else
				 {
					 sender.sendMessage(SandFall.pluginAdminPrefix + ChatColor.RED + "Unknown SandFall command. Type " + ChatColor.YELLOW + ChatColor.BOLD + "/sandfalladmin help");
				 }
			}
			else
			{
				sender.sendMessage(SandFall.pluginAdminPrefix + ChatColor.RED + "Unknown SandFall command. Type " + ChatColor.YELLOW + ChatColor.BOLD + "/sandfalladmin help");
			}
		}
		else
		{
			//No permissions
			sender.sendMessage(SandFall.pluginAdminPrefix + ChatColor.RED + "Insufficent permissions!");
		}
		return true;
	}
}
