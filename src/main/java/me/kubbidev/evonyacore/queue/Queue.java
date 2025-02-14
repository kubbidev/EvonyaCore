package me.kubbidev.evonyacore.queue;

import me.kubbidev.evonyacore.game.core.GameInstance;
import me.kubbidev.evonyacore.players.EvonyaPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Queue {

    private final GameInstance gameInstance;
    private final List<EvonyaPlayer> players;

    private final UUID uniqueId;

    public Queue(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
        this.players = new ArrayList<>();

        this.uniqueId = gameInstance.getUniqueId();
    }

    public GameInstance getGame() {
        return gameInstance;
    }

    public List<EvonyaPlayer> getPlayers() {
        return players;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public void addPlayer(EvonyaPlayer player) {
        this.players.add(player);
    }

    public void removePlayer(EvonyaPlayer player) {
        this.players.remove(player);
    }

    public boolean containsPlayer(EvonyaPlayer player) {
        return this.players.contains(player);
    }

    public void close() {
        this.players.clear();
    }
}
