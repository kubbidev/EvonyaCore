package me.kubbidev.evonyacore.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

public class Item {

    /*Item material*/
    private final Material material;

    private String name;
    private List<String> lore = new ArrayList<>();
    private int amount = 1;

    private final Map<Enchantment, Integer> enchantments = new HashMap<>();
    private final List<ItemFlag> itemFlags = new ArrayList<>();

    public Item(Material material) {
        this.material = material;
    }

    /**
     * Set item name
     *
     * @param name
     * Item name
     */
    public Item setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Set item lore
     *
     * @param lore
     * Item lore
     */
    public Item setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    /**
     * Set item amount
     *
     * @param amount
     * Item amount
     */
    public Item setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Add item enchantments
     *
     * @param enchantment
     * Item enchantment
     * @param power
     * Item enchantment power
     */
    public Item addEnchantments(Enchantment enchantment, int power) {
        this.enchantments.put(enchantment, power);
        return this;
    }

    /**
     *  Add item flag
     *
     * @param itemFlag
     *  Item flag
     */
    public Item addItemFlags(ItemFlag... itemFlag) {
        this.itemFlags.addAll(Arrays.asList(itemFlag));
        return this;
    }

    /**
     * Build and finalize the item
     *
     * @return new itemStack
     */
    public ItemStack parseItem() {
        final ItemStack itemStack = new ItemStack(this.material, this.amount);
        final ItemMeta itemMeta = itemStack.getItemMeta();

        if (this.name != null) {
            itemMeta.setDisplayName(Color.translate(ChatColor.RESET + this.name));
        }
        if (this.lore != null) {
            itemMeta.setLore(this.lore.stream().map(Color::translate).collect(Collectors.toList()));
        }
        this.itemFlags.forEach(itemMeta::addItemFlags);
        if (!this.enchantments.isEmpty()) {
            this.enchantments.forEach((enchantment, integer) -> itemMeta.addEnchant(enchantment, integer, false));
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}