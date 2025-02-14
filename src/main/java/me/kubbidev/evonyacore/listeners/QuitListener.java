package me.kubbidev.evonyacore.listeners;

import me.kubbidev.evonyacore.events.PlayerDisconnectEvent;
import me.kubbidev.evonyacore.game.Tracker;
import me.kubbidev.evonyacore.game.core.GameInstance;
import me.kubbidev.evonyacore.game.core.GameState;
import me.kubbidev.evonyacore.players.EPlayer;
import me.kubbidev.evonyacore.players.State;
import me.kubbidev.evonyacore.queue.QueueSystem;
import me.kubbidev.evonyacore.storage.EvonyaPlayerProvider;
import me.kubbidev.evonyacore.storage.EvonyaStatisticProvider;
import me.kubbidev.evonyacore.utils.EvonyaSounds;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.stream.Collectors;

public class QuitListener implements Listener {

    private final QueueSystem queueSystem;

    public QuitListener(QueueSystem queueSystem) {
        this.queueSystem = queueSystem;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        final Player bukkitPlayer = event.getPlayer();
        final EPlayer player = new EvonyaPlayerProvider(bukkitPlayer).saveAccount();

        new EvonyaStatisticProvider(player).saveStatistic();

        if (player.hasGameInstance())
            Bukkit.getPluginManager().callEvent(new PlayerDisconnectEvent(player, player.getGameInstance()));

        if (queueSystem.isQueue(player))
            queueSystem.getQueue(player).removePlayer(player);

        player.refresh();
        player.setGameMode(GameMode.ADVENTURE);
        player.setState(State.LOBBY);
    }

    @EventHandler
    public void onDemonSlayerQuit(PlayerDisconnectEvent event) {
        final GameInstance gameInstance = event.getGameInstance();
        final EPlayer player = event.getPlayer();
        final List<EPlayer> worldPlayers = gameInstance.getWorldPlayers().stream().filter(p -> !p.equals(player)).collect(Collectors.toList());

        gameInstance.getPlayers().remove(player);
        if (gameInstance.getGameState() == GameState.WAITING || gameInstance.getGameState() == GameState.STARTING)
            worldPlayers.forEach(players -> players.sendMessage(
                    "&a" + player.getUsername() + "&f a quitté la partie &8[&e" + gameInstance.getPlayers().size() + "&7/&e" + gameInstance.getSlots() + "&8]"));

        if (gameInstance.getGameState() == GameState.STARTED) {
            if (gameInstance.isStatistics())
                player.getEvonyaStatistic().addDeath();

            final Tracker playerTracker = gameInstance.getTracker(player);

            gameInstance.getEndHandler().checkForRemainingPlayer();
            worldPlayers.forEach((arenaPlayer) -> {

                arenaPlayer.sendMessage(" ");
                arenaPlayer.sendMessage("&8┃ &a" + playerTracker.getUsername() + "&f est mort.");
                arenaPlayer.sendMessage("&8┃ &fSon rôle était : " + playerTracker.getGameRole().getPrefix() + "&f.");
                arenaPlayer.sendMessage(" ");
                arenaPlayer.playSound(EvonyaSounds.DEATH);
            });
        }
    }
}
