package com.PilzBros.SandFall.Command;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.PilzBros.SandFall.SandFall;
import com.PilzBros.SandFall.Manager.ArenaCreation;

public class SFCommand implements CommandExecutor
{
	private SandFall sf;
	
	public SFCommand(SandFall sf)
	{
		this.sf = sf;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender.hasPermission("sandfall.user") || sender.hasPermission("sandfall.*") || sender.hasPermission("sandfall.admin"))
		{
			//Have Permissions
			if (args.length < 1)
			{
				sender.sendMessage(SandFall.pluginPrefix + "SandFall v" + SandFall.pluginVersion);
			}
			else if (args.length == 1)
			{
				if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?"))
				{
					sender.sendMessage(SandFall.pluginPrefix + "SandFall v" + SandFall.pluginVersion);
					sender.sendMessage(ChatColor.AQUA + "-----------------");
					sender.sendMessage(ChatColor.GOLD + "/sandfall play" + ChatColor.AQUA + "     Play Sandfall");
					sender.sendMessage(ChatColor.GOLD + "/sandfall quit" + ChatColor.AQUA + "     Quit Sandfall");
				}
				else if (args[0].equalsIgnoreCase("quit"))
				{
					if (SandFall.gameController.playerPlaying(sender.getName()))
					{
						SandFall.gameController.playerQuit(sender.getName());
					}
					else
					{
						sender.sendMessage(SandFall.pluginPrefix + ChatColor.RED + "You're not currently playing SandFall");
					}
				}
				else if (args[0].equalsIgnoreCase("play"))
				{
					sender.sendMessage(SandFall.pluginPrefix + ChatColor.RED + "Arena not supplied. To play SandFall: " + ChatColor.GOLD +  "/sandfall play [arena]");
				}
				else
				{
					sender.sendMessage(SandFall.pluginPrefix + ChatColor.RED + "Unknown SandFall command. Type " + ChatColor.YELLOW + ChatColor.BOLD + "/sandfall help");
				}
			}
			else if (args.length == 2)
			{
				if (args[0].equalsIgnoreCase("play"))
				{
					if (!SandFall.gameController.playerPlaying(Bukkit.getPlayer(sender.getName())))
					{
						if (SandFall.gameController.arenasExist())
						{
							if (SandFall.gameController.arenaExist(args[1]))
							{
								if (SandFall.gameController.getArena(args[1]).isEnabled())
									SandFall.gameController.playerStart(sender.getName(), args[1]);
								else
									sender.sendMessage(SandFall.pluginPrefix + ChatColor.RED + "That arena has been disabled!");
							}
							else
							{
								sender.sendMessage(SandFall.pluginPrefix + ChatColor.RED + "That arena does not exist!");
							}
						}
						else
						{
							sender.sendMessage(SandFall.pluginPrefix + ChatColor.RED + "SandFall needs to be setup by an admin before you can play!");
						}
						
					}
					else
					{
						sender.sendMessage(SandFall.pluginPrefix + ChatColor.AQUA + "You're currently playing SandFall!");
					}
				}
				else
				{
					sender.sendMessage(SandFall.pluginPrefix + ChatColor.RED + "Unknown SandFall command. Type " + ChatColor.YELLOW + ChatColor.BOLD + "/sandfall help");
				}
			}
		}
		else
		{
			//No permissions
			sender.sendMessage(SandFall.pluginPrefix + ChatColor.RED + "You do not have permissions to access SandFall");
		}
		return true;
	}
}
