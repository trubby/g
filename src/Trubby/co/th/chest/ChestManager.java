package Trubby.co.th.chest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import Trubby.co.th.GTA;
import Trubby.co.th.Utils.TimeConverter;

public class ChestManager {

	public Random ran = new Random();
	public List<GTAChest> chestlist = new ArrayList<>();
	public List<GTAItem> itemlist = new ArrayList<>();
	public List<GTAItem> gunlist = new ArrayList<>();
	public List<GTAItem> armourlist = new ArrayList<>();
	public List<GTAItem> copdroplist = new ArrayList<>();
	
	public long chest_delay = 12000L;
	
	@SuppressWarnings("deprecation")
	public void breakChest(final Chest chest){
		
		final Location loc = chest.getLocation();
		final byte data = chest.getBlock().getData();
		
		final GTAChest gtachest = new GTAChest(loc, data);
		chestlist.add(gtachest);
		chest.getBlock().setType(Material.AIR);
		chest.getWorld().playEffect(chest.getLocation(), Effect.STEP_SOUND, 54);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(GTA.instance, new Runnable() {
			
			@Override
			public void run() {
				loc.getBlock().setType(Material.CHEST);
				Block block = loc.getBlock();
				block.setData(data);
				
				fillChest((Chest)block.getState());
				
				chestlist.remove(gtachest);
			}
		}, chest_delay);
	}
	
	@SuppressWarnings("deprecation")
	public void breakChest(final Chest chest,long delay){
		
		final Location loc = chest.getLocation();
		final byte data = chest.getBlock().getData();
		
		final GTAChest gtachest = new GTAChest(loc, data);
		chestlist.add(gtachest);
		chest.getBlock().setType(Material.AIR);
		chest.getWorld().playEffect(chest.getLocation(), Effect.STEP_SOUND, 54);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(GTA.instance, new Runnable() {
			
			@Override
			public void run() {
				loc.getBlock().setType(Material.CHEST);
				Block block = loc.getBlock();
				block.setData(data);
				
				fillChest((Chest)block.getState());
				
				chestlist.remove(gtachest);
			}
		}, delay);
	}
	
	@SuppressWarnings("deprecation")
	public void breakChest(final Chest chest,Player p){
		
		final Location loc = chest.getLocation();
		final byte data = chest.getBlock().getData();
		
		final GTAChest gtachest = new GTAChest(loc, data);
		chestlist.add(gtachest);
		chest.getBlock().setType(Material.AIR);
		chest.getWorld().playEffect(chest.getLocation(), Effect.STEP_SOUND, 54);
		
		GTA.getPlayerManager().addMoney(p, ran.nextInt(3) + 1);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(GTA.instance, new Runnable() {
			
			@Override
			public void run() {
				loc.getBlock().setType(Material.CHEST);
				Block block = loc.getBlock();
				block.setData(data);
				
				fillChest((Chest)block.getState());
				
				chestlist.remove(gtachest);
			}
		}, chest_delay);
	}
	
	@SuppressWarnings("deprecation")
	public void forceRestoreAllChest(){
		for(GTAChest chest : chestlist){
			chest.loc.getBlock().setType(Material.CHEST);
			Block block = chest.loc.getBlock();
			block.setData(chest.data);
			
			fillChest((Chest)block.getState());
		}
	}
	
	public void fillChest(Chest chest){
		Inventory inv = chest.getInventory();
		
		//MIST. fill
		int i = 0;
		for(GTAItem gtaitem : GTA.getChestManager().itemlist){
			if(ran.nextInt(100) + 1 <= gtaitem.getChance()){
				ItemStack is = gtaitem.getItem();
				//random amount
				if(is != null){
				
					if(gtaitem.getAmount() != 1){
						is.setAmount(ran.nextInt(gtaitem.getAmount()) +1);
					}
					
					if(is.getAmount() != 0){
						inv.addItem(is);
					}
				}
			}
			i++;
		}
		
		//GUNS fill
		/*int gun = 0;
		for (int j = 0; j < 6; j++) {
			if(gun > 0)break;
			
			GTAItem gtaitem = gunlist.get(ran.nextInt(gunlist.size()));
			if(ran.nextInt(1000) + 1 <= gtaitem.getChance()){
				inv.addItem(gtaitem.getItem());
				gun++;
			}
		}*/
		
		int gun = 0;
		if(ran.nextInt(4) == 1){ //15%
			for (GTAItem gtaitem : gunlist) {
				if(gun > 0)break;
				
				if(ran.nextInt(1000) + 1 <= gtaitem.getChance()){
					inv.addItem(gtaitem.getItem());
					gun++;
				}
			}
		}
		
		//System.out.println("Chest fill count : " + i + " / Has gun : " + gun);
		
		//Armour fill
		if(ran.nextInt(3) == 1){ //18%
			for(GTAItem gtaitem : armourlist){
				if(ran.nextInt(100) + 1 <= gtaitem.getChance()){
					inv.addItem(gtaitem.getItem());
					break;
				}
			}
		}
	}
	
	/*
	 *  Chest delay
	 */
	
	public void setChest_delay(long chest_delay) {
		this.chest_delay = chest_delay;
	}
	
	public void runChestDelayCheckerTask(){
		Bukkit.getScheduler().scheduleSyncRepeatingTask(GTA.instance, new Runnable() {
			
			@Override
			public void run() {
				checkChestDelay();
			}
		}, 6000, 6000);
	}
	
	// 0-5 > 10 min.
	// 6-10 > 8 min.
	// 11-20 > 7 min.
	// 21-30 > 6 min.
	// 31-40 > 5 min.
	// 41-50 > 4 min.
	
	public void checkChestDelay(){
		int onlines = Bukkit.getOnlinePlayers().length;
		long delay = 12000;
		if(onlines > 41){
			delay = 4800;
		}else if(onlines > 31){
			delay = 6000;
		}else if(onlines > 21){
			delay = 7200;
		}else if(onlines > 11){
			delay = 8400;
		}else if(onlines > 5){
			delay = 9600;
		}else{
			delay = 12000;
		}
		
		setChest_delay(delay);
		
		for(Player p : Bukkit.getOnlinePlayers()){
			p.sendMessage("[GTA]Chest regeneration delay has been set to : " + TimeConverter.milisToMin(delay * 50));
		}
		System.out.println("Chest Delay has been update to : " + delay);
	}
}
