package me.kubbidev.evonyacore.listeners;

import me.kubbidev.evonyacore.LobbyFunction;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.evonyacore.utils.Message;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

    private final LobbyFunction lobbyFunction;

    public MoveListener(LobbyFunction lobbyFunction) {
        this.lobbyFunction = lobbyFunction;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        final Location locFrom = event.getFrom();
        final Location locTo = event.getTo();

        if (locFrom.getBlockX() == locTo.getBlockX() && locFrom.getBlockY() == locTo.getBlockY() && locFrom.getBlockZ() == locTo.getBlockZ())
            return;

        final EvonyaPlayer player = PlayerManager.wrapEvonyaPlayer(event.getPlayer());

        if (locTo.getBlockY() < 0)
            lobbyFunction.lobbyEnter(player);

        if (player.isBypass()) {
            new Message("&cVous êtes actuellement en &abypass&c.").sendActionBar(event.getPlayer());
        }
    }
}
