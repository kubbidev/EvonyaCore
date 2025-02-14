package me.kubbidev.evonyacore.queue;

import me.kubbidev.evonyacore.game.core.GameInstance;
import me.kubbidev.evonyacore.players.EPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Queue {

    private final GameInstance gameInstance;
    private final List<EPlayer> players;

    private final UUID uniqueId;

    public Queue(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
        this.players = new ArrayList<>();

        this.uniqueId = gameInstance.getUniqueId();
    }

    public GameInstance getGame() {
        return gameInstance;
    }

    public List<EPlayer> getPlayers() {
        return players;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public void addPlayer(EPlayer player) {
        this.players.add(player);
    }

    public void removePlayer(EPlayer player) {
        this.players.remove(player);
    }

    public boolean containsPlayer(EPlayer player) {
        return this.players.contains(player);
    }

    public void close() {
        this.players.clear();
    }
}
