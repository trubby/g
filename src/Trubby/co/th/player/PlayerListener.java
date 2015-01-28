package Trubby.co.th.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.iCo6.system.events.HoldingsUpdate;

import Trubby.co.th.GTA;
import Trubby.co.th.GTAPlayer;
import Trubby.co.th.Utils.MobsUtil;

public class PlayerListener implements Listener{

	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		GTA.getPlayerManager().addPlayer(e.getPlayer());
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e){
		GTA.getPlayerManager().removePlayer(e.getPlayer());
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e){
		Player death = e.getEntity();
		e.setDeathMessage(null);
		if(death.getKiller() instanceof Player){
			//KILLER
			Player killer = death.getKiller();
			
			GTAPlayer p = GTA.getPlayerManager().getGTAplayer(killer.getName());
			p.setKill(p.getKill() + 1);
			GTA.getPlayerManager().addWanted(killer, 1.0f);
			
			//killer.sendMessage(ChatColor.RED + "You killed " + ChatColor.YELLOW + death.getName() + "." + ChatColor.RED + " You will be chase by cops.");
			
			if(death.getWorld().getName().equalsIgnoreCase("gta")){
				MobsUtil.randomCop(killer.getLocation(), (int) p.getWanted());
			}
			
			//DEATH
			GTAPlayer deathp = GTA.getPlayerManager().getGTAplayer(death.getName());
			deathp.setDeath(deathp.getDeath() + 1);
			
			//death.sendMessage(ChatColor.RED + "You were killed by " + ChatColor.YELLOW + killer.getName() + ".");
			
			//BROADCAST
			ItemStack is = killer.getItemInHand();
			if(is.getType() == Material.WOOD_SPADE || is.getType() == Material.STONE_SPADE || is.getType() == Material.IRON_SPADE || is.getType() == Material.GOLD_SPADE || is.getType() == Material.DIAMOND_SPADE){
				Bukkit.broadcastMessage(ChatColor.GREEN + killer.getName() + " " + ChatColor.WHITE + GTA.getStringManager().getGunIcon("pistol") + ChatColor.RED + " " + ChatColor.STRIKETHROUGH + death.getName());
			}else if(is.getType() == Material.WOOD_HOE || is.getType() == Material.STONE_HOE || is.getType() == Material.IRON_HOE || is.getType() == Material.GOLD_HOE || is.getType() == Material.DIAMOND_HOE){
				Bukkit.broadcastMessage(ChatColor.GREEN + killer.getName() + " " + ChatColor.WHITE + GTA.getStringManager().getGunIcon("shotgun") + ChatColor.RED + " " + ChatColor.STRIKETHROUGH + death.getName());
			}else if(is.getType() == Material.WOOD_PICKAXE || is.getType() == Material.STONE_PICKAXE || is.getType() == Material.IRON_PICKAXE || is.getType() == Material.GOLD_PICKAXE || is.getType() == Material.DIAMOND_PICKAXE){
				Bukkit.broadcastMessage(ChatColor.GREEN + killer.getName() + " " + ChatColor.WHITE + GTA.getStringManager().getGunIcon("assault") + ChatColor.RED + " " + ChatColor.STRIKETHROUGH + death.getName());
			}else if(is.getType() == Material.WOOD_AXE || is.getType() == Material.STONE_AXE || is.getType() == Material.IRON_AXE || is.getType() == Material.GOLD_AXE || is.getType() == Material.DIAMOND_AXE){
				Bukkit.broadcastMessage(ChatColor.GREEN + killer.getName() + " " + ChatColor.WHITE + GTA.getStringManager().getGunIcon("sniper") + ChatColor.RED + " " + ChatColor.STRIKETHROUGH + death.getName());
			}else{
				Bukkit.broadcastMessage(ChatColor.GREEN + killer.getName() + " " + ChatColor.WHITE + GTA.getStringManager().getGunIcon("melee") + ChatColor.RED + " " + ChatColor.STRIKETHROUGH + death.getName());
			}
			
			//ECONOMY
			stealMoney(killer, death, deathp.getWanted());
			
			p.updateScoreboard();
			deathp.updateScoreboard();
				
		}else{
			return;
		}
	}
	
	public void stealMoney(Player stealer, Player stealed, float wanted){
		double stealed_money = GTA.economy.getBalance(stealed.getName());
		double amount = 0;
		if(wanted >= 5f){
			amount = (int) ((stealed_money/100) * 60);
			GTA.economy.withdrawPlayer(stealed.getName(), amount);
			GTA.economy.depositPlayer(stealer.getName(), amount);
		}else if(wanted >= 4.0f){
			amount = (int) ((stealed_money/100) * 40);
			GTA.economy.withdrawPlayer(stealed.getName(), amount);
			GTA.economy.depositPlayer(stealer.getName(), amount);
		}else if(wanted >= 3.0f){
			amount = (int) ((stealed_money/100) * 30);
			GTA.economy.withdrawPlayer(stealed.getName(), amount);
			GTA.economy.depositPlayer(stealer.getName(), amount);
		}else if(wanted >= 2.0f){
			amount = (int) ((stealed_money/100) * 20);
			GTA.economy.withdrawPlayer(stealed.getName(), amount);
			GTA.economy.depositPlayer(stealer.getName(), amount);
		}else if(wanted >= 1.0f){
			amount = (int) ((stealed_money/100) * 15);
			GTA.economy.withdrawPlayer(stealed.getName(), amount);
			GTA.economy.depositPlayer(stealer.getName(), amount);
		}else if(wanted >= 0f){
			amount = (int) ((stealed_money/100) * 10);
			GTA.economy.withdrawPlayer(stealed.getName(), amount);
			GTA.economy.depositPlayer(stealer.getName(), amount);
		}
		
		stealer.sendMessage(ChatColor.GREEN + "You stolen " + ChatColor.YELLOW + amount + "$" + ChatColor.GREEN + " from " + stealed.getName());
		stealed.sendMessage(ChatColor.RED + stealer.getName() + " take " + ChatColor.YELLOW + amount + "$" + ChatColor.RED + " from you.");
	}
	
	@EventHandler
	public void onFoodlevelchange(FoodLevelChangeEvent e){
		if(e.getEntity().getWorld().getName().equalsIgnoreCase("gtalobby")){
			Player p = (Player) e.getEntity();
			p.setSaturation(12.8f);
		}
	}
	
	@EventHandler
	public void onHoldUpdate(final HoldingsUpdate e){
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(GTA.instance, new Runnable() {
			
			@Override
			public void run() {
				GTAPlayer gtap = GTA.getPlayerManager().getGTAplayer(e.getAccountName());
				gtap.updateScoreboard();
			}
		}, 5L);
	}
	
	HashMap<String, Integer> melee_log = new HashMap<>();
	//ANTI CHEAT!!!!!!!!!!
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
				
				if(melee_log.containsKey(p.getName())){
					int i = melee_log.get(p.getName());
					System.out.println(i);
					if(i > 2){
						p.kickPlayer(ChatColor.RED + "[" + ChatColor.WHITE + "GTAAntiCheat" + ChatColor.RED + "]" + ChatColor.WHITE + " Melee attack too fast!");
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
				}
			}else{
				e.setCancelled(true);
			}
		}
	}
	
	ArrayList<UUID> com_cool = new ArrayList<>();
	
	@EventHandler
	public void onPlayerCompass(PlayerInteractEvent e){
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(e.getPlayer().getItemInHand().getType() == Material.COMPASS){
				final Player p = e.getPlayer();
				if(!p.getWorld().getName().equalsIgnoreCase("GTA")){
					p.sendMessage(ChatColor.RED + "Compass can be used only in GTA world.");
					return;
				}
				
				//Cooldown
				if(com_cool.contains(p.getUniqueId())){
					p.sendMessage(ChatColor.RED + "Compass has 30 sec. cooldown.");
					return;
				}
				
				Player target = null;
				double targetdistance = 0.0;
				for(Player all : Bukkit.getOnlinePlayers()){
					if(all.getWorld().getName().equalsIgnoreCase("gta") && !all.getName().equalsIgnoreCase(p.getName())){
						double distance = p.getLocation().distance(all.getLocation());
						if(distance < 100){
							GTAPlayer gtap = GTA.getPlayerManager().getGTAplayer(all.getPlayer().getName());
							if(gtap.getWanted() > 0.0f){
								if(targetdistance == 0.0){
									targetdistance = distance;
									target = all;
								}else if(distance < targetdistance){
									targetdistance = distance;
									target = all;
								}
							}
						}
					}
				}
				
				if(target != null){
					p.setCompassTarget(target.getLocation());
					p.sendMessage(ChatColor.GREEN + "Set Coppass target to : " + ChatColor.RED + "" + ChatColor.UNDERLINE + target.getName());
					p.playSound(p.getLocation(), Sound.LEVEL_UP, 1f, 1f);
					com_cool.add(p.getUniqueId());
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(GTA.instance, new Runnable() {
						
						@Override
						public void run() {
							com_cool.remove(p.getUniqueId());
						}
					}, 600L);
				}else{
					p.sendMessage(ChatColor.RED + "No player near you.");
				}
			}
		}
	}
	
	@EventHandler
	public void PlayerClickVillager(PlayerInteractEntityEvent e){
		if(e.getRightClicked() instanceof Villager){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void PlayerJoinMessage(PlayerJoinEvent e){
		e.setJoinMessage(ChatColor.GRAY + "[" + ChatColor.DARK_GRAY + "+" + ChatColor.GRAY + "] " + e.getPlayer().getName() + " join the server.");
	}
	
	@EventHandler
	public void PlayerJoinMessage(PlayerQuitEvent e){
		e.setQuitMessage(ChatColor.GRAY + "[" + ChatColor.DARK_GRAY + "-" + ChatColor.GRAY + "] " + e.getPlayer().getName() + " left the server.");
	}
	
	/**
	 * 		ETC.
	 */
	
	@EventHandler
	public void PlayerJoinTeamRefresh(PlayerJoinEvent e){
		for(Player p : Bukkit.getOnlinePlayers()){
			GTAPlayer gtap = GTA.getPlayerManager().getGTAplayer(p.getName());
			if(gtap != null){
				gtap.updateTeam();
			}
		}
	}
}
