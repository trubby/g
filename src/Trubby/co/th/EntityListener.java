package Trubby.co.th;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingBreakEvent.RemoveCause;
import org.bukkit.inventory.ItemStack;

import Trubby.co.th.chest.GTAItem;

public class EntityListener implements Listener{

	Random ran = new Random();
	
	@EventHandler
	public void onTargetSelect(EntityTargetEvent e){
		if(e.getEntityType() == EntityType.PIG_ZOMBIE){
			if(e.getTarget() instanceof Player){
				Player p = (Player) e.getTarget();
				if(!GTA.getWantedManager().wantedlist.containsKey(p.getName())){
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onKillCops(EntityDeathEvent e){
		if(e.getEntityType() == EntityType.PIG_ZOMBIE){
			LivingEntity le = e.getEntity();
			if(le.getKiller() instanceof Player){
				Player p = le.getKiller();
				GTA.getPlayerManager().addWanted(p, 0.5f);
				
				GTA.economy.depositPlayer(p.getName(), 2);
				GTAPlayer gtap = GTA.getPlayerManager().getGTAplayer(p.getName());
				gtap.updateScoreboard();
				
				p.sendMessage("You earn " + ChatColor.GREEN + "8$ " + ChatColor.RESET + "from killing a policeman.");
				
				for(GTAItem gtai : GTA.getChestManager().copdroplist){
					if(ran.nextInt(100) + 1 <= gtai.getChance()){
						ItemStack is = gtai.getItem();
						//random amount
						if(is != null){
							
							if(gtai.getAmount() != 1){
								is.setAmount(ran.nextInt(gtai.getAmount()) +1);
							}
								
							if(is.getAmount() != 0){
								le.getWorld().dropItemNaturally(le.getLocation(), is);
								break;
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onHangBreak(HangingBreakByEntityEvent e){
		if(e.getRemover() instanceof Player){
			Player p = (Player) e.getRemover();
			if(!p.isOp()){
				e.setCancelled(true);
			}
			return;
		}
		
		else{
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onHangDamageEntity(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof ItemFrame){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onHangDamage(EntityDamageEvent e){
		if(e.getEntity() instanceof ItemFrame){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPaintingDestroy2(HangingBreakEvent e){
		if(e.getCause() == RemoveCause.EXPLOSION){
			e.setCancelled(true);
		}
	}
}
