package com.PilzBros.SandFall.Plugin;

public enum Setting {

	//Update
	ReportMetrics("MetricReporting",true),
	
	//Setup
	SelectionTool("Setup.SelectionTool", 284),
	
	//Game Settings
	CountdownTime("Gameplay.WaitingTime", 15),
	gameDuration("GamePlay.GameDuration", 300),
	gameDurationScore("GamePlay.DisplayDurationWithSecLeft", 10),
	FoodLevel("Gameplay.FoodLevel", 0),
	FireQuantity("Gameplay.FireQuantity", 100),
	InGameCommands("Gameplay.InGameCommands", false),
	InGameTeleport("Gameplay.Teleporting", false),
	SignStats("Gameplay.RealtimeSignStats", true),
	FriendlyFire("Gameplay.AllowFriendlyFire", false),
	
	//Game Messages
	WaitingMessage("Messages.InGame.WaitingMessage","SandFall will begin after the countdown finishes. To leave, type /sandfall quit"),
	GameBeginMessage("Messages.InGame.GameBeginMessage","SandFall game starts, now!"),
	QuitMessage("Messages.InGame.QuitMessage","You quit SandFall, thanks for playing!"),
	KickMessage("Messages.InGame.KickMessage","You have been kicked from SandFall!"),
	DeathMessage("Messages.InGame.DeathMessage","You died, thanks for playing SandFall!"),
	WinMessage("Messages.InGame.WinMessage","Congratulations! You won SandFall!"),
	ReloadMessage("Messages.InGame.ReloadMessage", "A server reload has forced you out of SandFall"),
	GameAlreadyInSession("Messages.InGame.GameAlreadyOngoing","A round of SandFall is already in progress, you can play when it's done!"),
	
	//Other Messages
	CommandDisabledMessage("Messages.Etc.CommandsDisabled","Commands are disabled during SandFall gameplay"),
	
	//Etc
	NotifyOnAustinPilz("NotifyOnPluginCreatorJoin", true);
	
	private String name;
	private Object def;
	
	private Setting(String Name, Object Def)
	{
		name = Name;
		def = Def;
	}
	
	public String getString()
	{
		return name;
	}
	
	public Object getDefault()
	{
		return def;
	}
}
