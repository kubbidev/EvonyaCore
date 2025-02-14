package me.kubbidev.evonyacore.events;

import me.kubbidev.evonyacore.game.core.GameInstance;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;

public class PlayerKillEvent extends EvonyaPlayerEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    private final EvonyaPlayer killer;
    private final Location location;
    private final GameInstance gameInstance;

    public PlayerKillEvent(EvonyaPlayer victim, EvonyaPlayer killer, Location location, GameInstance gameInstance) {
        super(victim);
        this.killer = killer;
        this.location = location;
        this.gameInstance = gameInstance;
    }

    public EvonyaPlayer getKiller() {
        return killer;
    }

    public Location getLocation() {
        return location;
    }

    public GameInstance getGameInstance() {
        return gameInstance;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
