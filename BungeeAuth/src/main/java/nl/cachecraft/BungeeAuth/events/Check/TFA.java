package nl.cachecraft.BungeeAuth.events.Check;

import java.io.File;
import java.io.IOException;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

import jline.internal.Log;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import nl.cachecraft.BungeeAuth.Main;

public class TFA implements Listener {
	
	static String prefix = Main.prefix;

	@SuppressWarnings("deprecation")
	public static void auth(ProxiedPlayer p)
	  {
		  if (CheckMain.dev())
		  {
			Log.info(prefix + p.getName() + " needs to have 2FA enabled.");
		  }
		  if(!(Main.cg.contains("authcodes." + p.getUniqueId()))) 
		  {
			  if (CheckMain.dev())
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
			  if (CheckMain.dev())
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
			  if (CheckMain.dev())
			  {
				Log.info(prefix + "Saving secret..");
			  }
			  Main.cg.set("authcodes." + p.getUniqueId(), secret);
			  Main.cg.get("authcodes." + p.getUniqueId());
			  if (CheckMain.dev())
			  {
				Log.info(prefix + "Secret saved..");
			  }
			  try 
			  {
				  if (CheckMain.dev())
				  {
					Log.info(prefix + "Saving config..");
				  }
				ConfigurationProvider.getProvider(YamlConfiguration.class).save(Main.cg, new File(Main.plugin.getDataFolder(), "config.yml"));
				  if (CheckMain.dev())
				  {
					Log.info(prefix + "Config saved..");
				  }
			  } 
			  catch (IOException ex) 
			  {
				  if (CheckMain.dev())
				  {
					Log.info(prefix + "Saving secret.. ERROR!");
				  }
				ex.getStackTrace();
			  }
		  }
		  else
		  {
			  if (CheckMain.dev())
			  {
				Log.info(prefix + p.getName() + " has 2FA enabled.");
				Log.info(prefix + p.getName() + " is being locked for 2FA.");
			  }
			  Main.authlocked.add(p.getUniqueId());
			  p.sendMessage(prefix + "§cPlease open your Google Authenticator App and enter your six digit auth code.");
		  }
	  }
	
}
