package Trubby.co.th;

import java.util.HashMap;

public class StringManager {

	HashMap<String, String> gun_message = new HashMap<>();
	
	public StringManager(){
		this.gun_message.put("pistol", "\u928E");
		this.gun_message.put("shotgun", "\u928A\u928B");
		this.gun_message.put("assault", "\u9286\u9287");
		this.gun_message.put("sniper", "\u9284\u9285");
		this.gun_message.put("melee", "\u928c");
	}

	public String getGunIcon(String type){
		return this.gun_message.get(type);
	}
}
