package com.PilzBros.SandFall.Manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import com.PilzBros.SandFall.SandFall;
import com.PilzBros.SandFall.Plugin.Setting;
import com.PilzBros.SandFall.Plugin.Settings;

public class ScoreboardManager 
{
	protected BoardManager countdown;
	protected BoardManager score;
	protected BoardManager blank;
	private GameManager gameManager;
	
	public ScoreboardManager(GameManager gm)
	{
		this.countdown = new BoardManager("SandFallCountdown", ChatColor.GOLD + "SandFall", DisplaySlot.SIDEBAR);
		this.score =  new BoardManager("SandFallScore", ChatColor.GOLD + "SandFall", DisplaySlot.SIDEBAR);
		this.blank = new BoardManager("test","dummy", DisplaySlot.SIDEBAR);
		this.gameManager = gm;
		//prepareBoard();
	}
	
	private void prepareBoard()
	{
		score.setObjectiveScore(ChatColor.GREEN + "Alive", gameManager.scoreboardPlaying);
		score.setObjectiveScore(ChatColor.RED + "Dead", gameManager.scoreboardDead);
		countdown.setObjectiveScore(ChatColor.AQUA + "Starts In", gameManager.countdownSeconds);
	}
	
	public void updateBoard()
	{
		//called by Autocheck()
		score.setObjectiveScore(ChatColor.GREEN + "Alive", gameManager.scoreboardPlaying);
		score.setObjectiveScore(ChatColor.RED + "Dead", gameManager.scoreboardDead);
		
		//Display time left is there is less than 60 seconds
		if (gameManager.timeLeft < 60)
		{
			score.setObjectiveScore(ChatColor.AQUA + "Time Left", gameManager.timeLeft);
		}
	}
	
	public void updateCountown()
	{
		countdown.setObjectiveScore(ChatColor.GREEN + "Starts In", gameManager.countdownSeconds);
	}
	
	public void displayCountdown()
	{
		if (gameManager.inWaiting)
		{
			//Only display if people are waiting
			for (Player online : Bukkit.getOnlinePlayers())
			{
				if (gameManager.pm.isPlaying(online))
				{
					countdown.setScoreboard(online);
				}
			}
		}
	}
	
	public void displayScore()
	{
		if (gameManager.inProgress)
		{
			//only display if game is ongoing
			for (Player online : Bukkit.getOnlinePlayers())
			{
				if (gameManager.pm.isPlaying(online))
				{
					score.setScoreboard(online);
				}
			}
		}
	}
	
	public void removeBoard(Player player)
	{
		blank.setScoreboard(player);
	}

}
