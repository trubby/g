package Trubby.co.th.wanted;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import Trubby.co.th.GTA;
import Trubby.co.th.GTAPlayer;
import Trubby.co.th.Utils.TimeConverter;

public class WantedManager {

	public HashMap<String,Wanted> wantedlist = new HashMap<>();
	
	public void addWanted(Player p, float add){
		float newwanted = 0f;
		if(wantedlist.containsKey(p.getName())){
			Wanted wanted = wantedlist.get(p.getName());
			newwanted = wanted.getWanted() + add;
			wanted.setWanted(newwanted);
			wanted.setTime(System.currentTimeMillis());
			
		}else{
			wantedlist.put(p.getName(), new Wanted(p.getName(), add, System.currentTimeMillis()));
			newwanted = add;
		}
		
		GTAPlayer gtaplayer = GTA.getPlayerManager().getGTAplayer(p.getName());
		gtaplayer.setWanted(newwanted);
		gtaplayer.updateScoreboard();
		
		p.sendMessage("Wanted level : " + ChatColor.RED + "" + getWantedTab(newwanted) + ChatColor.GRAY +  "  (/wanted check)");
	}
	
	public void clearWanted(Player p){
		wantedlist.remove(p.getName());
		GTAPlayer gtaplayer = GTA.getPlayerManager().getGTAplayer(p.getName());
		gtaplayer.setWanted(0);
		gtaplayer.updateScoreboard();
		
		p.sendMessage(ChatColor.GREEN + "You have escaped from the cops. You're free now!.");
	}
	
	public void clearWanted(String str){
		wantedlist.remove(str);
		GTAPlayer gtaplayer = GTA.getPlayerManager().getGTAplayer(str);
		gtaplayer.setWanted(0);
		gtaplayer.updateScoreboard();
		
		Player p = Bukkit.getPlayer(str);
		p.sendMessage(ChatColor.GREEN + "You have escaped from the cops. You're free now!.");
	}
	
	/**
	 * 		WANTED TASK
	 */
	public void runWantedTask() {
		Iterator<Entry<String, Wanted>> ite = wantedlist.entrySet().iterator();
		while(ite.hasNext()){
			@SuppressWarnings("rawtypes")
			Entry pairs = (Entry)ite.next();
			Wanted w = (Wanted) pairs.getValue();
			
			if(System.currentTimeMillis() - (long) w.getTime() > getWantedTime(w.getWanted())){
				ite.remove();
				clearWanted(w.getName());
			}
		}
		
		/*for (Object object : wantedlist.keySet()) {
			Wanted w = wantedlist.get(object);
			if(System.currentTimeMillis() - (long) w.getTime() > getWantedTime(w.getWanted())){
				clearWanted(w.getName());
			}
		}*/
	}
	
	public long getWantedTime(float wanted){
		if(wanted >= 5){
			return 6*60*1000;
		}else if(wanted >= 4){
			return 4*60*1000;
		}else if(wanted >= 3){
			return 3*60*1000;
		}else if(wanted >= 2){
			return 2*60*1000;
		}else if(wanted >= 1){
			return 1*60*1000;
		}else if(wanted > 0){
			return 1*60*1000;
		}else{
			return 0;
		}
	}
	
	public String getWantedTab(float wanted){
		if(wanted >= 5){
			return ChatColor.YELLOW + "\u272a\u272a\u272a\u272a\u272a";
		}else if(wanted >= 4){
			return ChatColor.YELLOW + "\u272a\u272a\u272a\u272a" + ChatColor.DARK_GRAY + "\u272a";
		}else if(wanted >= 3){
			return ChatColor.YELLOW + "\u272a\u272a\u272a" + ChatColor.DARK_GRAY + "\u272a\u272a";
		}else if(wanted >= 2){
			return ChatColor.YELLOW + "\u272a\u272a" + ChatColor.DARK_GRAY + "\u272a\u272a\u272a";
		}else if(wanted > 0){
			return ChatColor.YELLOW + "\u272a" + ChatColor.DARK_GRAY + "\u272a\u272a\u272a\u272a";
		}else{
			return ChatColor.DARK_GRAY + "\u272a\u272a\u272a\u272a\u272a";
		}
	}
	
	public void checkWantedLevel(Player p){
		if(wantedlist.containsKey(p.getName())){
			Wanted wanted = wantedlist.get(p.getName());
			long timeleft = (long) getWantedTime(wanted.getWanted()) - (System.currentTimeMillis() - wanted.getTime());
			p.sendMessage("Your wanted is : " + getWantedTab(wanted.getWanted()));
			p.sendMessage("Time left : " + ChatColor.GREEN + TimeConverter.milisToMin(timeleft));
		}else{
			p.sendMessage("You don't have any wanted right now.");
		}
	}
	
	public void listWanted(Player p){
		p.sendMessage(ChatColor.GOLD + "" + ChatColor.STRIKETHROUGH + "----------" + ChatColor.RED + " Wanted LIST  " + ChatColor.GOLD + "" + ChatColor.STRIKETHROUGH  + "----------");
		for (Object object : wantedlist.keySet()) {
			Wanted w = wantedlist.get(object);
			long timeleft = (long) getWantedTime(w.getWanted()) - (System.currentTimeMillis() - w.getTime());
			p.sendMessage(w.getName() + " : " + getWantedTab(w.getWanted()) + ChatColor.RESET + " : " + ChatColor.GREEN + TimeConverter.milisToMin(timeleft));
		}
	}
}
