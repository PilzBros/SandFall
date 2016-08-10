package com.PilzBros.SandFall;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.PilzBros.SandFall.Plugin.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.PilzBros.SandFall.Command.SFAdminCommand;
import com.PilzBros.SandFall.Command.SFCommand;
import com.PilzBros.SandFall.Controller.GameController;
import com.PilzBros.SandFall.Listener.PlayerListener;
import com.PilzBros.SandFall.Listener.SignListener;
import com.PilzBros.SandFall.Manager.GameManager;
import com.PilzBros.SandFall.Manager.SignManager;
import com.PilzBros.SandFall.Runnable.GameRunnable;
import com.PilzBros.SandFall.Runnable.CountdownRunnable;



public class SandFall extends JavaPlugin implements Listener
{
	//Strings
	public static final String pluginName = "SandFall";
	public static final String pluginVersion = "2.1.1";
	public static final String pluginPrefix = ChatColor.GOLD + "[SandFall] " + ChatColor.WHITE;
	public static final String pluginAdminPrefix = ChatColor.GOLD + "[SandFall Admin] " + ChatColor.WHITE;
	public static final String signPrefix = ChatColor.GOLD + "[SandFall]";
	public static final String consolePrefix = "[SandFall]";
	public static final String pluginWebsite = "http://sandfall.austinpilz.com";
	public static final String signJoinText = ChatColor.GREEN + "Join";
	public static final String signWaitingText = ChatColor.AQUA + "Starting in";
	public static final String signInprogressText = ChatColor.LIGHT_PURPLE + "In Progress";
	public static final Logger log = Logger.getLogger("Minecraft");
	
	//Vars
	public static boolean updateNeeded;
	public static SandFall instance;
	public static GameController gameController;
	public static SignManager signManager;
	public static InputOutput IO;
	public static SpigotUpdateChecker updateChecker;
	//protected static ProtocolManager protocolManager;
	
	public static long lastTimeCheck;
	
	@Override
	public void onLoad() 
	{
	    //protocolManager = ProtocolLibrary.getProtocolManager();
	}
	
	@SuppressWarnings("deprecation")
	public void onEnable()
	{	
		long startMili = System.currentTimeMillis() % 1000;
		//Init.
		instance = this;
		
		//Initial IO
		this.IO = new InputOutput();
		IO.LoadSettings();
		
		//Objects
		gameController = new GameController();
		signManager = new SignManager();
		this.lastTimeCheck = 0;

		
		//IO
		IO.prepareDB();
		IO.updateDB();
		IO.loadArena();
		IO.loadSigns();
		
		//Commands	
		getCommand("sf").setExecutor(new SFCommand(this));
		getCommand("sfa").setExecutor(new SFAdminCommand(this));
		getCommand("sandfall").setExecutor(new SFCommand(this));
		getCommand("sandfalladmin").setExecutor(new SFAdminCommand(this));
		
		
		
		//Listeners
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
		getServer().getPluginManager().registerEvents(new SignListener(), this);
		
		
		
		//New Timers / Tasks
		BukkitTask managerTask = Bukkit.getScheduler().runTaskTimer(this, new GameRunnable(), 20, 20);
		BukkitTask countdownTask = Bukkit.getScheduler().runTaskTimer(this, new CountdownRunnable(), 20, 20);
		
		//Update
		this.updateChecker = new SpigotUpdateChecker();
		try
		{
			this.updateChecker.checkUpdate(this.pluginVersion);
			if (this.updateChecker.isUpdateNeeded())
			{
				log.log(Level.INFO, this.consolePrefix + " SandFall has an update available! Currently running v" + SandFall.pluginVersion + " and the most recent version is v" + SandFall.updateChecker.getLatestVersion() + " Please visit " + SandFall.pluginWebsite + " to update");
			}
		}
		catch (Exception e)
		{
			log.log(Level.INFO, this.consolePrefix + " Error while checking for update :(");
		}

		
		//Metrics Reporting
				// - Option in global.yml to opt-out
				if (Settings.getGlobalBoolean(Setting.ReportMetrics))
				{
					try 
					{
					    MetricsLite metrics = new MetricsLite(this);
					    metrics.start();
					   log.log(Level.INFO, "[SandFall] Metrics submitted");
					} 
					catch (IOException e) 
					{
					log.log(Level.WARNING, "[SandFall] Metrics reporting failed");
					}
				}
				else
				{
					log.log(Level.INFO, "[SandFall] Metric reporting disabled");
				}
				
				log.log(Level.INFO, "[SandFall] Bootup took " + (System.currentTimeMillis() % 1000 - startMili) + " ms"); 
	}
	
	public void onReload()
	{
		gameController.serverReload();
	}
	
	public void onDisable()
	{	
		gameController.serverReload();
	}
	
}
