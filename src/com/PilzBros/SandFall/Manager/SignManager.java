package com.PilzBros.SandFall.Manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

import com.PilzBros.SandFall.SandFall;
import com.PilzBros.SandFall.Item.Arena;
import com.PilzBros.SandFall.Item.Signs;

public class SignManager 
{
	
	public SignManager()
	{
		//
	}
	
	public void addSign(Block block, Arena arena)
	{
		
		Block clickedBlock = block;
		Sign thisSign = (Sign) clickedBlock.getState();
		String[] lines = thisSign.getLines();
		
			thisSign.setLine(0, SandFall.signPrefix);
			thisSign.setLine(1, arena.name);
			thisSign.setLine(2, SandFall.signJoinText);
			thisSign.update();
			

			
			SandFall.gameController.getArena(arena.name).addSign(thisSign);
			SandFall.IO.storeSign(clickedBlock.getWorld().getName(), clickedBlock.getX(), clickedBlock.getY(), clickedBlock.getZ(), arena.name);
		
		
	}
	
	public void removeSign(BlockBreakEvent event)
	{
		Block clickedBlock = event.getBlock();
		SandFall.IO.removeSign(clickedBlock.getWorld().getName(), clickedBlock.getX(), clickedBlock.getY(), clickedBlock.getZ());
	}
	
	
	
	
	//reset signs

}
