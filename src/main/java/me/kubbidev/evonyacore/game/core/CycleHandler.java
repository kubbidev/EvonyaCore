package me.kubbidev.evonyacore.game.core;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.game.GameManager;
import me.kubbidev.evonyacore.game.Tracker;
import me.kubbidev.evonyacore.utils.EvonyaSounds;
import me.kubbidev.evonyacore.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.Comparator;

public class CycleHandler extends BukkitRunnable {

    private final GameManager gameManager;
    private final GameInstance gameInstance;

    public CycleHandler(GameInstance gameInstance) {
        this.gameManager = EvonyaPlugin.INSTANCE.getGameManager();
        this.gameInstance = gameInstance;
    }

    @Override
    public void run() {
        if (gameInstance.isEnding()) {
            cancel();
            return;
        }

        if (gameInstance.isStatistics())
            gameInstance.getPlayers().forEach(onlinePlayer -> onlinePlayer.getEvonyaStatistic().addTimePlayed());

        final long elapsed = gameInstance.getElapsedTime();
        final long max = gameInstance.getMaxGameTime();

        if (elapsed == (max - 60) || elapsed == (max - 30) || elapsed == (max - 10) || elapsed == (max - 5) || elapsed == (max - 3) || elapsed == (max - 2) || elapsed == (max - 1)) {
            gameInstance.getWorldPlayers().forEach(evonyaPlayer -> evonyaPlayer.sendMessage(EvonyaPlugin.PREFIX + "Fin du jeu dans &a" + Utils.getFormattedTime((max - elapsed)) + "&f."));
        }

        if (elapsed >= max) {
            gameInstance.setGameState(GameState.ENDED);
            gameInstance.getWorldPlayers().forEach(arenaPlayer -> {

                arenaPlayer.teleport(gameInstance.getSpawnLocation());

                arenaPlayer.sendMessage(" ");
                arenaPlayer.sendMessage("&8┃ &6Fin de la partie - Demon Slayer");
                arenaPlayer.sendMessage(" ");
                arenaPlayer.sendMessage("  &8»&a Temps écoulé !");
                arenaPlayer.sendMessage("  &8»&a Aucun gagnant n'a été détecté.");
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
                arenaPlayer.playSound(EvonyaSounds.DEATH);
            });
            Bukkit.getScheduler().runTaskLater(EvonyaPlugin.INSTANCE, () -> gameManager.deleteGameInstance(gameInstance), 300L);
            cancel();
            return;
        }
        gameInstance.setElapsedTime(gameInstance.getElapsedTime() + 1);
    }
}
