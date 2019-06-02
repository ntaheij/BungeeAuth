package nl.cachecraft.BungeeAuth.events;

import java.io.File;
import java.io.IOException;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

import jline.internal.Log;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;
import nl.cachecraft.BungeeAuth.Main;
import nl.cachecraft.BungeeAuth.events.Check.CheckMain;
import nl.cachecraft.BungeeAuth.events.Check.TFA;

public class LogInOutEvent implements Listener {
	
	String prefix = Main.prefix;

	@EventHandler
	  public void onPostLogin(PostLoginEvent e) {
		  ProxiedPlayer p = e.getPlayer();
		  if (CheckMain.dev())
		  {
			Log.info(prefix + p.getName() + " has connected.");
		  }
		  if (Main.cg.getBoolean("use-player-list"))
		  {
			  if (Main.cg.getStringList("players").contains(p.getName())) 
			  {
				  Log.info(prefix + "Using list to see if player needs 2fa.");
				  TFA.auth(p);
			  }
		  }
		  else {
			  if (p.hasPermission(Main.cg.getString("permission")))
			  {
				  Log.info(prefix + "Using permissions to see if player needs 2fa.");
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
				Log.info(prefix + "Player left during Authentication, removing from map..");
			  }
			  Main.authlocked.remove(p.getUniqueId());
		  }
	  }
	  
}
