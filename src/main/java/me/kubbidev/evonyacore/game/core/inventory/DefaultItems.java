package me.kubbidev.evonyacore.game.core.inventory;

import me.kubbidev.evonyacore.utils.Item;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class DefaultItems extends CustomInventory {

    @Override
    public ItemStack[] getContents() {

        return new ItemStack[] {
                new Item(Material.DIAMOND_SWORD).addEnchantments(Enchantment.DAMAGE_ALL, 3).parseItem(),
                new Item(Material.GOLDEN_APPLE).setAmount(16).parseItem(),
                new Item(Material.BOW).addEnchantments(Enchantment.ARROW_DAMAGE, 3).parseItem(),
                new Item(Material.STONE).setAmount(64).parseItem(),
                new Item(Material.LAVA_BUCKET).parseItem(),
                new Item(Material.IRON_PICKAXE).addEnchantments(Enchantment.DIG_SPEED, 3).parseItem(),
                new Item(Material.STONE).setAmount(64).parseItem(),
                new Item(Material.STONE).setAmount(64).parseItem(),
                new Item(Material.WATER_BUCKET).parseItem(),
                new Item(Material.LAVA_BUCKET).parseItem(),
                new Item(Material.WATER_BUCKET).parseItem(),
                new Item(Material.ARROW).setAmount(32).parseItem(),
                new Item(Material.STONE).setAmount(64).parseItem(),
                new Item(Material.STONE).setAmount(64).parseItem()

        };
    }

    @Override
    public ItemStack[] getArmors() {
        return new ItemStack[] {
                new Item(Material.DIAMOND_BOOTS).addEnchantments(Enchantment.PROTECTION_ENVIRONMENTAL, 2).parseItem(),
                new Item(Material.IRON_LEGGINGS).addEnchantments(Enchantment.PROTECTION_ENVIRONMENTAL, 3).parseItem(),
                new Item(Material.DIAMOND_CHESTPLATE).addEnchantments(Enchantment.PROTECTION_ENVIRONMENTAL, 2).parseItem(),
                new Item(Material.DIAMOND_HELMET).addEnchantments(Enchantment.PROTECTION_ENVIRONMENTAL, 2).parseItem()
        };
    }
}