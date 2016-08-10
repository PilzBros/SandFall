package com.PilzBros.SandFall.Manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.PilzBros.SandFall.SandFall;
import com.PilzBros.SandFall.Item.Arena;
import com.PilzBros.SandFall.Java.DLL;
import com.PilzBros.SandFall.Plugin.Setting;
import com.PilzBros.SandFall.Plugin.Settings;

public class GameManager 
{

	public ScoreboardManager scoreboard;
	public PlayerManager pm;
	public List<String> playing = new ArrayList<String>();
	public List<String> respawn = new ArrayList<String>();
	public Player tempRespawn;
	public Arena arena;
	public boolean inProgress;
	public boolean inWaiting;
	public boolean waitCountdown;
	public int scoreboardPlaying;
	public int scoreboardDead;
	public int countdownSeconds;
	public int timeLeft;
	
	
	
	public GameManager(Arena arena)
	{
		this.arena = arena;
		this.scoreboard = new ScoreboardManager(this);
		this.pm = new PlayerManager(this);
		this.tempRespawn = null;
		inProgress = false;
		inWaiting = false;
		waitCountdown = false;
		scoreboardPlaying = 0;
		scoreboardDead = 0;
		timeLeft = Settings.getGlobalInt(Setting.gameDuration);
		countdownSeconds = Settings.getGlobalInt(Setting.CountdownTime);
	}
	
	public void bootup()
	{
		playing.clear();
		managerSetup();
	}
	
	protected void managerSetup()
	{
		if (arena != null)
			arena.reset();
		
		inProgress = false;
		inWaiting = false;
		waitCountdown = false;
		scoreboardPlaying = 0;
		scoreboardDead = 0;
		countdownSeconds = Settings.getGlobalInt(Setting.CountdownTime);
		timeLeft = Settings.getGlobalInt(Setting.gameDuration);
	}

	/**
     * Returns if game is currently in sessions
     */
	public boolean inProgress()
	{
		if (playing.isEmpty())
			return false;
		else
			return true;
	}
	
	/**
     * Called automatically by timed task to updtae scoreboards and current game status
     */
	public void autoCheck()
	{
		//Scoreboard Management
		if (inWaiting || inProgress)
		{
			scoreboard.updateBoard();
			scoreboard.displayCountdown();
			scoreboard.displayScore();
			if (inProgress && Settings.getGlobalBoolean(Setting.SignStats))
				arena.signController.updateSigns();
		}
		
		
		if (!inProgress())
		{
			inProgress = false;
		}
		
	}
	
	/**
     * Starts game after timer countdown
     */
	public void timesUp()
	{
		inWaiting = false;
		inProgress = true;
		gameBegin();
		arena.signController.updateSigns();
	}
	
	/**
     * Called by user command to start playing
     *
     * @param player Player who issues play command
     * 
     */
	public void start(Player player)
	{
		//called by game controller
		if (inProgress)
		{
			player.sendMessage(SandFall.pluginPrefix + ChatColor.GOLD + Settings.getGlobalString(Setting.GameAlreadyInSession));
		}
		else if (inWaiting)
		{
			//add player into game!
			pm.preparePlayer(player);
		}
		else
		{
			//not playing or in progress, add player
			inWaiting = true;
			pm.preparePlayer(player);
		}
	}
	
	/**
     * Called when player quits game
     *
     * @param player Player who quit game
     * 
     */
	public void quit(Player player)
	{
		pm.leavePlaying(player);
		pm.restorePlayer(player);
		player.sendMessage(SandFall.pluginPrefix + ChatColor.GOLD + Settings.getGlobalString(Setting.QuitMessage));
	}
	
	/**
     * Called when player quits game
     *
     * @param player Player who quit game
     * 
     */
	public void kick(Player player)
	{
		pm.leavePlaying(player);
		pm.restorePlayer(player);
		player.sendMessage(SandFall.pluginPrefix + ChatColor.GOLD + Settings.getGlobalString(Setting.KickMessage));
	}
	
	/**
     * Called when a player dies, updates it in game
     *
     * @param player Player who died
     * 
     */
	public void death(Player player)
	{ 
		//Update scoreboard numbers
		scoreboardPlaying--;
		scoreboardDead++;
	
		//teleport to return point on respawn
		pm.leavePlaying(player);
		pm.restorePlayer(player);
		
		//see if player was last one standing
		if (inProgress())
		{
			if (scoreboardPlaying <= 1)
			{
				//one man standing, let them know they won
				List<String> players = playing;
				Iterator<String> i = players.iterator();
				while (i.hasNext())
				{
					Player p = Bukkit.getPlayer(i.next());
					pm.winNotify(p);
					pm.leavePlaying(p);
					pm.restorePlayer(p);
				}
				
				gameOver();
			}
		}
		
		player.sendMessage(SandFall.pluginPrefix + ChatColor.GOLD + Settings.getGlobalString(Setting.DeathMessage));
	}
	
	/**
     * Notifies users who are playing of server reload
     */
	public void serverReload()
	{
		for (Player online : Bukkit.getOnlinePlayers())
		{
			if (pm.isPlaying(online))
			{
				pm.leavePlaying(online);
				pm.restorePlayer(online);
				pm.reloadNotify(online);
			}
		}
		
	}
	
	/**
     * Forces all users who are playing out of game, ending game
     */
	public void forceEnd()
	{
		for (Player online : Bukkit.getOnlinePlayers())
		{
			if (pm.isPlaying(online))
			{
				pm.leavePlaying(online);
				pm.restorePlayer(online);
				pm.forceEndNotify(online);
			}
		}
		
		gameOver();
		
	}

	/**
     * Notify waiting users that the game has begun
     */
	private void gameBegin()
	{
		arena.reset();
		List<String> players = playing;
		Iterator<String> i = players.iterator();
		while (i.hasNext())
		{
			Player p = Bukkit.getPlayer(i.next());
			pm.beginGameMessage(p);
		}
	}
	
	/**
     * Restores arena and game settings after every game
     */
	protected void gameOver()
	{
		managerSetup();
		arena.signController.restoreSigns();
	}

	public void countdownNotify()
	{
		List<String> players = playing;
		Iterator<String> i = players.iterator();
		while (i.hasNext())
		{
			String now = i.next();
			Player p = Bukkit.getPlayer(now);
			pm.countdownNotify(p);
		}
	}
}
