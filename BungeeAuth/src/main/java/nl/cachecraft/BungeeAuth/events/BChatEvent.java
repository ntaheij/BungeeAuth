package nl.cachecraft.BungeeAuth.events;
 
import java.io.File;
import java.io.IOException;

import jline.internal.Log;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import nl.cachecraft.BungeeAuth.Main;
import nl.cachecraft.BungeeAuth.events.Check.CheckMain;

public class BChatEvent implements Listener 
{
	
	  @SuppressWarnings("deprecation")
	  @EventHandler
	  public void onChat(ChatEvent e)
	  {
		  ProxiedPlayer p = (ProxiedPlayer)e.getSender();
		  String msg = e.getMessage().replaceAll(" ", "");
		  if (Main.registering.containsKey(p.getUniqueId()))
		  {
				  try
				  {  
					  int code = Integer.valueOf(msg);
					  Log.info("code: " + code);
					  Log.info("secret: " + code);
					  if(CheckMain.playerInputCode(p, code))
					  {
						  Main.authlocked.remove(p.getUniqueId());
						  if (CheckMain.dev() || CheckMain.normal())
						  {
							  Log.info(Main.prefix + "[" + p.getName() + "]" + " Code is valid.");
						  }
						  p.sendMessage(Main.prefix + Main.mc.getString("auth.valid").replace("<player>", p.getName()).replace("&", "§"));
						  if (CheckMain.dev())
						  {
							Log.info(Main.prefix + "Saving secret..");
						  }
						  	Main.ac.set("authcodes." + p.getUniqueId(), Main.registering.get(p.getUniqueId()));
						  	Main.ac.get("authcodes." + p.getUniqueId());
						  if (CheckMain.dev())
						  {
							Log.info(Main.prefix + "Secret saved..");
						  }
						  try 
						  {
							  if (CheckMain.dev())
							  {
								Log.info(Main.prefix + "Saving authcodes..");
							  }
							  Main.cp.save(Main.ac, new File(Main.plugin.getDataFolder(), "authcodes.yml"));
							  if (CheckMain.dev())
							  {
								Log.info(Main.prefix + "Authcodes saved..");
							  }
						  } 
						  catch (IOException ex) 
						  {
							  if (CheckMain.dev())
							  {
								Log.info(Main.prefix + "Saving secret.. ERROR!");
							  }
							ex.getStackTrace();
						  }
						  Main.registering.remove(p.getUniqueId());
						  e.setCancelled(true);
					  }
					  else
					  {
						  p.sendMessage(Main.prefix + Main.mc.getString("auth.invalid").replace("<player>", p.getName()).replace("&", "§"));
						  e.setCancelled(true);
					  }
				  }
				  catch (Exception ex)
				  {
					  if (CheckMain.dev() || CheckMain.normal())
					  {
						  Log.info(Main.prefix + "[" + p.getName() + "]" + " Code is invalid.");
					  }
					  p.sendMessage(Main.prefix + Main.mc.getString("auth.invalid").replace("<player>", p.getName()).replace("&", "§"));
					  e.setCancelled(true);
				  }
			  }
		  if (Main.authlocked.contains(p.getUniqueId()))
		  {
			  if (!Main.tries.containsKey(p.getUniqueId()))
			  {
				  Main.tries.put(p.getUniqueId(), 1);
			  }
			  if (CheckMain.normal())
			  {
				  Log.info(Main.prefix + "§7Login Try [" + p.getName() + "] " + Main.tries.get(p.getUniqueId()) + "/" + Main.max_tries + " tries.");
			  }
			  int tries_player = Main.tries.get(p.getUniqueId());
			  if (tries_player > (Main.max_tries - 1)) {
				  String kick_message = Main.mc.getString("auth.kick-message").replaceAll("&", "§");
				  kick_message = kick_message.replace("<tries>", String.valueOf(Main.max_tries));
				  p.disconnect(Main.prefix + "\n" + kick_message);
				  if (CheckMain.normal() || CheckMain.dev())
				  {
					  Log.info(Main.prefix + "[" + p.getName() + "]" + " Kicking player..");
				  }
				  Main.tries.remove(p.getUniqueId());
				  if (CheckMain.dev())
				  {
					  Log.info(Main.prefix + "temp-ban: " + Main.cg.getBoolean("temp-ban"));
				  }
				  if (Main.cg.getBoolean("temp-ban") == true)
				  {
					  if (CheckMain.dev())
					  {
						  Log.info(Main.prefix + "[" + p.getName() + "]" + " Tempbanning player..");
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
					  if(CheckMain.playerInputCode(p, code))
					  {
						  Main.authlocked.remove(p.getUniqueId());
						  if (CheckMain.dev() || CheckMain.normal())
						  {
							  Log.info(Main.prefix + "[" + p.getName() + "]" + " Code is valid.");
						  }
						  p.sendMessage(Main.prefix + "§aAuthentication successful. Welcome §9" + p.getName() + "§a.");
						  Main.tries.remove(p.getUniqueId());
						  e.setCancelled(true);
					  }
					  else
					  {
						  int tries_t = Main.tries.get(p.getUniqueId());
						  Main.tries.remove(p.getUniqueId());
						  Main.tries.put(p.getUniqueId(), tries_t + 1);
						  if (CheckMain.dev() || CheckMain.normal())
						  {
							  Log.info(Main.prefix + "[" + p.getName() + "]" + " Code is invalid.");
						  }
						  p.sendMessage(Main.prefix + Main.mc.getString("auth.invalid").replace("<player>", p.getName()).replace("&", "§")); 
						  e.setCancelled(true);
					  }
				  }
				  catch (Exception ex)
				  {
					  if (CheckMain.dev() || CheckMain.normal())
					  {
						  Log.info(Main.prefix + "[" + p.getName() + "]" + " Code is invalid.");
					  }
					  int tries_t = Main.tries.get(p.getUniqueId());
					  Main.tries.remove(p.getUniqueId());
					  Main.tries.put(p.getUniqueId(), tries_t + 1);
					  p.sendMessage(Main.prefix + Main.mc.getString("auth.invalid").replace("<player>", p.getName()).replace("&", "§"));
					  e.setCancelled(true);
				  }  
			  }
		  }
	  }
}
