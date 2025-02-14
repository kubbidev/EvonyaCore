package me.kubbidev.evonyacore.threads;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.game.Tracker;
import me.kubbidev.evonyacore.game.core.GameInstance;
import me.kubbidev.evonyacore.players.EPlayer;
import me.kubbidev.evonyacore.scoreboard.FastBoard;
import me.kubbidev.evonyacore.scoreboard.ScoreBoardLayout;
import me.kubbidev.evonyacore.scoreboard.ScoreBoardManager;
import me.kubbidev.evonyacore.scoreboard.ScoreBoardTypes;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class UpdateScoreBoardThread implements Runnable {

    private final ScoreBoardManager scoreBoardManager;
    private final ScoreBoardLayout scoreBoardLayout;
    private final FastBoard fastBoard;
    private final EPlayer player;

    private ScoreBoardTypes scoreBoardTypes;

    public UpdateScoreBoardThread(ScoreBoardManager scoreBoardManager, FastBoard fastBoard) {
        this.scoreBoardManager = scoreBoardManager;
        this.scoreBoardLayout = scoreBoardManager.getScoreBoardLayout();
        this.fastBoard = fastBoard;
        this.player = fastBoard.getPlayer();
        this.scoreBoardTypes = getScoreboardType();

        this.fastBoard.updateLines(scoreBoardLayout.getLines(scoreBoardTypes));
        this.fastBoard.resetObjective();
    }

    private ScoreBoardTypes getScoreboardType() {
        switch (player.getState()) {
            case LOBBY:
                return ScoreBoardTypes.LOBBY;
            case CITY:
                return ScoreBoardTypes.CITY;
            case DEMONSLAYER_LOBBY:
                return ScoreBoardTypes.DEMONSLAYER_LOBBY;
            case DEMONSLAYER_PLAYING:
                return ScoreBoardTypes.DEMONSLAYER_PLAYING;
            case DEMONSLAYER_SPECTATOR:
                return ScoreBoardTypes.DEMONSLAYER_SPECTATOR;
            default:
                throw new RuntimeException("Unable to find player state!");
        }
    }

    @Override
    public void run() {
        if (!this.player.isOnline()) {
            this.fastBoard.delete();
            this.scoreBoardManager.getFastBoards().remove(fastBoard);
            return;
        }
        if (!this.scoreBoardTypes.equals(getScoreboardType())) {
            this.scoreBoardTypes = getScoreboardType();

            this.fastBoard.updateLines(scoreBoardLayout.getLines(scoreBoardTypes));
            this.fastBoard.resetObjective();
        }
        final List<String> lines = scoreBoardLayout.getLines(scoreBoardTypes);
        final List<String> translateLines = new ArrayList<>();

        lines.forEach(line -> translateLines.add(scoreBoardManager.translatePlaceholders(line, this.player)));

        this.fastBoard.updateLines(translateLines);
        this.fastBoard.updateTitle(scoreBoardManager.translatePlaceholders(scoreBoardLayout.getTitle(scoreBoardTypes), player));

        final String header = scoreBoardLayout.getHeader(scoreBoardTypes);
        final String footer = scoreBoardLayout.getFooter();

        this.player.setPlayerListHeaderFooter(
                scoreBoardManager.translatePlaceholders(header, player),
                scoreBoardManager.translatePlaceholders(footer, player));

        scoreBoardManager.getFastBoards().forEach(fastBoard -> {

            final EPlayer player = fastBoard.getPlayer();
            //
            // LOBBY AND CITY PART.
            //
            final World HUB = scoreBoardManager.getLobbyFunction().getLobby();
            final World CITY = scoreBoardManager.getCityFunction().getCity();

            if (this.player.getWorld().equals(HUB) || this.player.getWorld().equals(CITY)) {
                this.fastBoard.getRanks().forEach(team -> {
                    if (!team.hasEntry(player.getUsername()) && Integer.parseInt(team.getName()) == player.getPlayerRank().getValue())
                        team.addEntry(player.getUsername());
                });
                return;
            }
            //
            // DEMON SLAYER PART.
            //
            if (!this.player.hasGameInstance())
                return;

            // NONE IN GAME TEAM
            final GameInstance gameInstance = this.player.getGameInstance();
            final Team spectator = this.fastBoard.getTeam("spectator");

            if (!gameInstance.getPlayers().contains(player)) {
                if (!spectator.hasEntry(player.getUsername()))
                    spectator.addEntry(player.getUsername());
                return;
            }

            // WAITING STATE & PREFIX DISABLED
            if (!gameInstance.isTeamPrefix() || gameInstance.isWaiting()) {
                final Team team = this.fastBoard.getTeam("player");

                if (!team.hasEntry(player.getUsername()))
                    team.addEntry(player.getUsername());
                return;
            }

            // DEMON SLAYER TEAMS PREFIX
            if (gameInstance.isStarted()) {
                final Tracker playerTracker = gameInstance.getTracker(player);

                this.fastBoard.getRoles().forEach(team -> {
                    final String index = String.valueOf(playerTracker.getGameRole().isDemon() ? 0 : 1);

                    if (!team.hasEntry(player.getUsername()) && team.getName().equals(index + playerTracker.getGameRole().getName()))
                        team.addEntry(player.getUsername());
                });
            }
        });
        Bukkit.getScheduler().runTaskLater(EvonyaPlugin.INSTANCE, this, 2L);
    }
}
