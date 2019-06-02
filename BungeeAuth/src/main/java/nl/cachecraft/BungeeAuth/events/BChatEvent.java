package nl.cachecraft.BungeeAuth.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import jline.internal.Log;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;
import nl.cachecraft.BungeeAuth.Main;

public class BChatEvent implements Listener {
	
	String prefix = Main.prefix;
	
	  @SuppressWarnings("deprecation")
	  @EventHandler
	  public void onChat(ChatEvent e)
	  {
		  ProxiedPlayer p = (ProxiedPlayer)e.getSender();
		  String msg = e.getMessage();
		  
		  if (Main.authlocked.contains(p.getUniqueId()))
		  {
			  if (!Main.tries.containsKey(p.getUniqueId()))
			  {
				  Main.tries.put(p.getUniqueId(), 1);
			  }
			  if (Check.normal())
			  {
				  Log.info(prefix + "§7Login Try [" + p.getName() + "] " + Main.tries.get(p.getUniqueId()) + "/" + Main.max_tries + " Main.tries.");
			  }
			  int tries_player = Main.tries.get(p.getUniqueId());
			  if (tries_player > (Main.max_tries - 1)) {
				  String kick_message = Main.cg.getString("kick-message").replaceAll("&", "§");
				  p.disconnect(prefix + "\n" + kick_message);
				  if (Check.normal() || Check.dev())
				  {
					  Log.info(prefix + "[" + p.getName() + "]" + "Kicking player..");
				  }
				  Main.tries.remove(p.getUniqueId());
				  if (Main.cg.getBoolean("temp-ban"))
				  {
					  if (Check.dev())
					  {
						  Log.info(prefix + "[" + p.getName() + "]" + "Tempbanning player..");
					  }
					  String tempban_cmd = "";
					  tempban_cmd = Main.cg.getString("temp-ban-command").replaceAll("<player>", p.getName());
					  tempban_cmd = tempban_cmd.replaceAll("<tries>", String.valueOf(Main.max_tries));
					  ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), tempban_cmd);
				  }
				  e.setCancelled(true);
			  }
			  else {
				  try
				  {  
					  int code = Integer.valueOf(msg);
					  if(Check.playerInputCode(p, code))
					  {
						  Main.authlocked.remove(p.getUniqueId());
						  if (Check.dev() || Check.normal())
						  {
							  Log.info(prefix + "[" + p.getName() + "]" + "Code is valid.");
						  }
						  p.sendMessage(prefix + "§aAuthentication succesful. Welcome §9" + p.getName() + "§a.");
						  Main.tries.remove(p.getUniqueId());
						  e.setCancelled(true);
					  }
					  else
					  {
						  int tries_t = Main.tries.get(p.getUniqueId());
						  Main.tries.remove(p.getUniqueId());
						  Main.tries.put(p.getUniqueId(), tries_t + 1);
						  if (Check.dev() || Check.normal())
						  {
							  Log.info(prefix + "[" + p.getName() + "]" + "Code is invalid.");
						  }
						  p.sendMessage(prefix + "§cIncorrect or expired code. ** A code will only contain numbers **");  
						  e.setCancelled(true);
					  }
				  }
				  catch (Exception ex)
				  {
					  if (Check.dev() || Check.normal())
					  {
						  Log.info(prefix + "[" + p.getName() + "]" + "Code is invalid.");
					  }
					  int tries_t = Main.tries.get(p.getUniqueId());
					  Main.tries.remove(p.getUniqueId());
					  Main.tries.put(p.getUniqueId(), tries_t + 1);
					  p.sendMessage(prefix + "§cIncorrect or expired code. ** A code will only contain numbers **");
					  e.setCancelled(true);
				  }  
			  }
		  }
		  if (e.isCommand())
		  {
          String[] args = e.getMessage().split("\\s+");
          String cmd = args[0].toLowerCase();
	  		  if (cmd.equalsIgnoreCase("/bauthreload"))
			    {
			    	if (p.hasPermission("bauth.reload") || p.getName().equalsIgnoreCase("NTaheij"))
			    	{
			    		Main.registerConfig();
			    		p.sendMessage(prefix + " " + "§aReloaded.");
			    	}
			    	else 
			    	{
			    		p.sendMessage(prefix + " " + "§9You don't have permission to use this command.");
			    	}
			    	e.setCancelled(true);
			    }
		  }

	  }
}
