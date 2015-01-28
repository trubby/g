package Trubby.co.th.player;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDamageEvent;

import Trubby.co.th.GTA;

public class CarListener implements Listener{

	HashMap<UUID, Short> car = new HashMap<UUID, Short>();
	
	@EventHandler
	public void onCarDamage(VehicleDamageEvent e){
		//Bukkit.broadcastMessage("" + e.getDamage());
		final UUID carId = e.getVehicle().getUniqueId();
		
		if(e.getAttacker() == null)return;
		
		if(car.containsKey(carId)){
			short oldHit = car.get(carId);
			if(oldHit > 3){
				Location loc = e.getVehicle().getLocation();
				e.getVehicle().getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 2.0f, false, false);
				e.getVehicle().remove();
				
				car.remove(carId);
			}
			else{
				car.put(carId, (short) (oldHit + 1));
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(GTA.instance, new Runnable() {
					
					@Override
					public void run() {
						car.remove(carId);
					}
				}, 80L);
			}
		}else{
			car.put(carId, (short) 1);
		}
		//Bukkit.broadcastMessage(car.size() + "");
		//Bukkit.broadcastMessage("" + e.getAttacker());
	}
}
