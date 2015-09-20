package Trubby.co.th;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.ScoreboardManager;

import Trubby.co.th.SQL.SqlManager;
import Trubby.co.th.Utils.ClearUtil;
import Trubby.co.th.Utils.GMHook;
import Trubby.co.th.chest.ChestListener;
import Trubby.co.th.chest.ChestManager;
import Trubby.co.th.chest.ItemDatabase;
import Trubby.co.th.player.Anticheat;
import Trubby.co.th.player.CarListener;
import Trubby.co.th.player.PlayerListener;
import Trubby.co.th.player.PlayerManager;
import Trubby.co.th.rank.Ranking;
import Trubby.co.th.wanted.WantedManager;

import com.shampaggon.crackshot.CSUtility;

public class GTA extends JavaPlugin{
	
	//\u272a
	public static GTA instance;
	public static ScoreboardManager sbm = Bukkit.getScoreboardManager();
	public static PlayerManager pmg = new PlayerManager();
	public static ChestManager cmg = new ChestManager();
	public static SqlManager sql = new SqlManager();
	public static WantedManager wtm = new WantedManager();
	public static StringManager stm = new StringManager();
	public static GMHook gmhook;
	
	public static Economy economy = null;
	public static CSUtility crackshot = new CSUtility(); 
	
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
		Bukkit.getPluginManager().registerEvents(new EntityListener(), this);
		Bukkit.getPluginManager().registerEvents(new ChestListener(), this);
		Bukkit.getPluginManager().registerEvents(new CarListener(), this);
		Bukkit.getPluginManager().registerEvents(new Ranking(), this);
		Bukkit.getPluginManager().registerEvents(new Anticheat(), this);
		
		gmhook = new GMHook(this);
		
		instance = this;
		
		getSql().openConnection();
		
		setupEconomy();
		
		for(Player p : Bukkit.getOnlinePlayers()){
			getPlayerManager().addPlayer(p);
		}
		
		runWantedTask();
		cmg.runChestDelayCheckerTask();
		new ItemDatabase();
		
	}
	
	@Override
	public void onDisable() {
		for(Player p : Bukkit.getOnlinePlayers()){
			getPlayerManager().removePlayer(p);
		}
		
		//getChestManager().forceRestoreAllChest();
		
		getSql().closeConnection();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		
		if(!(sender instanceof Player))return false;
			
		if(label.equalsIgnoreCase("gta") || label.equalsIgnoreCase("g")){
			//permission
			if(!sender.hasPermission("gta.admin")){
				sender.sendMessage(ChatColor.RED + "Nothing happen.");
				return false;
			}
			
			if(args.length >= 1){
				
				if(args[0].equalsIgnoreCase("chestdelay")){
					if(args.length >= 2){
						getChestManager().setChest_delay(Integer.parseInt(args[1]));
						sender.sendMessage("Chest delay set to " + args[1]);
					}else{
						sender.sendMessage("/gta chestdelay [amount]");
					}
				}
				
				else if(args[0].equalsIgnoreCase("chestreset")){
					//TODO remove
					Player p = (Player) sender;
					/*PigZombie pigzombie = (PigZombie) MobsUtil.spawnMob(p.getLocation(), EntityType.PIG_ZOMBIE, 1, 0.0, 0.0, 0.0);
					pigzombie.setAngry(true);
					
					getPlayerManager().addWanted(p);*/
					int i = 0;
					for(Chunk c : p.getWorld().getLoadedChunks()){
						for(BlockState b : c.getTileEntities()){
							if(b instanceof Chest){
								Chest chest = (Chest) b;
								chest.getInventory().clear();
								//getChestManager().breakChest(chest,20L);
								i++;
							}
						}
					}
					p.sendMessage(""+i);
				}
				
				else if(args[0].equalsIgnoreCase("clear")){
					Player p = (Player) sender;
					if(args.length < 2) {
						p.sendMessage("/gta clear car");
						p.sendMessage("/gta clear cop");
					}
					
					else if(args[1].equalsIgnoreCase("car")){
						p.sendMessage("clear " + ClearUtil.clearEntity(p.getWorld(), EntityType.MINECART) + " cars.");
					}
					
					else if(args[1].equalsIgnoreCase("cop")){
						p.sendMessage("clear " + ClearUtil.clearEntity(p.getWorld(), EntityType.PIG_ZOMBIE) + " cops.");
					}
				}
				
				
			}else{
				sender.sendMessage("/gta help");
				return false;
			}
		}
		
		else if(label.equalsIgnoreCase("wanted")){
			Player p = (Player) sender;
			if(args.length < 1) {
				p.sendMessage("/wanted help");
			}
			
			else if(args[0].equalsIgnoreCase("check")){
				getWantedManager().checkWantedLevel(p);
			}
			
			else if(args[0].equalsIgnoreCase("list")){
				getWantedManager().listWanted(p);
			}
			
			else if(args[0].equalsIgnoreCase("help")){
				p.sendMessage("/wanted check");
				p.sendMessage("/wanted list");
			}
		}
		
		return false;
	}
	
	
	//GETTER
	public static ScoreboardManager getScoreboardManager() {
		return sbm;
	}

	public static ChestManager getChestManager() {
		return cmg;
	}

	public static PlayerManager getPlayerManager(){
		return pmg;
	}
	
	public static SqlManager getSql() {
		return sql;
	}
	
	public static WantedManager getWantedManager() {
		return wtm;
	}
	
	public static StringManager getStringManager() {
		return stm;
	}
	
	//TASK
	public void runWantedTask(){
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			
			@Override
			public void run() {
				getWantedManager().runWantedTask();
			}
		}, 0, 100);
	}
	
	//API
	private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }
	
}
