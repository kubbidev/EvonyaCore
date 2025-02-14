package me.kubbidev.evonyacore.listeners;

import me.kubbidev.evonyacore.CityFunction;
import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.evonyacore.players.State;
import me.kubbidev.evonyacore.menu.Menu;
import me.kubbidev.evonyacore.players.EPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class InventoryInteractListener implements Listener {

    private final CityFunction cityFunction;

    public InventoryInteractListener(CityFunction cityFunction) {
        this.cityFunction = cityFunction;
    }

    @EventHandler
    public void playerClickInventory(InventoryClickEvent event) {
        final Inventory inventory = event.getClickedInventory();
        final ItemStack current = event.getCurrentItem();

        if (inventory == null)
            return;

        if (current == null)
            return;

        final InventoryHolder holder = inventory.getHolder();
        final EPlayer player = PlayerManager.wrapPlayer((Player) event.getWhoClicked());

        if (inventory.getType() == InventoryType.PLAYER) {

            if (player.getWorld().equals(cityFunction.getCity()))
                return;

            if (!player.getState().equals(State.DEMONSLAYER_PLAYING) && !player.isBypass())
                event.setCancelled(true);

        }
        if (holder instanceof Menu) {
            event.setCancelled(true);

            if (current.getType() == Material.AIR)
                return;

            ((Menu) holder).handleMenu(event);
        }
    }
}
