package me.kubbidev.evonyacore.listeners;

import me.kubbidev.evonyacore.CityFunction;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
import me.kubbidev.evonyacore.players.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropItemListener implements Listener {

    private final CityFunction cityFunction;

    public DropItemListener(CityFunction cityFunction) {
        this.cityFunction = cityFunction;
    }

    @EventHandler
    public void playerPlayerDropItem(PlayerDropItemEvent event) {
        final EvonyaPlayer player = PlayerManager.wrapEvonyaPlayer(event.getPlayer());

        if (player.hasGameInstance())
            if (player.getGameInstance().isStarted())
                return;

        if (player.getWorld().equals(cityFunction.getCity()) || player.isBypass())
            return;

        event.setCancelled(true);
    }
}
