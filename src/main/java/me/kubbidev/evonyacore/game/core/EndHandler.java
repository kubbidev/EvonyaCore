package me.kubbidev.evonyacore.game.core;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.game.GameManager;
import me.kubbidev.evonyacore.game.Tracker;
import me.kubbidev.evonyacore.players.EPlayer;
import me.kubbidev.evonyacore.utils.Fireworks;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.Comparator;

public class EndHandler {

    private final GameManager gameManager;
    private final GameInstance gameInstance;

    public EndHandler(GameInstance gameInstance) {
        this.gameManager = EvonyaPlugin.INSTANCE.getGameManager();
        this.gameInstance = gameInstance;
    }

    public void checkForRemainingPlayer() {
        Bukkit.getServer().getScheduler().runTaskLater(EvonyaPlugin.INSTANCE, () -> {

            if (gameInstance.getGameState() != GameState.STARTED)
                return;
            final int size = gameInstance.getPlayers().size();

            if (!(size == 1 || size == 0))
                return;

            if (size == 1) {
                final EPlayer winner = gameInstance.getPlayers().get(0);
                final Tracker winnerTracker = gameInstance.getTracker(winner);

                this.sendWinMessage(winnerTracker);
                this.launchFireWorks(winnerTracker);

                if (gameInstance.isStatistics())
                    winner.getEvonyaStatistic().addWin();
            }
            gameInstance.setGameState(GameState.ENDED);
            gameInstance.setEnding(true);

            Bukkit.getScheduler().runTaskLater(EvonyaPlugin.INSTANCE, () -> gameManager.deleteGameInstance(gameInstance), 300L);
        }, 60L);
    }

    private void sendWinMessage(Tracker winner) {

        gameInstance.getWorldPlayers().forEach(arenaPlayer -> {

            arenaPlayer.teleport(gameInstance.getSpawnLocation());

            arenaPlayer.sendMessage(" ");
            arenaPlayer.sendMessage("&8┃ &6Fin de la partie - Demon Slayer");
            arenaPlayer.sendMessage(" ");
            arenaPlayer.sendMessage("  &8»&a Victoire de &6" + winner.getUsername() + "&a avec &6" + winner.getKills() + "&a éliminations.");
            arenaPlayer.sendMessage("  &8»&a Son rôle était : " + winner.getGameRole().getPrefix() + "&f.");
            arenaPlayer.sendMessage(" ");

            arenaPlayer.sendMessage("&8┃ &6Top éliminations :");
            gameInstance.getTrackers().stream()
                    .sorted(Comparator.comparingInt(Tracker::getKills).reversed())
                    .limit(3)
                    .forEach(v -> arenaPlayer.sendMessage("  &8•&a " + v.getUsername() + " (" + v.getKills() + " éliminations)"));

            arenaPlayer.sendMessage(" ");
            arenaPlayer.sendMessage("&8┃ &6Top dégats :");
            gameInstance.getTrackers().stream()
                    .sorted(Comparator.comparingDouble(Tracker::getDamage).reversed())
                    .limit(3)
                    .forEach(v -> arenaPlayer.sendMessage("  &8•&a " + v.getUsername() + " (" + new DecimalFormat("#.##").format(v.getDamage()) + " de dégats)"));
            arenaPlayer.sendMessage(" ");
        });
    }

    private void launchFireWorks(Tracker player) {
        new BukkitRunnable() {
            private int timer = 3;

            @Override
            public void run() {
                if (timer == 0) {
                    cancel();
                    return;
                }
                timer--;

                new Fireworks(1)
                        .setLocation(gameInstance.getSpawnLocation())
                        .setColors(player.getGameRole().isDemon() ? org.bukkit.Color.RED : org.bukkit.Color.LIME)
                        .spawnFireworks();
            }
        }.runTaskTimer(EvonyaPlugin.INSTANCE, 1L, 20L);
    }
}