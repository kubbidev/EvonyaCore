package me.kubbidev.evonyacore.menu;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.nexuspowered.item.ItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public abstract class Menu implements InventoryHolder {

    protected Inventory inventory;

    protected final PlayerMenuUtility playerMenuUtility;
    protected final EvonyaPlugin plugin;

    public Menu(PlayerMenuUtility playerMenuUtility, EvonyaPlugin plugin) {
        this.playerMenuUtility = playerMenuUtility;
        this.plugin = plugin;
    }

    public abstract String getMenuName();

    public abstract int getRows();

    public abstract boolean isUpdated();

    public abstract long getTicksUpdate();

    public abstract MenuBorder getBorder();

    public abstract ItemStack getBorderMaterial();

    public abstract void setMenuItems();

    public abstract void updateMenu();

    public abstract void handleMenu(InventoryClickEvent event);

    public void open() {

        inventory = Bukkit.createInventory(this, (getRows() * 9), Color.translate(getMenuName()));

        this.setBorder();
        this.setMenuItems();
        this.initUpdate();

        playerMenuUtility.getOwner().openInventory(inventory);
    }

    public void initUpdate() {
        if (isUpdated()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    updateMenu();

                    if (inventory.getViewers().isEmpty())
                        cancel();
                }
            }.runTaskTimer(plugin, 1L, getTicksUpdate());
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setBorder() {
        if (getBorder() == MenuBorder.CORNER)
            addCornerBorder();
        else addLineBorder();
    }

    private void addCornerBorder() {
        final int rows = getRows();
        final List<Integer> corners = new ArrayList<>(Arrays.asList(0, 1, 7, 8, 9, 17));

        switch (rows) {
            case 3:
                IntStream.of(18, 19, 25, 26).forEach(corners::add);
                break;
            case 4:
                IntStream.of(18, 27, 28, 26, 35, 34).forEach(corners::add);
                break;
            case 5:
                IntStream.of(27, 36, 37, 35, 43, 44).forEach(corners::add);
                break;
            case 6:
                IntStream.of(36, 45, 46, 44, 53, 52).forEach(corners::add);
                break;
        }
        corners.forEach(i -> inventory.setItem(i, ItemStackBuilder.of(getBorderMaterial()).name("").build()));
    }

    private void addLineBorder() {
        final int rows = getRows();
        final int slots = (rows * 9) - 1;
        final List<Integer> lines = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));

        if (rows > 4 || rows == 3) {
            IntStream.rangeClosed(9, 17).forEach(lines::add);
        }
        IntStream.rangeClosed((slots - 8), slots).forEach(lines::add);
        lines.forEach(i -> inventory.setItem(i, ItemStackBuilder.of(getBorderMaterial()).name("").build()));
    }
}
