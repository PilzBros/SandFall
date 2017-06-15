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

				//Determine arena game status
				if (arena.gameManager.inWaiting)
				{
						//Check to see if there are players in the arena
						if (!arena.gameManager.playing.isEmpty())
						{
							//Check to see if the game time has expired
							if (arena.gameManager.countdownSeconds <= 0)
							{
								arena.gameManager.timesUp();
							}
							else
							{
								//Decrement second from the game time
								arena.gameManager.countdownSeconds--;
								arena.gameManager.scoreboard.updateCountown();
								arena.signController.updateSigns(); //This could be pretty resource intensive
								
								if (arena.gameManager.countdownSeconds <= 0)
								{
									arena.gameManager.timesUp();
								}
								else if (arena.gameManager.countdownSeconds < 5 && arena.gameManager.countdownSeconds > 0)
								{
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
							//Make it rain sand
							arena.makeItRain();
							arena.gameManager.timeLeft--;
						}
						else if (arena.gameManager.timeLeft == 0)
						{
							//Force the game to end if someone is still somehow alive
							arena.gameManager.forceEnd();
						}
					}
			}
		}
	}
}
