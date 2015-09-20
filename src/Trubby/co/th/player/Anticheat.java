package Trubby.co.th.player;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Anticheat implements Listener{

	/**
	 * 
	 * 		ANTI FORCEFIELD
	 * 
	 */
	HashMap<String, Integer> melee_log = new HashMap<>();
	@EventHandler
	public void onPlayerMelee(EntityDamageByEntityEvent e){
		if(e.getDamager() instanceof Player){
			final Player p = (Player) e.getDamager();
			if(p.getItemInHand().getType() == Material.YELLOW_FLOWER ||
					p.getItemInHand().getType() == Material.STICK ||
					p.getItemInHand().getType() == Material.AIR ||
					p.getItemInHand().getType() == Material.BONE){
				
				//Car
				if(p.isInsideVehicle()){
					p.sendMessage(ChatColor.DARK_RED + "Melee Attack in a car is not allowed.");
					e.setCancelled(true);
					return;
				}
				
				/*if(melee_log.containsKey(p.getName())){
					int i = melee_log.get(p.getName());
					//System.out.println(i);
					if(i > 2){
						//p.kickPlayer(ChatColor.RED + "[" + ChatColor.WHITE + "GTAAntiCheat" + ChatColor.RED + "]" + ChatColor.WHITE + " Melee attack too fast!");
						for(Player admin : Bukkit.getOnlinePlayers()){
							if(admin.isOp()){
								admin.sendMessage("[GTA Anticheat] " + p.getName() + " kick : attack too fast.");
							}
						}
					}
					e.setCancelled(true);
					melee_log.put(p.getName(), i+1);
				}else{
						melee_log.put(p.getName(), 1);
						Bukkit.getScheduler().scheduleSyncDelayedTask(GTA.instance, new Runnable() {
							
							@Override
							public void run() {
								melee_log.remove(p.getName());
							}
						}, 7L);
				}*/
			}else{
				e.setCancelled(true);
			}
		}
	}
	
	/**
	 * 
	 * 		ANTI FORCEFIELD
	 * 
	 */
	
	HashMap<String, Long> chat_delay = new HashMap<>();
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e){
		Player p = e.getPlayer();
		if(chat_delay.containsKey(p.getName())){
			long timesince = System.currentTimeMillis() - chat_delay.get(p.getName());
			if(timesince < 5000){
				e.setCancelled(true);
				int timeleft = (int) (5 - (timesince/1000));
				e.getPlayer().sendMessage(ChatColor.RED + "You can speak again in " + timeleft + " sec.");
			}else{
				chat_delay.remove(p.getName());
			}
		}else{
			chat_delay.put(p.getName(), System.currentTimeMillis());
			
		}
	}

	
	//Clear cache
	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		//melee_log.remove(e.getPlayer().getName());
		chat_delay.remove(e.getPlayer().getName());
	}

}
