package me.kubbidev.evonyacore.game.core.inventory;

import org.bukkit.inventory.ItemStack;

public abstract class CustomInventory {

    public abstract ItemStack[] getContents();

    public abstract ItemStack[] getArmors();
}
