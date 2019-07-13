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
import nl.cachecraft.BungeeAuth.Connections.VerCheck;
import nl.cachecraft.BungeeAuth.Enums.DebugType;
import nl.cachecraft.BungeeAuth.events.BChatEvent;
import nl.cachecraft.BungeeAuth.events.BServerSwitchEvent;
import nl.cachecraft.BungeeAuth.events.LogInOutEvent;
import nl.cachecraft.BungeeAuth.events.Check.CheckMain;

public class Main extends Plugin implements Listener
{
	public static ArrayList<UUID> authlocked;
	public static HashMap<UUID, Integer> tries;
	public static int max_tries = 3;
	public static Configuration cg, ac, mc, ps;
	public static ConfigurationProvider cp;
	public static Main plugin;
	public static String prefix = "§6BungeeAuth>> §9";
	public static Enum<DebugType> debugtype;
	public static HashMap<UUID, String> registering;
	public static String ver = "§cnone";
	public static boolean uptodate;
	public static String latestver;
 	
	  public void onEnable()
	  {
		  ProxyServer.getInstance().getPluginManager().registerListener(this, this);
		  ProxyServer.getInstance().getPluginManager().registerListener(this, new LogInOutEvent());
		  ProxyServer.getInstance().getPluginManager().registerListener(this, new BChatEvent());
		  ProxyServer.getInstance().getPluginManager().registerListener(this, new BServerSwitchEvent());
		  ProxyServer.getInstance().getPluginManager().registerCommand(this, new CommandClass());
		  authlocked = new ArrayList<UUID>();
		  tries = new HashMap<UUID, Integer>();
		  registering = new HashMap<UUID, String>();
		  if (CheckMain.dev())
		  {
			Log.info(prefix + "Created listeners/maps.");
		  }
		  
		  plugin = this;
		  cp = ConfigurationProvider.getProvider(YamlConfiguration.class);
		  createFiles();
		  registerConfigs();
		  if (CheckMain.dev())
		  {
			Log.info(prefix + "Loaded configs.");
		  }		  
		  setVars();  
		  checkVersion();
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
	    if (!getDataFolder().exists()) 
	    {
	      getDataFolder().mkdir();
	    }
	    File file = new File(getDataFolder(), "config.yml");
	    if (!file.exists()) 
	    {
	      try
	      {
			  if (CheckMain.dev() || CheckMain.normal())
			  {
				  Log.info(prefix + "config.yml doesn't exist. Creating new one..");
			  }
	          InputStream in = getResourceAsStream("config.yml");
	          Files.copy(in, file.toPath(), new CopyOption[0]);
	      }
	      catch (IOException e)
	      {
	        e.printStackTrace();
	      }
	    }
	    File file_auths = new File(getDataFolder(), "authcodes.yml");
	    if (!file_auths.exists()) 
	    {
	      try
	      {
			  if (CheckMain.dev() || CheckMain.normal())
			  {
				  Log.info(prefix + "authcodes.yml doesn't exist. Creating new one..");
			  }
	          InputStream in = getResourceAsStream("authcodes.yml");
	          Files.copy(in, file_auths.toPath(), new CopyOption[0]);
	      }
	      catch (IOException e)
	      {
	        e.printStackTrace();
	      }
	    }
	    File file_msgs = new File(getDataFolder(), "messages.yml");
	    if (!file_msgs.exists()) 
	    {
	      try
	      {
			  if (CheckMain.dev() || CheckMain.normal())
			  {
				  Log.info(prefix + "messages.yml doesn't exist. Creating new one..");
			  }
	          InputStream in = getResourceAsStream("messages.yml");
	          Files.copy(in, file_msgs.toPath(), new CopyOption[0]);
	      }
	      catch (IOException e)
	      {
	        e.printStackTrace();
	      }
	    }
	    File file_playerstorage = new File(getDataFolder(), "playerstorage.yml");
	    if (!file_playerstorage.exists()) 
	    {
	      try
	      {
			  if (CheckMain.dev() || CheckMain.normal())
			  {
				  Log.info(prefix + "playerstorage.yml doesn't exist. Creating new one..");
			  }
	          InputStream in = getResourceAsStream("playerstorage.yml");
	          Files.copy(in, file_playerstorage.toPath(), new CopyOption[0]);
	      }
	      catch (IOException e)
	      {
	        e.printStackTrace();
	      }
	    }
	  }
	
	public static void registerConfigs()
	{
		try
		{
			cg = cp.load(new File(plugin.getDataFolder(), "config.yml"));
			ac = cp.load(new File(plugin.getDataFolder(), "authcodes.yml"));
			mc = cp.load(new File(plugin.getDataFolder(), "messages.yml"));
			ps = cp.load(new File(plugin.getDataFolder(), "playerstorage.yml"));
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void checkVersion()
	{
		//Vercheck working
		String s = VerCheck.verCheck("https://cachecraft.nl/plugins/bungeeauth/version.txt");
		latestver = s;
		if(ver.equalsIgnoreCase(s))
		{
			Log.info(prefix + "Version " + ver + " is up-to-date!");
			uptodate = true;
		}
		else 
		{
			Log.info(prefix + "Version " + ver + " is not up-to-date! Newest version: " + s);
			uptodate = false;
		}
	}
	
	public static void setVars()
	{
		  prefix = mc.getString("prefix").replace("&", "§") + " ";
		  //No double spaces at the end
		  if(prefix.substring(prefix.length() - 2).equalsIgnoreCase("  "))
		  {
			  prefix = mc.getString("prefix").replace("&", "§");
		  }
		  max_tries = cg.getInt("max-tries");
		  ver = cg.getString("version");
		  debugtype = DebugType.OFF;
	}
	
	public static String getPlayerFromString(String s)
	{
		String r = ProxyServer.getInstance().getPlayer(s).getName();
		return r;
	}
}
