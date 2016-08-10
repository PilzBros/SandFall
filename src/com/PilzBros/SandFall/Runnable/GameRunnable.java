package com.PilzBros.SandFall.Runnable;

import org.bukkit.scheduler.BukkitRunnable;

import com.PilzBros.SandFall.SandFall;

public class GameRunnable extends BukkitRunnable
{
	@Override
	public void run() 
	{
		SandFall.gameController.autoCheck();
	}

}
