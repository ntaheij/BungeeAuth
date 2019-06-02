package nl.cachecraft.BungeeAuth.events;

import jline.internal.Log;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import nl.cachecraft.BungeeAuth.Main;
import nl.cachecraft.BungeeAuth.events.Check.CheckMain;
import nl.cachecraft.BungeeAuth.events.Check.TFA;

public class LogInOutEvent implements Listener {

	@EventHandler
	  public void onPostLogin(PostLoginEvent e) {
		  ProxiedPlayer p = e.getPlayer();
		  if (CheckMain.dev())
		  {
			Log.info(Main.prefix + p.getName() + " has connected.");
		  }
		  if (Main.cg.getBoolean("use-player-list"))
		  {
			  if (Main.cg.getStringList("players").contains(p.getName())) 
			  {
				  Log.info(Main.prefix + "Using list to see if player needs 2fa.");
				  TFA.auth(p);
			  }
		  }
		  else {
			  if (p.hasPermission(Main.cg.getString("permission")))
			  {
				  Log.info(Main.prefix + "Using permissions to see if player needs 2fa.");
				  TFA.auth(p);
			  }
		  }
	  }
	  
	  @EventHandler
	  public void onLeave(PlayerDisconnectEvent e)	  
	  {
		  ProxiedPlayer p = e.getPlayer();
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
