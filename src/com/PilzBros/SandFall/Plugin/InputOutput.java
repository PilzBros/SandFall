package com.PilzBros.SandFall.Plugin;




import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import com.PilzBros.SandFall.SandFall;
import com.PilzBros.SandFall.Item.Arena;



public class InputOutput 
{
    public static YamlConfiguration global;
    private static Connection connection;
    
	public InputOutput()
	{
		if (!SandFall.instance.getDataFolder().exists()) 
		{
			try 
			{
				(SandFall.instance.getDataFolder()).mkdir();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		global = new YamlConfiguration();
	}
   
    
    public void LoadSettings()
	{
    	try {
    		if (!new File(SandFall.instance.getDataFolder(),"global.yml").exists()) global.save(new File(SandFall.instance.getDataFolder(),"global.yml"));

    		global.load(new File(SandFall.instance.getDataFolder(),"global.yml"));
	    	for (Setting s : Setting.values())
	    	{
	    		if (global.get(s.getString()) == null) global.set(s.getString(), s.getDefault());
	    	}
	    	
	    	
	    	global.save(new File (SandFall.instance.getDataFolder(),"global.yml"));
	    	

		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
    
    
    
    
    
    public static synchronized Connection getConnection() {
    	if (connection == null) connection = createConnection();
            try
            {
                if(connection.isClosed()) connection = createConnection();
            } 
            catch (SQLException ex) 
            {
                ex.printStackTrace();
            }
        
    	return connection;
    }
    private static Connection createConnection() {
        
    	try
    	{
                Class.forName("org.sqlite.JDBC");
                Connection ret = DriverManager.getConnection("jdbc:sqlite:" +  new File(SandFall.instance.getDataFolder().getPath(), "sandfall.sqlite").getPath());
                ret.setAutoCommit(false);
                SandFall.log.log(Level.INFO, "[SandFall] Using SQLite - Connection succeeded");
                return ret;
          
 
        } 
        catch (ClassNotFoundException e) 
        {
        	 SandFall.log.log(Level.SEVERE, "Connection to the MySQL database failed. Plugin startup terminated");
        	 e.printStackTrace();
        	 return null;
        } 
        catch (SQLException e) 
        {
        	SandFall.log.log(Level.SEVERE, "Connection to the MySQL database failed. Plugin startup terminated");
        	e.printStackTrace();
        	return null;
        }
    }
    
    public static synchronized void freeConnection() {
		Connection conn = getConnection();
        if(conn != null) {
            try {
            	conn.close();
            	conn = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void prepareDB()
    {
    	Connection conn = getConnection();
        Statement st = null;
        try 
        {
        		st = conn.createStatement();
            	st.executeUpdate("CREATE TABLE IF NOT EXISTS \"sandfall_arena\" (\"Name\" VARCHAR PRIMARY KEY  NOT NULL , \"X1\" DOUBLE, \"Y1\" DOUBLE, \"Z1\" DOUBLE, \"X2\" DOUBLE, \"Y2\" DOUBLE, \"Z2\" DOUBLE, \"StartX\" DOUBLE, \"StartY\" DOUBLE, \"StartZ\" DOUBLE, \"ReturnX\" DOUBLE, \"ReturnY\" DOUBLE, \"ReturnZ\" DOUBLE, \"Wood1X\" DOUBLE, \"Wood1Y\" DOUBLE, \"Wood1Z\" DOUBLE, \"Wood2X\" DOUBLE, \"Wood2Y\" DOUBLE, \"Wood2Z\" DOUBLE, \"Sand1X\" DOUBLE, \"Sand1Y\" DOUBLE, \"Sand1Z\" DOUBLE, \"Sand2X\" DOUBLE, \"Sand2Y\" DOUBLE, \"Sand2Z\" DOUBLE, \"playWorld\" VARCHAR, \"returnWorld\" VARCHAR)");
                //st.executeUpdate("CREATE TABLE IF NOT EXISTS \"saw_locations\" (\"Name\" VARCHAR PRIMARY KEY  NOT NULL , \"X\" DOUBLE, \"Y\" DOUBLE, \"Z\" DOUBLE, \"World\" VARCHAR, STRING)");
                st.executeUpdate("CREATE TABLE IF NOT EXISTS \"sandfall_signs\" (\"X\" DOUBLE, \"Y\" DOUBLE, \"Z\" DOUBLE, \"World\" VARCHAR, \"Arena\" VARCHAR)");
                //st.executeUpdate("CREATE TABLE IF NOT EXISTS \"saw_banned\" (\"Name\" VARCHAR)");
                conn.commit();
                st.close();

        } 
        catch (SQLException e) 
        {
            SandFall.log.log(Level.WARNING, "[SandFall] Error while preparing database tables! - " + e.getMessage());
            e.printStackTrace();
        }
        catch (Exception e) 
		{
        	e.printStackTrace();
		}
    }
    
    public void updateDB()
    {
    	Update("SELECT Arena  FROM sandfall_signs", "ALTER TABLE sandfall_signs ADD Arena VARCHAR;", "ALTER TABLE sandfall_signs ADD Arena varchar(250);" );
    }
    public void Update(String check, String sql)
    {
    	Update(check, sql, sql);
    }
    
    public void Update(String check, String sqlite, String mysql)
    {
    	try
    	{
    		Statement statement = getConnection().createStatement();
			statement.executeQuery(check);
			statement.close();
    	}
    	catch(SQLException ex)
    	{
    		SandFall.log.log(Level.INFO, "[SandFall] Updating database");
    		try {
    			String[] query;
    			
    			query = sqlite.split(";");
            	Connection conn = getConnection();
    			Statement st = conn.createStatement();
    			for (String q : query)	
    				st.executeUpdate(q);
    			conn.commit();
    			st.close();
    		} 
    		catch (SQLException e)
    		{
    			SandFall.log.log(Level.SEVERE, "[SandFall] Error while updating tables to the new version - " + e.getMessage());
                e.printStackTrace();
    		}
    	}
        
	}
    
    public void loadArena()
    {
    	try
		{

	    	Connection conn;
			PreparedStatement ps = null;
			ResultSet result = null;
			conn = getConnection();
			ps = conn.prepareStatement("SELECT `Name`, `X1`, `Y1`, `Z1`, `X2`, `Y2`, `Z2`, `StartX`, `StartY`, `StartZ`, `ReturnX`, `ReturnY`, `ReturnZ`, `Wood1X`, `Wood1Y`, `Wood1Z`, `Wood2X`, `Wood2Y`, `Wood2Z`, `Sand1X`, `Sand1Y`, `Sand1Z`, `Sand2X`, `Sand2Y`, `Sand2Z`, `playWorld`, `returnWorld` FROM `sandfall_arena`");
			result = ps.executeQuery();
			
			int count = 0;
			while (result.next())
			{
				Arena tmp = new Arena(result.getString("Name"), result.getDouble("X1"), result.getDouble("Y1"), result.getDouble("Z1"), result.getDouble("X2"), result.getDouble("Y2"), result.getDouble("Z2"), result.getDouble("StartX"), result.getDouble("StartY"), result.getDouble("StartZ"), result.getDouble("ReturnX"), result.getDouble("ReturnY"), result.getDouble("ReturnZ"), result.getString("playWorld"), result.getString("returnWorld"), result.getDouble("Sand1X"), result.getDouble("Sand1Y"), result.getDouble("Sand1Z"), result.getDouble("Sand2X"), result.getDouble("Sand2Y"), result.getDouble("Sand2Z"), result.getDouble("Wood1X"), result.getDouble("Wood1Y"), result.getDouble("Wood1Z"), result.getDouble("Wood2X"), result.getDouble("Wood2Y"), result.getDouble("Wood2Z"));
				
				SandFall.gameController.addArena(tmp);
				
						
						
						//temp = new Arena(result.getString("Name"), result.getDouble("X1"), result.getDouble("Y1"), result.getDouble("Z1"), result.getDouble("X2"), result.getDouble("Y2"), result.getDouble("Z2"), result.getDouble("StartX"), result.getDouble("StartY"), result.getDouble("StartZ"), result.getDouble("ReturnX"), result.getDouble("ReturnY"), result.getDouble("ReturnZ"), result.getString("playWorld"), result.getString("returnWorld"), result.getDouble("Sand1X"), result.getDouble("Sand1Y"), result.getDouble("Sand1Z"), result.getDouble("Sand2X"), result.getDouble("Sand2Y"), result.getDouble("Sand2Z"), result.getDouble("Wood1X"), result.getDouble("Wood1Y"), result.getDouble("Wood1Z"), result.getDouble("Wood2X"), result.getDouble("Wood2Y"), result.getDouble("Wood2Z"));
				count++;
			}
			if (count > 0)
			{
				SandFall.gameController.arenasLoaded();
				SandFall.log.log(Level.INFO, "[SandFall] Loaded " + count + " arena(s)");
			}
			
			 conn.commit();
             ps.close();
		}
		catch (SQLException e)
		{
			SandFall.log.log(Level.WARNING, "[SandFall] Encountered an issue loading arenas: " + e.getMessage());
		}
    }
    
    public void loadSigns()
    {
    	try
		{

	    	Connection conn;
			PreparedStatement ps = null;
			ResultSet result = null;
			conn = getConnection();
			ps = conn.prepareStatement("SELECT `X`, `Y`, `Z`, `World`, `Arena` FROM `sandfall_signs`");
			result = ps.executeQuery();
			
			int count = 0;
			List <Location> signsList = new ArrayList<Location>();
			
			while (result.next())
			{
				Location tmp = new Location(SandFall.instance.getServer().getWorld(result.getString("World")), result.getDouble("X"), result.getDouble("Y"), result.getDouble("Z"));
				Block block;
				Sign sign;
				
				try
				{
					block = tmp.getBlock();
					sign = (Sign) block.getState();
					Arena arena = SandFall.gameController.getArena(result.getString("Arena"));
					arena.addSign(sign);
					count++;
				}
				catch (ClassCastException e)
				{
					removeSign(result.getString("World"), result.getDouble("X"), result.getDouble("Y"), result.getDouble("Z"));
					SandFall.log.log(Level.INFO, "[SandFall] Error loading sign for arena - " + result.getString("Arena") + " : Error: Block not a sign");
				}
				catch (Exception e)
				{
					removeSign(result.getString("World"), result.getDouble("X"), result.getDouble("Y"), result.getDouble("Z"));
					SandFall.log.log(Level.INFO, "[SandFall] Error loading sign for arena - " + result.getString("Arena") + " - Removed!");
				}
			}
			
			 SandFall.gameController.arenaSignRestore();
			 SandFall.log.log(Level.INFO, "[SandFall] Loaded " + count + " signs");
			 result.close();
			 conn.commit();
             ps.close();
		}
		catch (SQLException e)
		{
			SandFall.log.log(Level.WARNING, "[SandFall] Encountered an issue loading signs: " + e.getMessage());
		}
    }
    
    public void deleteArena(Arena arena)
    {
    	removeArena(arena.getName());
    }
    
    public void storeSign(String world, double X, double Y, double Z, String arena)
    {
    	try 
    	{
	    	String sql;
			Connection conn = InputOutput.getConnection();
			
			sql = "INSERT INTO sandfall_signs (`World`, `X`, `Y`, `Z`, `Arena`) VALUES (?,?,?,?,?)";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			
			
	        preparedStatement.setString(1, world);
	        preparedStatement.setDouble(2, X);
	        preparedStatement.setDouble(3, Y);
	        preparedStatement.setDouble(4, Z);
	        preparedStatement.setString(5, arena);
	        preparedStatement.executeUpdate();
	        conn.commit();
	        
	        SandFall.log.log(Level.INFO, "[SandFall] Sign stored to DB");
    	}
    	catch (SQLException e) 
		{
    		SandFall.log.log(Level.WARNING, "[SandFall] Unexpected error while storing sign in database!");
			e.printStackTrace();
	    }
    }
    
    public void removeSign(String world, double X, double Y, double Z)
    {
    	try {
			Connection conn = InputOutput.getConnection();
			PreparedStatement ps = conn.prepareStatement("DELETE FROM sandfall_signs WHERE World = ? AND X = ? AND Y = ? AND Z = ?");
			ps.setString(1, world);
			ps.setDouble(2, X);
			ps.setDouble(3, Y);
			ps.setDouble(4, Z);
			ps.executeUpdate();
			conn.commit();
			
			ps.close();
		} catch (SQLException e) {
			SandFall.log.log(Level.WARNING,"[SandFall] Error while removing sign from database - " + e.getMessage() );
			
			e.printStackTrace();
		}
    }
    
    private void removeArena(String name)
    {
    	try {
			Connection conn = InputOutput.getConnection();
			PreparedStatement ps = conn.prepareStatement("DELETE FROM sandfall_arena WHERE Name = ?");
			ps.setString(1, name);
			ps.executeUpdate();
			conn.commit();
			
			ps.close();
		} catch (SQLException e) {
			SandFall.log.log(Level.WARNING,"[SandFall] Error while removing arena " + name + " - " + e.getMessage() );
			
			e.printStackTrace();
		}
    }
   
    public void storeArena(String pname, double pX1, double pY1, double pZ1, double pX2, double pY2, double pZ2, double pteleX, double pteleY, double pteleZ, double pfreeX, double pfreeY, double pfreeZ, String pteleWorld, String pfreeWorld, double ps1X, double ps1Y, double ps1Z, double ps2X, double ps2Y, double ps2Z, double pw1X, double pw1Y, double pw1Z, double pw2X, double pw2Y, double pw2Z)
    {
    	try 
    	{
	    	String sql;
			Connection conn = InputOutput.getConnection();
			//Implement check to see if Arena already exists, if so, delete and write over?
			/*
			if (SandFall.gameManager.arena != null)
			{
				sql = " DELETE FROM sandfall_arena";
				PreparedStatement preparedStatement = conn.prepareStatement(sql);
				preparedStatement.executeUpdate();
				SandFall.log.log(Level.INFO, "Arena written over");
			}
			*/
			
			sql = "INSERT INTO sandfall_arena (`Name`, `X1`, `Y1`, `Z1`, `X2`, `Y2`, `Z2`, `StartX`, `StartY`, `StartZ`, `ReturnX`, `ReturnY`, `ReturnZ`, `Wood1X`, `Wood1Y`, `Wood1Z`, `Wood2X`, `Wood2Y`, `Wood2Z`, `Sand1X`, `Sand1Y`, `Sand1Z`, `Sand2X`, `Sand2Y`, `Sand2Z`, `playWorld`, `returnWorld`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			
			
	        preparedStatement.setString(1, pname+"");
	        preparedStatement.setDouble(2, pX1);
	        preparedStatement.setDouble(3, pY1);
	        preparedStatement.setDouble(4, pZ1);
	        preparedStatement.setDouble(5, pX2);
	        preparedStatement.setDouble(6, pY2);
	        preparedStatement.setDouble(7, pZ2);
	        preparedStatement.setDouble(8, pteleX);
	        preparedStatement.setDouble(9, pteleY);
	        preparedStatement.setDouble(10, pteleZ);
	        preparedStatement.setDouble(11, pfreeX);
	        preparedStatement.setDouble(12, pfreeY);
	        preparedStatement.setDouble(13, pfreeZ);
	        preparedStatement.setDouble(14, pw1X);
	        preparedStatement.setDouble(15, pw1Y);
	        preparedStatement.setDouble(16, pw1Z);
	        preparedStatement.setDouble(17, pw2X);
	        preparedStatement.setDouble(18, pw2Y);
	        preparedStatement.setDouble(19, pw2Z);
	        preparedStatement.setDouble(20, ps1X);
	        preparedStatement.setDouble(21, ps1Y);
	        preparedStatement.setDouble(22, ps1Z);
	        preparedStatement.setDouble(23, ps2X);
	        preparedStatement.setDouble(24, ps2Y);
	        preparedStatement.setDouble(25, ps2Z);
	        preparedStatement.setString(26, pteleWorld);
	        preparedStatement.setString(27, pfreeWorld);
	        
	        preparedStatement.executeUpdate();
	        conn.commit();
	        
	        //SandFall.log.log(Level.INFO, "[SandFall] Arena stored to DB");
    	}
    	catch (SQLException e) 
		{
    		SandFall.log.log(Level.WARNING, "[SandFall] Unexpected error while storing arena in database!");
			e.printStackTrace();
	    }
    }
    
    public void updateArena()
    {
    	//
    }
    
    
    
    
    
    
    
    
    
    
    
}
