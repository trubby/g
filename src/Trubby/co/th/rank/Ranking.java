package Trubby.co.th.rank;

import java.util.ArrayList;



import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import Trubby.co.th.GTA;
import Trubby.co.th.GTAPlayer;

public class Ranking implements Listener{
	
	public ArrayList<Rank> rankList = new ArrayList<Rank>();
	public ArrayList<String> upgrading = new ArrayList<String>();
	
	public Ranking() {
		rankList.add(new Rank("default", ChatColor.YELLOW + "" + '\u9202', 0, 0)); // 0 
		rankList.add(new Rank("1-2", ChatColor.GOLD + "" + '\u9201', 100, 20)); // 2
		rankList.add(new Rank("1-3", ChatColor.RED + "" + '\u9201', 150, 30)); // 3 
		rankList.add(new Rank("2-1", ChatColor.YELLOW + "" + '\u9202', 300, 50)); // 4 -
		rankList.add(new Rank("2-2", ChatColor.GOLD + "" + '\u9202', 400, 60)); // 5
		rankList.add(new Rank("2-3", ChatColor.RED + "" + '\u9202', 500, 70)); // 6 
		rankList.add(new Rank("3-1", ChatColor.YELLOW + "" + '\u9203', 700, 100)); // 7 -
		rankList.add(new Rank("3-2", ChatColor.GOLD + "" + '\u9203', 800, 130)); // 8
		rankList.add(new Rank("3-3", ChatColor.RED + "" + '\u9203', 900, 160)); // 9 
		rankList.add(new Rank("1^1", ChatColor.YELLOW + "" + '\u9204', 1200, 250)); // 10 - 
		rankList.add(new Rank("1^2", ChatColor.GOLD + "" + '\u9204', 1400, 350)); // 11
		rankList.add(new Rank("1^3", ChatColor.RED + "" + '\u9204', 1600, 450)); // 12
		rankList.add(new Rank("2^1", ChatColor.YELLOW + "" + '\u9205', 2000, 600)); // 13 -
		rankList.add(new Rank("2^2", ChatColor.GOLD + "" + '\u9205', 2400, 800)); // 14
		rankList.add(new Rank("2^3", ChatColor.RED + "" + '\u9205', 2800, 1000)); // 15
		rankList.add(new Rank("3^1", ChatColor.YELLOW + "" + '\u9206', 3500, 1300)); // 16 -
		rankList.add(new Rank("3^2", ChatColor.GOLD + "" + '\u9206', 4000, 1500)); // 17
		rankList.add(new Rank("3^3", ChatColor.RED + "" + '\u9206', 4500, 1800)); // 18
		rankList.add(new Rank("1d1", ChatColor.YELLOW + "" + '\u9208', 6000, 2400)); // 19 -
		rankList.add(new Rank("1d2", ChatColor.GOLD + "" + '\u9208', 7000, 2800)); // 20
		rankList.add(new Rank("1d3", ChatColor.RED + "" + '\u9208', 8000, 3200)); // 21
	}
	
	@EventHandler
	public void onInterackNPC(PlayerInteractEntityEvent e){
		if(e.getRightClicked() instanceof Villager){
			Villager vil = (Villager) e.getRightClicked();
			Player p = e.getPlayer();
			
			if(vil.getCustomName().equalsIgnoreCase(ChatColor.GOLD + "Rank Upgrade")){
				
				if(upgrading.contains(p.getName())){
					return;
				}
				
				Rank nextRank = getNextRank(p);
				
				if(nextRank != null){
					p.sendMessage(ChatColor.STRIKETHROUGH + "                                               ");
					p.sendMessage(ChatColor.GOLD + "Requirement:");
					p.sendMessage(ChatColor.GRAY + "Next rank: " + nextRank.getIcon());
					p.sendMessage(ChatColor.GRAY + "Cost : " + ChatColor.YELLOW + nextRank.getCost() + "$");
					p.sendMessage(ChatColor.GRAY + "Kills : " + ChatColor.RED + nextRank.kill);
					p.sendMessage("");
					p.sendMessage(ChatColor.GREEN + "Do you want to upgrade your rank? Type the answer!");
					p.sendMessage(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Yes / No?");
					p.sendMessage(ChatColor.STRIKETHROUGH + "                                               ");
					
					upgrading.add(p.getName());
					
				}else{
					p.sendMessage("You're already at maximum rank.");
				}
			
			}
		}
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		Player p = e.getPlayer();
		if(upgrading.contains(p.getName())){
			e.setCancelled(true);
			if(e.getMessage().equalsIgnoreCase("yes")){
				p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "YES!");
				upgrading.remove(p.getName());
				UpRank(p);
			}else if(e.getMessage().equalsIgnoreCase("no")){
				p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "NO!");
				upgrading.remove(p.getName());
			}else{
				p.sendMessage(ChatColor.YELLOW + "\u0e04\u0e38\u0e13\u0e22\u0e37\u0e19\u0e22\u0e31\u0e19\u0e01\u0e32\u0e23\u0e2d\u0e31\u0e1e \u0052\u0061\u006e\u006b \u0e2b\u0e23\u0e37\u0e2d\u0e44\u0e21\u0e48 \u0059\u0065\u0073\u002f\u004e\u006f\u003f");
			}
		}
	}
	
	@EventHandler
	public void onLogout(PlayerQuitEvent e){
		if(upgrading.contains(e.getPlayer().getName())){
			upgrading.remove(e.getPlayer().getName());
		}
	}
	
	public void UpRank(Player p){
		Rank nextRank = getNextRank(p);
		GTAPlayer gtap = GTA.getPlayerManager().getGTAplayer(p.getName());
		
		//KILL
		if(gtap.getKill() < nextRank.kill){
			p.sendMessage(ChatColor.RED + "You don't have enough kills! " + nextRank.kill + " kills.");
			return;
		}
				
		//COST
		if(GTA.economy.getBalance(p.getName()) < nextRank.cost){
			p.sendMessage(ChatColor.RED + "You don't have enough money! " + nextRank.cost + "$");
			return;
		}
		
		GTA.gmhook.setGroup(p, nextRank.getName());
		GTA.economy.withdrawPlayer(p.getName(), (double)nextRank.cost);
		
		p.sendMessage(ChatColor.STRIKETHROUGH + "                                               ");
		p.sendMessage(ChatColor.GREEN + "SUCCESSFUL upgrade your rank! :D");
		p.sendMessage(ChatColor.GREEN + "Your current rank is now change");
		p.sendMessage("\u00bb\u00bb\u00bb\u00bb " + nextRank.getIcon() + ChatColor.RESET + " \u00ab\u00ab\u00ab\u00ab");
		p.sendMessage(ChatColor.GREEN + "Good luck!");
		p.sendMessage(ChatColor.STRIKETHROUGH + "                                               ");
		
		p.getWorld().playSound(p.getLocation(), Sound.LEVEL_UP, 1f, 1f);
	}
	
	
	public int getCurrentRank(Player p){
		String currentRank = GTA.gmhook.getGroup(p);
		for(Rank rank : rankList){
			if(rank.getName().equalsIgnoreCase(currentRank)){
				return rankList.indexOf(rank);
			}
		}
		
		return -1;
	}
	
	public Rank getNextRank(Player p){
		int currentRank = getCurrentRank(p);
		if(currentRank != -1 && currentRank < rankList.size() - 1){
			return rankList.get(currentRank + 1);
		}
		return null;
	}
	
}
