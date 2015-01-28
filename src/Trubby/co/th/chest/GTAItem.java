package Trubby.co.th.chest;

import org.bukkit.inventory.ItemStack;

public class GTAItem {

	private ItemStack item;
    private int amount;
    private int chance;

    public GTAItem(ItemStack item, int amount, int chance) {
        this.item = item;
        this.amount = amount;
        this.chance = chance;
    }
    
    public ItemStack getItem() {
        return item;
    }

    public int getChance() {
        return chance;
    }

	public int getAmount() {
		return amount;
	}
	
}
