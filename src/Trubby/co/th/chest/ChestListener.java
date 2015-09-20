package Trubby.co.th.chest;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;

import Trubby.co.th.GTA;
import Trubby.co.th.GTAPlayer;
import Trubby.co.th.Utils.TimeConverter;

public class ChestListener implements Listener{
	
	public Random ran = new Random();
	long chestLootTime = 1000 * 60 * 15;

	@EventHandler
	public void onCloseChest(InventoryCloseEvent e){
		if(!e.getPlayer().getWorld().getName().equalsIgnoreCase("gta")){
			return;
		}
		
		if(e.getInventory().getType() == InventoryType.CHEST){
			Player p = (Player) e.getPlayer();
			p.playSound(p.getLocation(), Sound.CHEST_CLOSE, 1f, 1f);
		}
	}
	
	//LOOT CHEST
	@EventHandler
	public void onClickChest(PlayerInteractEvent e){
		if(!e.getPlayer().getWorld().getName().equalsIgnoreCase("gta")){
			return;
		}
		
		if(e.getAction() == Action.LEFT_CLICK_BLOCK){
			if(e.getClickedBlock().getType() == Material.CHEST){
				if(e.getPlayer().isOp()){
					if(e.getPlayer().getItemInHand().getType() == Material.GOLD_AXE){
						return;
					}
				}
				//GTA.getChestManager().breakChest((Chest)e.getClickedBlock().getState(), e.getPlayer());
			}
		}else if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(e.getClickedBlock().getType() == Material.CHEST){
				e.setCancelled(true);
				Player p = (Player) e.getPlayer();
				Chest chest = (Chest)e.getClickedBlock().getState();
				GTAPlayer gtap = GTA.getPlayerManager().getGTAplayer(e.getPlayer().getName());
				if(gtap.lootedChest.containsKey(chest.getLocation())){
					long timesince = System.currentTimeMillis() - gtap.lootedChest.get(chest.getLocation());
					long timeleft = chestLootTime - timesince;
					if(timesince < chestLootTime){
						p.sendMessage(ChatColor.DARK_RED + "Next loot will come in : " + ChatColor.GRAY + TimeConverter.milisToMin(timeleft));
						return;
					}else{
						GTA.getChestManager().lootChest(p, chest);
					}
				}else{
					GTA.getChestManager().lootChest(p, chest);
				}
			}
		}
	}
	
}
