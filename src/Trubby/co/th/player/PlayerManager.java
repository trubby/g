package Trubby.co.th.player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import Trubby.co.th.GTA;
import Trubby.co.th.GTAPlayer;

public class PlayerManager {

	HashMap<String, GTAPlayer> playerlist = new HashMap<String, GTAPlayer>();
	
	/**
	 * 1 = 3 min.
	 * 2 = 6 min.
	 * 3 = 9 min.
	 * 4 = 12 min.
	 * 5 = 15 min.
	 */
	
	public void addPlayer(Player p){
		if(!checkDataExist(p)){
			try {
				Statement statement = GTA.getSql().connection.createStatement();
				statement.executeUpdate("INSERT INTO GTA (`Name`,`Kill`,`Death`) VALUES ('" + p.getName() + "', '0', '0')");
				statement.close();
				
				Bukkit.broadcastMessage("[GTA]" + ChatColor.GOLD + "Greeting! :D [sql-registered] " + ChatColor.WHITE + p.getName());
			} catch (Exception e) {
			}
		}
		playerlist.put(p.getName(), new GTAPlayer(p));
	}
	
	public void removePlayer(Player p){
		GTAPlayer gtap = playerlist.get(p.getName());
		if(gtap != null){
			gtap.save();
			playerlist.remove(p.getName());
		}
	}
	
	public void addWanted(Player p, float wanted){
		GTA.getWantedManager().addWanted(p, wanted);
	}
	
	public void removeWanted(Player p){
		GTA.getWantedManager().clearWanted(p);
	}
	
	public void removeWanted(String str){
		
	}
	
	public void addMoney(Player p, int amount){
		GTA.economy.depositPlayer(p.getName(), (double)amount);
		p.sendMessage("You get " + ChatColor.GREEN + amount + "$" + ChatColor.RESET + " from looting chest.");
	}
	
	public GTAPlayer getGTAplayer(String str) {
		return playerlist.get(str);
	}
	
	public boolean checkDataExist(Player p){
		try {
			PreparedStatement pre = GTA.getSql().connection.prepareStatement("SELECT * FROM `GTA` WHERE Name=?;");
			pre.setString(1, p.getName());
			ResultSet res = pre.executeQuery();
			boolean containPlayer = res.next();
			
			pre.close();
			res.close();
			
			return containPlayer;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public void test(){
		Bukkit.broadcastMessage("" + System.currentTimeMillis());
	}
}
