package nl.cachecraft.BungeeAuth;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import jline.internal.Log;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import nl.cachecraft.BungeeAuth.Enums.DebugType;
import nl.cachecraft.BungeeAuth.events.BChatEvent;
import nl.cachecraft.BungeeAuth.events.LogInOutEvent;
import nl.cachecraft.BungeeAuth.events.Check.CheckMain;

public class Main extends Plugin implements Listener
{
	public static ArrayList<UUID> authlocked;
	public static HashMap<UUID, Integer> tries;
	public static int max_tries = 3;
	public static Configuration cg;
	public static Main plugin;
	public static String prefix = "";
	public static Enum<DebugType> debugtype;
	
	  public void onEnable()
	  {
		  ProxyServer.getInstance().getPluginManager().registerListener(this, this);
		  ProxyServer.getInstance().getPluginManager().registerListener(this, new CommandClass());
		  ProxyServer.getInstance().getPluginManager().registerListener(this, new LogInOutEvent());
		  ProxyServer.getInstance().getPluginManager().registerListener(this, new BChatEvent());
		  authlocked = new ArrayList<UUID>();
		  tries = new HashMap<UUID, Integer>();
		  if (CheckMain.dev())
		  {
			Log.info(prefix + "Created listeners/maps.");
		  }
		  
		  plugin = this;
		  createFiles();
		  registerConfig();
		  if (CheckMain.dev())
		  {
			Log.info(prefix + "Loaded config.");
		  }
		  
		  prefix = cg.getString("prefix").replace("&", "§") + " ";
		  max_tries = cg.getInt("max-tries");
		  debugtype = DebugType.OFF;
		  if (CheckMain.dev())
		  {
			Log.info(prefix + "Got variables from the config.");
		  }
		  
		  //Checks
		  CheckMain.debugType();
		  if (!CheckMain.auth()) {
			  Log.info(prefix + "§cYou are using a pirated version!");
			  getProxy().getPluginManager().unregisterListeners(this);
		      getProxy().getPluginManager().unregisterCommands(this); 
		  }
		  
	  }
	  
	  public void createFiles()
	  {
	    if (!getDataFolder().exists()) {
	      getDataFolder().mkdir();
	    }
	    File file = new File(getDataFolder(), "config.yml");
	    if (!file.exists()) {
	      try
	      {
			  if (CheckMain.dev() || CheckMain.normal())
			  {
				  Log.info(prefix + "Config doesn't exist. Creating new one..");
			  }
	          InputStream in = getResourceAsStream("config.yml");
	          Files.copy(in, file.toPath(), new CopyOption[0]);
	      }
	      catch (IOException e)
	      {
	        e.printStackTrace();
	      }
	    }
	  }
	
	public static void registerConfig()
	{
		try
		{
			cg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(plugin.getDataFolder(), "config.yml"));			
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
