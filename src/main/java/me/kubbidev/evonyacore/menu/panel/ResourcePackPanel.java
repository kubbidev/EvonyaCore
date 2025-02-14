package me.kubbidev.evonyacore.menu.panel;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.menu.Menu;
import me.kubbidev.evonyacore.menu.MenuBorder;
import me.kubbidev.evonyacore.menu.PlayerMenuUtility;
import me.kubbidev.nexuspowered.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ResourcePackPanel extends Menu {

    public ResourcePackPanel(PlayerMenuUtility playerMenuUtility, EvonyaPlugin plugin) {
        super(playerMenuUtility, plugin);
    }

    @Override
    public String getMenuName() {
        return "&f(&c!&f) &cTextures pack";
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public boolean isUpdated() {
        return false;
    }

    @Override
    public long getTicksUpdate() {
        return 0;
    }

    @Override
    public MenuBorder getBorder() {
        return MenuBorder.CORNER;
    }

    @Override
    public ItemStack getBorderMaterial() {
        return ItemStackBuilder.of(Material.STAINED_GLASS_PANE).data(7).build();
    }

    @Override
    public void setMenuItems() {
    }

    @Override
    public void updateMenu() {
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
    }
}
