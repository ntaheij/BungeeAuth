package nl.cachecraft.BungeeAuth.events;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

import jline.internal.Log;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;
import nl.cachecraft.BungeeAuth.Main;

public class LogInOutEvent implements Listener {
	
	String prefix = Main.prefix;

	  @SuppressWarnings("deprecation")
	@EventHandler
	  public void onPostLogin(PostLoginEvent e) {
		  ProxiedPlayer p = e.getPlayer();
		  if (Check.dev())
		  {
			Log.info(prefix + p.getName() + " has connected.");
		  }
		  if (Main.cg.getStringList("players").contains(p.getName())) 
		  {
			  if (Check.dev())
			  {
				Log.info(prefix + p.getName() + " needs to have 2FA enabled.");
			  }
			  if(!(Main.cg.contains("authcodes." + p.getUniqueId()))) 
			  {
				  if (Check.dev())
				  {
					Log.info(prefix + p.getName() + " doesn't have 2FA enabled yet.");
				  }
				  GoogleAuthenticator gAuth = new GoogleAuthenticator();
				  GoogleAuthenticatorKey key = gAuth.createCredentials();
				  String secret = key.getKey();
				  String link = "https://noahtaheij.nl/auth/api/qr/generator.html?secret=" + secret;
				  String auth_name = Main.cg.getString("auth-name");
				  if (auth_name == null || auth_name.length() < 2)
				  {
					auth_name = "BungeeAuth";  
				  }
				  if (Main.cg.getBoolean("use-playername")) 
				  {
					  link = "https://noahtaheij.nl/auth/api/qr/generator.html?secret=" + secret + "&label=" + auth_name + "&issuer=" + p.getName();  
				  }
				  else 
				  {
					  link = "https://noahtaheij.nl/auth/api/qr/generator.html?secret=" + secret + "&label=" + auth_name;
				  }
				  if (Check.dev())
				  {
					Log.info(prefix + "Generating secret..");
					Log.info(prefix + "Generating link..");
					Log.info(prefix + "Secret generated [" + secret + "]");
					Log.info(prefix + "Link generated [" + secret + "]");
					Log.info(prefix + "Link contents:");
					Log.info(prefix + "- Secret: [" + secret + "]");
					Log.info(prefix + "- Server name: [" + auth_name + "]");
					Log.info(prefix + "- Using Playername: [" + Main.cg.getBoolean("use-playername") + "]");
				  }
				  
				  p.sendMessage(prefix + "§7Your §aGoogleAuth Code §7is §a" + secret);
				  p.sendMessage("§7You must enter this code in the Google Authenticator App before leaving the server.");
				  p.sendMessage("§7Alternatively, you can use the QR-code below.");
				  p.sendMessage("§a" + link);
				  if (Check.dev())
				  {
					Log.info(prefix + "Saving secret..");
				  }
				  Main.cg.set("authcodes." + p.getUniqueId(), secret);
				  Main.cg.get("authcodes." + p.getUniqueId());
				  if (Check.dev())
				  {
					Log.info(prefix + "Secret saved..");
				  }
				  try 
				  {
					  if (Check.dev())
					  {
						Log.info(prefix + "Saving config..");
					  }
					ConfigurationProvider.getProvider(YamlConfiguration.class).save(Main.cg, new File(Main.plugin.getDataFolder(), "config.yml"));
					  if (Check.dev())
					  {
						Log.info(prefix + "Config saved..");
					  }
				  } 
				  catch (IOException ex) 
				  {
					  if (Check.dev())
					  {
						Log.info(prefix + "Saving secret.. ERROR!");
					  }
					ex.getStackTrace();
				  }
			  }
			  else
			  {
				  if (Check.dev())
				  {
					Log.info(prefix + p.getName() + " has 2FA enabled.");
					Log.info(prefix + p.getName() + " is being locked for 2FA.");
				  }
				  Main.authlocked.add(p.getUniqueId());
				  p.sendMessage(prefix + "§cPlease open your Google Authenticator App and enter your six digit auth code.");
			  }
		  }
	  }
	  
	  @EventHandler
	  public void onLeave(PlayerDisconnectEvent e)	  
	  {
		  ProxiedPlayer p = e.getPlayer();
		  if (Main.authlocked.contains(p))
		  {
			  if (Check.dev())
			  {
				Log.info(prefix + "Player left during Authentication, removing from map..");
			  }
			  Main.authlocked.remove(p.getUniqueId());
		  }
	  }
	  
}
