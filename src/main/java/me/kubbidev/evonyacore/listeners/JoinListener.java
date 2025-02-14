package me.kubbidev.evonyacore.listeners;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.LobbyFunction;
import me.kubbidev.evonyacore.events.PlayerConnectionEvent;
import me.kubbidev.evonyacore.game.core.GameInstance;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
import me.kubbidev.evonyacore.scoreboard.ScoreBoardManager;
import me.kubbidev.evonyacore.storage.EvonyaPlayerProvider;
import me.kubbidev.evonyacore.storage.EvonyaStatisticProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final ScoreBoardManager scoreBoardManager;
    private final LobbyFunction lobbyFunction;

    public JoinListener(ScoreBoardManager scoreBoardManager, LobbyFunction lobbyFunction) {
        this.scoreBoardManager = scoreBoardManager;
        this.lobbyFunction = lobbyFunction;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        final Player bukkitPlayer = event.getPlayer();
        final EvonyaPlayer player = new EvonyaPlayerProvider(bukkitPlayer).loadAccount();

        new EvonyaStatisticProvider(player).loadStatistic();

        scoreBoardManager.setupScoreboard(player);
        lobbyFunction.lobbyEnter(player);
    }

    @EventHandler
    public void onGameConnect(PlayerConnectionEvent event) {
        final GameInstance gameInstance = event.getGameInstance();
        final EvonyaPlayer player = event.getPlayer();

        if (gameInstance.getPlayers().size() >= gameInstance.getSlots() && !player.isBypass() && !gameInstance.getCoHost().contains(player)) {
            player.sendMessage(EvonyaPlugin.PREFIX + "Impossible de se &aconnecter&f : la partie est &cpleine&f.");

            event.setCancelled(true);
        }
        switch (gameInstance.getGameState()) {
            case LOADING:
                player.sendMessage(EvonyaPlugin.PREFIX + "Merci de patienter, le serveur &cdémarre&f...");
                break;
            case STARTED:
            case ENDED:
                if (!gameInstance.getWhitelist().contains(player.getUsername()) && !player.isBypass() && !gameInstance.getCoHost().contains(player)) {
                    if (!gameInstance.isSpectate() || !gameInstance.isOpen()) {
                        player.sendMessage(EvonyaPlugin.PREFIX + "Impossible de se &aconnecter&f : spectateurs &cnon autorisés&f.");

                        event.setCancelled(true);
                    }
                }
                break;
            case WAITING:
            case STARTING:
                if (!gameInstance.getWhitelist().contains(player.getUsername()) && !player.isBypass() && !gameInstance.getCoHost().contains(player)) {
                    if (!gameInstance.isOpen()) {
                        if (player.getPreWL() <= 0) {
                            player.sendMessage(EvonyaPlugin.PREFIX + "Impossible de se &aconnecter&f : la &cfile d'attente&f est fermée.");

                            event.setCancelled(true);
                        } else player.setPreWL(player.getPreWL() - 1);
                    }
                }
                break;
        }
    }
}
