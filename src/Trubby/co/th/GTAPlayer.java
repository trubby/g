package Trubby.co.th;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import Trubby.co.th.Utils.ScoreboardUtils;

public class GTAPlayer {

	public String name;

	public int kill = 0;
	public int death = 0;
	public String kdr = "1.00";
	public float wanted = 0.0f;
	//score board
	public ScoreboardUtils sbu;
	public Scoreboard sb;
	public Objective ob;
	public Objective ob_healthbar;
	//public HashMap<String, Team> team_cache = new HashMap<String, Team>();
	public ArrayList<Team> team_cache = new ArrayList<Team>();
	
	
	public GTAPlayer(Player p){
		name = p.getName();
		
		try {
			Statement statement = GTA.getSql().connection.createStatement();
			ResultSet res = statement.executeQuery("SELECT * FROM GTA WHERE Name='"+name+"'");
			res.next();
			kill = res.getInt("Kill");
			death = res.getInt("Death");
		} catch (SQLException e) {
		}
		
		updateKdr();
		
		//set up scoreboard
		sbu = new ScoreboardUtils(ChatColor.RED + "" + ChatColor.BOLD + "GTA");
		sbu.add(ChatColor.AQUA + "Kills");
		sbu.add(""+kill);
		sbu.add(ChatColor.AQUA + "Deaths");
		sbu.add(""+death);
		sbu.add(ChatColor.AQUA + "K/D Ratio");
		sbu.add(""+kdr);
		sbu.add(ChatColor.AQUA + "Money");
		sbu.add(ChatColor.GOLD + "" + (int)GTA.economy.getBalance(name));
		sbu.add(ChatColor.AQUA + "Wanted");
		sbu.add(""+GTA.getWantedManager().getWantedTab(wanted));
		sbu.build();
		
		sb = sbu.getScoreboard();
		ob = sb.getObjective("GTA");
		
		ob_healthbar = sb.registerNewObjective("shothealth", "health");
		ob_healthbar.setDisplaySlot(DisplaySlot.BELOW_NAME);
		ob_healthbar.setDisplayName(ChatColor.RED + "\u2764");
		
		updateTeam();
		
		p.setScoreboard(sb);
	}
	
	public void updateScoreboard(){
		updateKdr();
		ob.unregister();
		sbu.reset();
		sbu.add(ChatColor.AQUA + "Kills");
		sbu.add(""+kill);
		sbu.add(ChatColor.AQUA + "Deaths");
		sbu.add(""+death);
		sbu.add(ChatColor.AQUA + "K/D Ratio");
		sbu.add(""+kdr);
		sbu.add(ChatColor.AQUA + "Money");
		sbu.add(ChatColor.GOLD + "" + (int)GTA.economy.getBalance(name));
		sbu.add(ChatColor.AQUA + "Wanted");
		sbu.add(""+GTA.getWantedManager().getWantedTab(wanted));
		sbu.update();
		ob = sb.getObjective("GTA");
		
		updateTeam();
	}
	
	public void updateTeam(){
		if(team_cache.size() > 0){
			for(Team t : team_cache){
				t.unregister();
			}
		}
		
		team_cache.clear();
		
		int i = 1;
		for(Player p : Bukkit.getOnlinePlayers()){
			Team team = sb.registerNewTeam("show-" + i);
			//team.setDisplayName(""+i);
			
			if(GTA.getWantedManager().wantedlist.containsKey(p.getName())){
				team.setPrefix(ChatColor.translateAlternateColorCodes('&', GTA.gmhook.getPrefix(p) + ChatColor.RED));
				team.setSuffix(ChatColor.translateAlternateColorCodes('&', GTA.gmhook.getSuffix(p)));
			}else{
				team.setPrefix(ChatColor.translateAlternateColorCodes('&', GTA.gmhook.getPrefix(p)));
				team.setSuffix(ChatColor.translateAlternateColorCodes('&', GTA.gmhook.getSuffix(p)));
			}
			
			team.addPlayer(p);
			
			team_cache.add(team);
			i++;
		}
	}
	
	public void save(){
		try {
			Statement statement = GTA.getSql().connection.createStatement();
			statement.executeUpdate("UPDATE  `MC`.`GTA` SET  `Kill` =  '"+kill+"',`Death` =  '"+death+"' WHERE  `GTA`.`Name` =  '"+name+"';");
			statement.close();
		} catch (SQLException e) {
		}
	}
	
	/** 
	 *     GETTER AND SETTER 
	 */
	
	public String getName() {
		return name;
	}
	
	public int getKill() {
		return kill;
	}
	
	public void setKill(int kill) {
		this.kill = kill;
		updateKdr();
	}

	public int getDeath() {
		return death;
	}

	public void setDeath(int death) {
		this.death = death;
		updateKdr();
	}

	public String getKdr() {
		return kdr;
	}

	public void setKdr(String kdr) {
		this.kdr = kdr;
	}
	
	public void updateKdr() {
		
		if(kill == 0){
			kdr = "1.00";
			return;
		}
		
		float calkdr = (float) kill/death;
		this.kdr = new DecimalFormat("0.00").format(calkdr);
	}

	public float getWanted() {
		return wanted;
	}

	public void setWanted(float wanted) {
		this.wanted = wanted;
	}

	public Scoreboard getSb() {
		return sb;
	}

	public Objective getOb() {
		return ob;
	}
}
