package me.kubbidev.evonyacore.players;

import me.kubbidev.evonyacore.exceptions.EvonyaPlayerDoesNotExistException;
import me.kubbidev.evonyacore.exceptions.EvonyaStatisticDoesNotExistException;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PlayerManager {

    private static final List<EvonyaPlayer> EVONYA_PLAYERS = Collections.synchronizedList(new ArrayList<>());
    private static final List<EvonyaStatistic> EVONYA_STATISTICS = Collections.synchronizedList(new ArrayList<>());

    public static List<EvonyaPlayer> getEvonyaPlayers() {
        return EVONYA_PLAYERS;
    }

    public static List<EvonyaStatistic> getEvonyaStatistics() {
        return EVONYA_STATISTICS;
    }

    public static synchronized EvonyaPlayer wrapEvonyaPlayer(Player player) {
        try {
            return wrapEvonyaPlayer(player.getUniqueId());
        } catch (EvonyaPlayerDoesNotExistException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static synchronized EvonyaPlayer wrapEvonyaPlayer(UUID uuid) throws EvonyaPlayerDoesNotExistException {
        for (EvonyaPlayer evonyaPlayer : EVONYA_PLAYERS) {
            if (evonyaPlayer.getUniqueId().equals(uuid))
                return evonyaPlayer;
        }
        throw new EvonyaPlayerDoesNotExistException(uuid.toString());
    }

    public static synchronized EvonyaPlayer wrapEvonyaPlayer(String username) throws EvonyaPlayerDoesNotExistException {
        for (EvonyaPlayer evonyaPlayer : EVONYA_PLAYERS) {
            if (evonyaPlayer.getUsername().equals(username))
                return evonyaPlayer;
        }
        throw new EvonyaPlayerDoesNotExistException(username);
    }

    public static synchronized boolean hasEvonyaPlayer(Player player) {
        return EVONYA_PLAYERS.stream().anyMatch(account -> account.getUniqueId().equals(player.getUniqueId()));
    }

    public static synchronized EvonyaStatistic wrapEvonyaStatistic(EvonyaPlayer player) {
        try {
            return wrapEvonyaStatistic(player.getUniqueId());
        } catch (EvonyaStatisticDoesNotExistException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized EvonyaStatistic wrapEvonyaStatistic(UUID uuid) throws EvonyaStatisticDoesNotExistException {
        for (EvonyaStatistic EvonyaStatistic : EVONYA_STATISTICS) {
            if (EvonyaStatistic.getUuid().equals(uuid))
                return EvonyaStatistic;
        }
        throw new EvonyaStatisticDoesNotExistException(uuid.toString());
    }

    public static synchronized boolean hasEvonyaStatistic(EvonyaPlayer player) {
        return EVONYA_STATISTICS.stream().anyMatch(statistic -> statistic.getUuid().equals(player.getUniqueId()));
    }
}
