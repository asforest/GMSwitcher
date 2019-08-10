package cn.innc11.GMSwitcher;

import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.player.PlayerToggleSneakEvent;
import cn.nukkit.plugin.PluginBase;

public class GMSwitcher extends PluginBase implements Listener 
{
	HashMap<String, Long> lastPlayerSnakeTime;
	int interval;
	
	@Override
	public void onEnable() 
	{
		lastPlayerSnakeTime = new HashMap<String, Long>();
		
		saveDefaultConfig();
		getServer().getPluginManager().registerEvents(this, this);
		
		interval = getConfig().getInt("interval");
	}
	
	@Override
	public void onDisable() 
	{
		lastPlayerSnakeTime.clear();
		lastPlayerSnakeTime = null;
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent e)
	{
		lastPlayerSnakeTime.remove(e.getPlayer().getName());
	}
	
	@EventHandler
	public void onPlayerSnakeEvent(PlayerToggleSneakEvent e)
	{
		if(e.getPlayer().isOp() && !e.getPlayer().isSneaking() && e.getPlayer().isOnGround())
		{
			Player player = e.getPlayer();
			if(lastPlayerSnakeTime.containsKey(player.getName()))
			{
				long cttime = System.currentTimeMillis();
				long lstime = lastPlayerSnakeTime.get(player.getName()).longValue();
				
				if((cttime-lstime)<interval)
				{
					if(player.getGamemode()==0)
					{
						player.setGamemode(1);
					}else if(player.getGamemode()==1)
					{
						player.setGamemode(0);
					}
					
					lastPlayerSnakeTime.put(player.getName(), Long.valueOf(0));
				}else {
					lastPlayerSnakeTime.put(player.getName(), Long.valueOf(System.currentTimeMillis()));
				}
			}else {
				lastPlayerSnakeTime.put(player.getName(), Long.valueOf(System.currentTimeMillis()));
			}
			
			
		}
	}
}