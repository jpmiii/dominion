package com.psygate.minecraft;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerInventoryHelper {
	public static boolean takeFromPlayerInventory(ItemStack stack, Player player) {
		Inventory inv = player.getInventory();
		int amount = 0;
		
		for(ItemStack st : inv.getContents()) {
			if(st != null && stack.getType().equals(st.getType())) {
				amount += st.getAmount();
				if(amount > stack.getAmount()) {
					break;
				}
			}
		}
		
		if(amount < stack.getAmount()) {
			return false;
		} else {
			inv.removeItem(stack);
			return true;
		}
	}
}
