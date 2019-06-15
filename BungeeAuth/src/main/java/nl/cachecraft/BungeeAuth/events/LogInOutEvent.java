package nl.cachecraft.BungeeAuth.events;

import jline.internal.Log;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import nl.cachecraft.BungeeAuth.Main;
import nl.cachecraft.BungeeAuth.Util.LoginMgr;
import nl.cachecraft.BungeeAuth.events.Check.CheckMain;
import nl.cachecraft.BungeeAuth.events.Check.TFA;

public class LogInOutEvent implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	  public void onPostLogin(PostLoginEvent e) {
		  ProxiedPlayer p = e.getPlayer();
		  if (CheckMain.dev())
		  {
			Log.info(Main.prefix + p.getName() + " has connected.");
		  }
		  
		  if(LoginMgr.checkLastToCurrent(p)) {
			  p.sendMessage(Main.prefix + Main.mc.getString("auth.autologin").replace("<player>", p.getName()).replace("&", "§"));;
			  p.sendMessage(Main.prefix + Main.mc.getString("auth.valid").replace("<player>", p.getName()).replace("&", "§"));
			  return;
		  }
		  
		  if (Main.cg.getBoolean("use-player-list"))
		  {
			  if (Main.cg.getStringList("players").contains(p.getName())) 
			  {
				  Log.info(Main.prefix + "Using list to see if player needs 2fa.");
				  TFA.auth(p);
				  LoginMgr.lastLogin(p);
			  }
		  }
		  else {
			  if (p.hasPermission(Main.cg.getString("permission")))
			  {
				  Log.info(Main.prefix + "Using permissions to see if player needs 2fa.");
				  TFA.auth(p);
				  LoginMgr.lastLogin(p);
			  }
		  }
	  }
	  
	  @EventHandler
	  public void onLeave(PlayerDisconnectEvent e)	  
	  {
		  ProxiedPlayer p = e.getPlayer();
		  if (Main.cg.getBoolean("use-player-list"))
		  {
			  if (Main.cg.getStringList("players").contains(p.getName())) 
			  {
				  LoginMgr.lastLogout(p);
			  }
		  }
		  else {
			  if (p.hasPermission(Main.cg.getString("permission")))
			  {
				  LoginMgr.lastLogout(p);
			  }
		  }
		  if (Main.authlocked.contains(p.getUniqueId()))
		  {
			  if (CheckMain.dev())
			  {
				Log.info(Main.prefix + "Player left during Authentication, removing from map..");
			  }
			  Main.authlocked.remove(p.getUniqueId());
		  }
		  if (Main.registering.containsKey(p.getUniqueId()))
		  {
			  if (CheckMain.dev())
			  {
				Log.info(Main.prefix + "Player left during Registration, removing from map..");
			  }
			  Main.registering.remove(p.getUniqueId());
		  }
	  }
	  
}
