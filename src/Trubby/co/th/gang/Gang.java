package Trubby.co.th.gang;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class Gang {

	String name = "";
	ArrayList<String> member = new ArrayList<String>();
	int max = 4;
	String leader = null;
	
	public Gang(Player p, String name){
		member.add(p.getName());
		this.name = name;
		this.leader = p.getName();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getMember() {
		return member;
	}

	public void setMember(ArrayList<String> member) {
		this.member = member;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}
	
}
