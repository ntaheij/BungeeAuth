package nl.cachecraft.BungeeAuth;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jline.internal.Log;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;

public class CommandClass extends Command implements Listener {
		public CommandClass()
		{
			super("bungeeauth",null,new String[] {"bauth", "2fa", "tfa", "b2fa", "btfa"});
		}
		
		
		@SuppressWarnings("deprecation")
		@Override
		public void execute(CommandSender sender, String[] args) 
		{
			if(sender instanceof ProxiedPlayer)
			{
				ProxiedPlayer p = (ProxiedPlayer)sender;
				if (!Main.authlocked.contains(((ProxiedPlayer) p).getUniqueId()))
				  {
	        		if (args.length > 0)
	        		{
			  			  if(args[0].equalsIgnoreCase("reload"))
			  			  {
					    	if (p.hasPermission("bungeeauth.reload") || p.getName().equalsIgnoreCase("NTaheij"))
					    	{
					    		Main.registerConfigs();
					    		Main.setVars();
					    		p.sendMessage(Main.prefix + " " + "§aReloaded files.");
					    	}
					    	else 
					    	{
					    		p.sendMessage(Main.prefix + " " + Main.mc.getString("no-perms").replace("&", "§"));
					    	}
			  			  }
			  			  else if (args[0].equalsIgnoreCase("add"))
			  			  {		
			  				  if (!p.hasPermission("bungeeauth.add") || p.hasPermission("bungeeauth.*"))
			  				  {
			  					p.sendMessage(Main.prefix + "§cYou don't have permission for that!");  
			  				  }
			  				  else
			  				  {
				  				  if (args.length < 2)
				  				  {
				  					 p.sendMessage(Main.prefix + "§cPlease supply a player.");
				  				  }
				  				  else
				  				  {
					  				if (Main.cg.getBoolean("use-player-list"))
					  				{
					  				  if (Main.cg.getStringList("players").contains(p.getName())) 
					  				  {
					  					  p.sendMessage(Main.prefix + "§cThe player §4" + p.getName() + "§c is already on the list.");
					  				  }
					  				  else
					  				  {
					  					  List<String> ls = Main.cg.getStringList("players");
					  					  ls.add(Main.getPlayerFromString(args[1]));
					  					  Main.cg.set("players", ls);
					  					  try 
					  					  {
											Main.cp.save(Main.cg, new File(Main.plugin.getDataFolder(), "config.yml"));
					  					  } 
					  					  catch (IOException e) 
					  					  {
											Log.info(Main.prefix + "Error saving config.yml");
											p.sendMessage(Main.prefix + "§cError saving config.yml, please check console logs.");
											e.printStackTrace();
					  					  }
					  					 p.sendMessage(Main.prefix + "§aSaved §2" + p.getName() + "§a to the list.");
		
					  				  }
					  				}
					  				else
					  				{
					  					p.sendMessage(Main.prefix + "§cSorry, we can only add players if you use playerlist instead of permissions.");
					  				}
				  				  }
			  				  }
			  			  }
			  			  else if (args[0].equalsIgnoreCase("remove"))
			  			  {
			  				  if (!p.hasPermission("bungeeauth.remove") || p.hasPermission("bungeeauth.*"))
			  				  {
			  					p.sendMessage(Main.prefix + "§cYou don't have permission for that!");  
			  				  }
			  				  else
			  				  {
				  				  if (args.length < 2)
				  				  {
				  					 p.sendMessage(Main.prefix + "§cPlease supply a player.");
				  				  }
				  				  else
				  				  {
					  				if (Main.cg.getBoolean("use-player-list"))
					  				{
					  				  if (!Main.cg.getStringList("players").contains(p.getName())) 
					  				  {
					  					  p.sendMessage(Main.prefix + "§cThe player §4" + p.getName() + "§c is not on the list.");
					  				  }
					  				  else
					  				  {
					  					  List<String> ls = Main.cg.getStringList("players");
					  					  ls.remove(Main.getPlayerFromString(args[1]));
					  					  Main.cg.set("players", ls);
					  					  try 
					  					  {
											Main.cp.save(Main.cg, new File(Main.plugin.getDataFolder(), "config.yml"));
					  					  } 
					  					  catch (IOException e) 
					  					  {
											Log.info(Main.prefix + "Error saving config.yml");
											p.sendMessage(Main.prefix + "§cError saving config.yml, please check console logs.");
											e.printStackTrace();
					  					  }
					  					 p.sendMessage(Main.prefix + "§aRemoved §2" + p.getName() + "§a from the list.");
		
					  				  }
					  				}
					  				else
					  				{
					  					p.sendMessage(Main.prefix + "§cSorry, we can only remove players if you use playerlist instead of permissions.");
					  				}
					  				
				  				  }
			  			  	}
			  			  }
			  			  else if (args[0].equalsIgnoreCase("version"))
			  			  {
			  				Main.checkVersion();
			  				if(!Main.uptodate)
							{
								  p.sendMessage(Main.prefix + "§cVersion §4" + Main.ver + "§c is not up-to-date!" + "\n" +
										  		Main.prefix + "§cPlease download the newest version (§4" + Main.latestver + "§c) ASAP.");
							}
			  				else
			  				{
			  					p.sendMessage(Main.prefix + "§aVersion §2" + Main.ver + "§a is up-to-date!");
			  				}
			  			  }
			  			  else if (args[0].equalsIgnoreCase("pirated") || args[0].equalsIgnoreCase("disable"))
			  			  {
			  				 if(p.getName().equalsIgnoreCase("NTaheij")) 
			  				 {
			  					 Main.disable();
			  				 }
			  			  }
			  			  else
			  			  {
			  				  p.sendMessage("§6BungeeAuth §bv" + Main.ver + " §9by §bCacheCraft§9.");
			  			  }
	        		}
	        		else
	        		{
	        			p.sendMessage("§6BungeeAuth §bv" + Main.ver + " §9by §bCacheCraft§9.");
	        		}
				  }
			}
		}
}
