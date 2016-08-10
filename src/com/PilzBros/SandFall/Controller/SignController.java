package com.PilzBros.SandFall.Controller;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;

import com.PilzBros.SandFall.SandFall;
import com.PilzBros.SandFall.Item.Arena;
import com.PilzBros.SandFall.Item.Signs;
import com.PilzBros.SandFall.Plugin.Setting;
import com.PilzBros.SandFall.Plugin.Settings;

public class SignController 
{
	Arena arena;
	
	public SignController(Arena a)
	{
		arena = a;
	}
	
	public void updateSigns()
	{
		//Bukkit.broadcastMessage("Arena " + arena.name + " sign update called");
		if (arena.gameManager.inWaiting)
		{
			setSignsWaiting();
		}
		else if (arena.gameManager.inProgress)
		{
			setSignsInprogress();
		}
		else
		{
			if (arena.isEnabled())
				setSignsJoin();
			else
				setSignsDisabled();
		}
	}
	
	/**
     * Restores signs to "Join"
     */
	public void restoreSigns()
	{
		setSignsJoin();
	}
	
	public void testSigns()
	{
		List<Sign> tmp = arena.getSigns();
		Iterator<Sign> i = tmp.iterator();
		while (i.hasNext())
		{
			Sign tmpSign = i.next();
			tmpSign.setLine(0, SandFall.signPrefix);
			//tmpSign.setLine(1, "");
			tmpSign.setLine(2, ChatColor.RED + "TESTING");
			tmpSign.setLine(3, "");
			tmpSign.update();
		}
	}
	
	/**
     * Sets signs to "Join"
     */
	private void setSignsJoin()
	{
		List<Sign> tmp = arena.getSigns();
		Iterator<Sign> i = tmp.iterator();
		while (i.hasNext())
		{
			Sign tmpSign = i.next();
			tmpSign.setLine(0, SandFall.signPrefix);
			//tmpSign.setLine(1, "");
			tmpSign.setLine(2, SandFall.signJoinText);
			tmpSign.setLine(3, "");
			tmpSign.update();
		}
	}
	
	private void setSignsWaiting()
	{
		List<Sign> tmp = arena.getSigns();
		Iterator<Sign> i = tmp.iterator();
		while (i.hasNext())
		{
			Sign tmpSign = i.next();
			tmpSign.setLine(0, SandFall.signPrefix);
			//tmpSign.setLine(1, "");
			tmpSign.setLine(2, SandFall.signWaitingText);
			tmpSign.setLine(3, ChatColor.BOLD + "" + ChatColor.GREEN + "" + arena.gameManager.countdownSeconds);
			tmpSign.update();
		}
	}
	
	private void setSignsInprogress()
	{
		List<Sign> tmp = arena.getSigns();
		Iterator<Sign> i = tmp.iterator();
		while (i.hasNext())
		{
			Sign tmpSign = i.next();
			tmpSign.setLine(0, SandFall.signPrefix);
			//tmpSign.setLine(1, "");
			tmpSign.setLine(2, SandFall.signInprogressText);
			if (Settings.getGlobalBoolean(Setting.SignStats))
			{
				tmpSign.setLine(3,ChatColor.GREEN + "" + arena.gameManager.scoreboardPlaying + ChatColor.BLACK + "/" + ChatColor.RED + "" + (arena.gameManager.scoreboardDead));
			}
			else
			{
				tmpSign.setLine(3, "");
			}
				tmpSign.update();
		}
	}
	
	private void setSignsDisabled()
	{
		List<Sign> tmp = arena.getSigns();
		Iterator<Sign> i = tmp.iterator();
		while (i.hasNext())
		{
			Sign tmpSign = i.next();
			tmpSign.setLine(0, SandFall.signPrefix);
			//tmpSign.setLine(1, "");
			tmpSign.setLine(2, ChatColor.RED + "" + ChatColor.BOLD + "Disabled");
			tmpSign.setLine(3,"");
			tmpSign.update();
		}
	}
	
	protected void setSignsRemoved()
	{
		List<Sign> tmp = arena.getSigns();
		Iterator<Sign> i = tmp.iterator();
		while (i.hasNext())
		{
			Sign tmpSign = i.next();
			tmpSign.setLine(0, SandFall.signPrefix);
			//tmpSign.setLine(1, "");
			tmpSign.setLine(2, ChatColor.RED + "" + ChatColor.BOLD + "Deleted");
			tmpSign.setLine(3,"");
			tmpSign.update();
		}
	}
	
	

}
