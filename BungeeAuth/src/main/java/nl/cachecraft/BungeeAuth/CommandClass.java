package nl.cachecraft.BungeeAuth;

import java.util.ArrayList;
import java.util.UUID;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class CommandClass implements Listener {
	
	String prefix = Main.prefix;

	  @SuppressWarnings("deprecation")
	@EventHandler
	  public void onChat(ChatEvent e)
	  {
		  ProxiedPlayer p = (ProxiedPlayer)e.getSender();
		  String msg = e.getMessage();
		  
		  if (!Main.authlocked.contains(p.getUniqueId()))
		  {
			  if (e.isCommand())
			  {
	            String[] args = e.getMessage().split("\\s+");
	            String cmd = args[0].toLowerCase();
		  		  if (cmd.equalsIgnoreCase("/bauthreload"))
				    {
				    	if (p.hasPermission("bauth.reload") || p.getName().equalsIgnoreCase("NTaheij"))
				    	{
				    		Main.registerConfig();
				    		p.sendMessage(Main.prefix + " " + "§aReloaded.");
				    	}
				    	else 
				    	{
				    		p.sendMessage(Main.prefix + " " + "§9You don't have permission to use this command.");
				    	}
				    	e.setCancelled(true);
				    }
		  		  else if (cmd.equals("/bauthnext"))
		  		  {
		  			  
		  		  }
			  }
		  }
	  }

}
