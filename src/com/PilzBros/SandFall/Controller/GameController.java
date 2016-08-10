package com.PilzBros.SandFall.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.PilzBros.SandFall.SandFall;
import com.PilzBros.SandFall.Item.Arena;

public class GameController 
{
	public HashMap<String, Arena> arenas;
	public HashMap<String, Arena> players;
	public HashMap<String, Arena> respawn;
	
	public GameController()
	{
		arenas = new HashMap<String, Arena>();
		players = new HashMap<String, Arena>();
		respawn = new HashMap<String, Arena>();
	}
	
	public void reloadController()
	{
		//
	}
	
	public void autoCheck()
	{
		Iterator it = arenas.entrySet().iterator();
		while (it.hasNext()) 
		{
		    Map.Entry entry = (Map.Entry) it.next();
		    String name = (String)entry.getKey();
		    Arena arena = (Arena)entry.getValue();
		    arena.gameManager.autoCheck();
		}
	}
	
	public void arenaSignRestore()
	{
		Iterator it = arenas.entrySet().iterator();
		while (it.hasNext()) 
		{
		    Map.Entry entry = (Map.Entry) it.next();
		    String name = (String)entry.getKey();
		    Arena arena = (Arena)entry.getValue();
		    arena.signController.restoreSigns();
		}
	}
	
	public void addArena(Arena a)
	{
		arenas.put(a.getName(), a);
	}
	
	public void removeArena(Arena a)
	{
		a.signController.setSignsRemoved();
		arenas.remove(a.getName());
		SandFall.IO.deleteArena(a);
	}
	public Arena getArena(String name)
	{
		Arena tmp;
	    tmp = arenas.get(name.toLowerCase());
	    return tmp;
		
	}
	public Arena getPlayerArena(String player)
	{
		Arena tmp;
		tmp = players.get(player);
		return tmp;
	}
	public void arenasLoaded()
	{
		//called by IO after arenas have been loaded
		if (!arenas.isEmpty())
		{
			prepareArenas();
		}
	}
	public void serverReload()
	{
		Iterator it = arenas.entrySet().iterator();
		while (it.hasNext()) 
		{
		    Map.Entry entry = (Map.Entry) it.next();
		    String name = (String)entry.getKey();
		    Arena arena = (Arena)entry.getValue();
		    arena.gameManager.serverReload();
		}
	}
	private void prepareArenas()
	{		
		Iterator it = arenas.entrySet().iterator();
		while (it.hasNext()) 
		{
		    Map.Entry entry = (Map.Entry) it.next();
		    String name = (String)entry.getKey();
		    Arena arena = (Arena)entry.getValue();
		    SandFall.log.log(Level.INFO, "Arena (" + arena.name + ") prepared");
		    arena.reset();
		}
		
	}
	public void addPlayer(String name, String arena)
	{
		players.put(name, getArena(arena));
	}
	
	public void removePlayer(String name)
	{
		if (playerPlaying(name))
			players.remove(name);
	}
	
	public boolean arenasExist()
	{
		if (!arenas.isEmpty())
			return true;
		else
			return false;
	}
	public boolean arenaExist(String name)
	{
		if (arenas.containsKey(name.toLowerCase()))
			return true;
		else
			return false;
	}
	public boolean playerPlaying(String name)
	{
		if (players.containsKey(name))
			return true;
		else
			return false;
	}
	public boolean playerPlaying(Player player)
	{
		if (players.containsKey(player.getName()))
			return true;
		else
			return false;
	}
	/**
     * Checks to see if player is in repsawn queue
     *
     * @param player to check respawn status
     * 
     */
	public boolean playerToRespawn(Player player)
	{
		if (respawn.containsKey(player.getName()))
			return true;
		else 
			return false;
	}
	/**
     * Returns arena of player who died
     * @param player Player to return respawn arena init
     * @return Arena in which player was playing on death
     */
	public Arena getPlayerRespawnArena(Player player)
	{
		return respawn.get(player.getName());
	}
	public void kickPlayer(String name)
	{
		if (playerPlaying(name))
		{
			players.get(name).gameManager.kick(Bukkit.getPlayer(name));
			removePlayer(name);
		}
	}
	public void playerLogoff(Player player)
	{
		players.get(player.getName()).gameManager.pm.leavePlaying(player);
		removePlayer(player.getName());
	}
	public void playerDeath(Player player)
	{
		respawn.put(player.getName(), getPlayerArena(player.getName()));
		players.get(player.getName()).gameManager.death(player);
		removePlayer(player.getName());

	}
	public void playerRespawn(Player player)
	{
		//
	}
	
	public void playerStart(String name, String arena)
	{
		//called when player executes command or clicks sign
		if (arenaExist(arena))
		{
			getArena(arena).gameManager.start(Bukkit.getPlayer(name));
			addPlayer(name, arena);
		}
	}
	
	public void playerQuit(String name)
	{
		if (playerPlaying(name))
		{
			getPlayerArena(name).gameManager.quit(Bukkit.getPlayer(name));
			removePlayer(name);
		}
			
	}
	
	
	
	
	
	
	//needs to auto call the autocheck() of arena which calls the auto check of game manager
}
