package nl.cachecraft.BungeeAuth.Util;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import jline.internal.Log;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import nl.cachecraft.BungeeAuth.Main;
import nl.cachecraft.BungeeAuth.events.Check.CheckMain;
 

public class LoginMgr implements Listener {
	
	public static boolean checkLastToCurrent(ProxiedPlayer p)
	{
		UUID u = p.getUniqueId();
		String ipcurrent = p.getAddress().getHostName().toString();
		String ipconfig = Main.ps.getString("last-ip." + u);
		long timecurrent = System.currentTimeMillis();
		long timeconfig = Main.ps.getLong("last-logout." + u);
		int relogtimeconfig = Main.cg.getInt("playerstorage.time");
		
		
		if(Main.cg.getString("playerstorage.use").equalsIgnoreCase("login"))
		{
			timeconfig = Main.ps.getLong("last-login" + u);
		}
		boolean v1 = Main.cg.getBoolean("playerstorage.auto-2fa");
		boolean v2 = Main.cg.getBoolean("playerstorage.track-ips");
		boolean bool = false;
		if (v1 && v2)
		{
			if ((timecurrent - timeconfig) < relogtimeconfig * 3600000)
			{
				if(ipcurrent.equalsIgnoreCase(ipconfig));
				{
					bool = true;
				}
			}
		}
		return bool;	
	}
	public static void lastLogin(ProxiedPlayer p)
	{
		UUID u = p.getUniqueId();
		String ip = p.getAddress().getHostName().toString();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String dates = dateFormat.format(date).toString();
		
		if(Main.cg.getBoolean("playerstorage.track-ips"))
		{
			setIP(u, ip);
			addIPList(u, ip, dates);
		}
		setLastLogin(u);
		saveStorage();
	}
	public static void lastLogout(ProxiedPlayer p)
	{
		UUID u = p.getUniqueId();
		setLastLogout(u);
		saveStorage();
	}
	
	public static void setIP(UUID u, String ip)
	{
		Main.ps.set("last-ip." + u, ip);
	}
	public static void addIPList(UUID u, String ip, String d)
	{
		List<String> sl = Main.ps.getStringList("ip-list." + u);
		sl.add(ip + " (" + d + ")");
		Main.ps.set("ip-list." + u, sl);
	}
	public static void setLastLogin(UUID u)
	{
		long cms = System.currentTimeMillis();
		Main.ps.set("last-login." + u, cms);
	}
	public static void setLastLogout(UUID u)
	{
		long cms = System.currentTimeMillis();
		Main.ps.set("last-logout." + u, cms);
	}
	
	public static void saveStorage()
	{
		try 
		  {
			  if (CheckMain.dev())
			  {
				Log.info(Main.prefix + "Saving playerstorage..");
			  }
			  Main.cp.save(Main.ps, new File(Main.plugin.getDataFolder(), "playerstorage.yml"));
			  if (CheckMain.dev())
			  {
				Log.info(Main.prefix + "Playerstorage saved..");
			  }
		  } 
		  catch (IOException ex) 
		  {
			  if (CheckMain.dev())
			  {
				Log.info(Main.prefix + "Saving playerstorage.. ERROR!");
			  }
			ex.getStackTrace();
		  }
	}

}
