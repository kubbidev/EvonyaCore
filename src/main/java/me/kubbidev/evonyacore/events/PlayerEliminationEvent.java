package me.kubbidev.evonyacore.events;

import me.kubbidev.evonyacore.game.core.GameInstance;
import me.kubbidev.evonyacore.players.EPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.event.HandlerList;

import java.util.List;

public class PlayerEliminationEvent extends EvonyaPlayerEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    private final List<Item> drops;
    private final Location location;
    private final GameInstance gameInstance;

    public PlayerEliminationEvent(EPlayer player, List<Item> drops, Location location, GameInstance gameInstance) {
        super(player);
        this.drops = drops;
        this.location = location;
        this.gameInstance = gameInstance;
    }

    public List<Item> getDrops() {
        return drops;
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
