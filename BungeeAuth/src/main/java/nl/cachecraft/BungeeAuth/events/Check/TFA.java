package nl.cachecraft.BungeeAuth.events.Check;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

import jline.internal.Log;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import nl.cachecraft.BungeeAuth.Main;

public class TFA implements Listener {

	@SuppressWarnings("deprecation")
	public static void auth(ProxiedPlayer p)
	  {
		  if (CheckMain.dev())
		  {  
			Log.info(Main.prefix + p.getName() + " needs to have 2FA enabled.");
		  }
		  if(!(Main.ac.contains("authcodes." + p.getUniqueId()))) 
		  {
			  if (CheckMain.dev())
			  {
				Log.info(Main.prefix + p.getName() + " doesn't have 2FA enabled yet.");
			  }
			  		  
			  GoogleAuthenticator gAuth = new GoogleAuthenticator();
			  GoogleAuthenticatorKey key = gAuth.createCredentials();
			  String secret = key.getKey();
			  String website = "https://cachecraftapi.z6.web.core.windows.net/generator.html?secret=";
			  String link = website + secret;
			  String auth_name = Main.mc.getString("auth.auth-name");
			  if (auth_name == null || auth_name.length() < 2)
			  {
				auth_name = "BungeeAuth";  
			  }
			  if (Main.cg.getBoolean("use-playername")) 
			  {
				  link = website + secret + "&label=" + auth_name + "&issuer=" + p.getName();  
			  }
			  else 
			  {
				  link = website + secret + "&label=" + auth_name;
			  }
			  if (CheckMain.dev())
			  {
				Log.info(Main.prefix + "Generating secret..");
				Log.info(Main.prefix + "Generating link..");
				Log.info(Main.prefix + "Secret generated [" + secret + "]");
				Log.info(Main.prefix + "Link generated [" + secret + "]");
				Log.info(Main.prefix + "Link contents:");
				Log.info(Main.prefix + "- Secret: [" + secret + "]");
				Log.info(Main.prefix + "- Server name: [" + auth_name + "]");
				Log.info(Main.prefix + "- Using Playername: [" + Main.cg.getBoolean("use-playername") + "]");
			  }
			  
			  p.sendMessage(Main.prefix + "§7You are required to register using 2FA via the Google Authenticator App.");
			  p.sendMessage("§7Your §aGoogleAuth Code §7is §a" + secret);
			  p.sendMessage("§7Alternatively, you can use the QR-code below.");
			  p.sendMessage("§a" + link);
			  p.sendMessage("§cAfter doing so type the six digit verification code.");
			  Main.registering.put(p.getUniqueId(), secret);
		  }
		  else
		  {
			  if (CheckMain.dev())
			  {
				Log.info(Main.prefix + p.getName() + " has 2FA enabled.");
				Log.info(Main.prefix + p.getName() + " is being locked for 2FA.");
			  }
			  Main.authlocked.add(p.getUniqueId());
			  p.sendMessage(Main.prefix + Main.mc.getString("auth.open-message").replace("<player>", p.getName()).replace("&", "§"));
			 
		  }
	  }
	
}
