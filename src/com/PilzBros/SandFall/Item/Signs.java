package com.PilzBros.SandFall.Item;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;

import com.PilzBros.SandFall.SandFall;


public class Signs 
{
	private Sign sign;
	private Arena arena;
	
	public Signs(Sign s, Arena a)
	{
		sign = s;
		arena = a;
		prepare();
	}
	
	public void update()
	{
		if (arena.gameManager.inProgress)
		{
			setInProgress();
		}
		else if (arena.gameManager.inWaiting)
		{
			setWaiting();
		}
		else
		{
			setJoin();
		}
	}
	
	private void prepare()
	{
		sign.setLine(0, SandFall.signPrefix);
		sign.setLine(1, arena.getName());
		sign.setLine(2, "");
		sign.setLine(3, "");
		sign.update();
		
		setJoin();
	}
	
	private void setJoin()
	{
		sign.setLine(2, SandFall.signJoinText);
		sign.update();
	}
	
	private void setWaiting()
	{
		sign.setLine(2, SandFall.signWaitingText);
		sign.update();
	}
	
	private void setInProgress()
	{
		sign.setLine(2, SandFall.signInprogressText);
		sign.update();
	}
	
	public void setDeleted()
	{
		sign.setLine(2, ChatColor.RED + "" + ChatColor.BOLD + "Deleted");
	}

}
