package nl.cachecraft.BungeeAuth.events;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent.Reason;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import nl.cachecraft.BungeeAuth.Main;

public class BServerSwitchEvent implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onServerSwitch(ServerConnectEvent e)
	{
		ProxiedPlayer p = e.getPlayer();
		if (Main.authlocked.contains(p.getUniqueId()) || Main.registering.containsKey(p.getUniqueId()))
		{
			if (!(e.getReason() == Reason.JOIN_PROXY))
			{
			p.sendMessage(Main.prefix + "§cYou can't switch servers whilst authenticating!");
			e.setCancelled(true);
			//e.setTarget(p.getServer().getInfo());
			}
		}
	}
}
