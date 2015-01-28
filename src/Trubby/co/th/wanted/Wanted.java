package Trubby.co.th.wanted;

public class Wanted {

	public String name;
	public float wanted;
	public long time;
	
	public Wanted(String name, float wanted, long time){
		
		this.name = name;
		this.wanted = wanted;
		this.time = time;
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getWanted() {
		return wanted;
	}

	public void setWanted(float wanted) {
		this.wanted = wanted;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	
}
