package Trubby.co.th.gang;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class GangManager {
	
	ArrayList<String> inGang = new ArrayList<String>();
	ArrayList<Gang> gangList = new ArrayList<Gang>();
	
	//IS IN GANG?
	public boolean isInGang(Player p){
		if(inGang.contains(p.getName())){
			return true;
		}
		return false;
	}
	
	//TODO CRAETE GANG
	public void createGang(Player p){
		
	}
	
	//TODO INCITE
	public void addPlayer(Player p, Gang g){
		
	}
	
}
