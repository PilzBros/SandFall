package com.PilzBros.SandFall.Runnable;
import java.util.Map.Entry;

import org.bukkit.scheduler.BukkitRunnable;

import com.PilzBros.SandFall.SandFall;
import com.PilzBros.SandFall.Item.Arena;


public class CountdownRunnable extends BukkitRunnable
{
	@Override
	public void run() 
	{
		if (System.currentTimeMillis() - SandFall.lastTimeCheck >= 1000)
		{
			int timePassed = (int) Math.round((double) (System.currentTimeMillis() - SandFall.lastTimeCheck) / 1000.0);
			SandFall.lastTimeCheck = System.currentTimeMillis();
		
			for(Entry<String, Arena> a : SandFall.gameController.arenas.entrySet()) 
			{
				Arena arena = a.getValue();
				if (arena.gameManager.inWaiting)
				{
						if (!arena.gameManager.playing.isEmpty())
						{
							if (arena.gameManager.countdownSeconds <= 0)
							{
								arena.gameManager.timesUp();
							}
							else
							{
								arena.gameManager.countdownSeconds--;
								arena.gameManager.scoreboard.updateCountown();
								arena.signController.updateSigns();
								
								if (arena.gameManager.countdownSeconds <= 0)
								{
									arena.gameManager.timesUp();
								}
								else if (arena.gameManager.countdownSeconds <= 5)
								{
									if (arena.gameManager.countdownSeconds == 5)
										arena.gameManager.countdownNotify();
									else if (arena.gameManager.countdownSeconds == 4)
										arena.gameManager.countdownNotify();
									else if (arena.gameManager.countdownSeconds == 3)
										arena.gameManager.countdownNotify();
									else if (arena.gameManager.countdownSeconds == 2)
										arena.gameManager.countdownNotify();
									else if (arena.gameManager.countdownSeconds == 1)
										arena.gameManager.countdownNotify();
				
								}
							}
						}
	
					}
					else if (arena.gameManager.inProgress)
					{
						//Game is in progress
						if ((arena.gameManager.timeLeft > 10 || arena.gameManager.timeLeft < 10) && arena.gameManager.timeLeft != 0)
						{
							arena.gameManager.timeLeft--;
						}
						else if (arena.gameManager.timeLeft == 10)
						{
							//Make it rain
							arena.makeItRain();
							arena.gameManager.timeLeft--;
						}
						else if (arena.gameManager.timeLeft == 0)
						{
							//Force the game to end
							arena.gameManager.forceEnd();
						}
					}
			}
		}
	}
}
