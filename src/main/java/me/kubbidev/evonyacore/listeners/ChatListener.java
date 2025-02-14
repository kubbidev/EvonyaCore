package me.kubbidev.evonyacore.listeners;

import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.evonyacore.players.State;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        final EvonyaPlayer player = PlayerManager.wrapEvonyaPlayer(event.getPlayer());

        final String role = player.getPlayerRank().getScoreBoardPrefix();
        final String name = player.getUsername();
        final String message = event.getMessage();

        if (player.getState() == State.DEMONSLAYER_SPECTATOR) {
            player.getGameInstance().getWorldPlayers().stream().filter(evonyaPlayer -> !player.getGameInstance().getPlayers().contains(evonyaPlayer))
                    .forEach(evonyaPlayer -> evonyaPlayer.sendMessage(Color.translate("&7&lSPEC&7 " + name + "&8 » &7") + message));
        }
        else player.getWorld().getPlayers().forEach(onlinePlayer -> onlinePlayer.sendMessage(Color.translate(role + name + "&8 » &7") + message));
    }
}
