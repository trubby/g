package Trubby.co.th.Utils;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class ClearUtil {
	
	public static int clearEntity(World world, EntityType type){
		int count = 0;
		for(Entity en : world.getEntities()){
			if(en.getType() == type){
				en.remove();
				count++;
			}
		}
		
		return count;
	}

}
