package nl.cachecraft.BungeeAuth.events.Check;

import com.warrenstrange.googleauth.GoogleAuthenticator;

import jline.internal.Log;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import nl.cachecraft.BungeeAuth.Main;
import nl.cachecraft.BungeeAuth.Enums.DebugType;
 
public class CheckMain implements Listener {
//	
	public static boolean playerInputCode(ProxiedPlayer p, int code)
	{
		String secretkey = Main.ac.getString("authcodes." + p.getUniqueId());
		if (Main.registering.containsKey(p.getUniqueId()))
		{
			secretkey = Main.registering.get(p.getUniqueId());
		}
		GoogleAuthenticator gAuth = new GoogleAuthenticator();
		gAuth.getTotpPassword(secretkey);
		boolean codeisvalid = gAuth.authorize(secretkey, code);
		if (CheckMain.dev())
		{
			Log.info(Main.prefix + "Checking if code is valid..");
		}
		if (codeisvalid)
		{
			Main.authlocked.remove(p.getUniqueId());
			return codeisvalid;
		}
		  
		return codeisvalid;
	}
	
	public static void debugType()
	{
		  if (Main.cg.getString("debug").equalsIgnoreCase("normal"))
		  {
			  Main.debugtype = DebugType.NORMAL;
		  }
		  else if (Main.cg.getString("debug").equalsIgnoreCase("dev") || Main.cg.getString("debug").equalsIgnoreCase("developer"))
		  {
			  Main.debugtype = DebugType.DEV;
		  }
		  else
		  {
			  Main.debugtype = DebugType.OFF;  
		  }
		  if (normal())
		  {
			  Log.info(Main.prefix + "Detected debugtype: NORMAL");
		  }
		  if (dev())
		  {
			Log.info(Main.prefix + "Detected debugtype: DEVELOPER");
		  }
		  if (off())
		  {
			  Log.info(Main.prefix + "Detected debugtype: OFF");
		  }
		  
		  if (normal())
		  {
			Log.info(Main.prefix + "Plugin has enabled succesfully.");  
		  }
		  
		  if (dev())
		  {
			Log.info(Main.prefix + "Plugin has enabled succesfully.");
		  }
	}
	
	public static boolean auth() {
		return true;
	}
	  
	public static boolean dev()
	{
		if (Main.debugtype == DebugType.DEV)
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
	public static boolean normal()
	{
		if (Main.debugtype == DebugType.NORMAL)
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
	public static boolean off()
	{
		if (Main.debugtype == DebugType.OFF)
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
}
