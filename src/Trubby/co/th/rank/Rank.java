package Trubby.co.th.rank;

public class Rank {

	String name;
	String icon;
	int cost;
	int kill;
	
	Rank(String name, String icon, int cost, int kill) {
		this.name = name;
		this.icon = icon;
		this.cost = cost;
		this.kill = kill;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCost() {
		return cost;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getKill() {
		return kill;
	}

	public void setKill(int kill) {
		this.kill = kill;
	}

	
	
}
