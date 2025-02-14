package me.kubbidev.evonyacore.events;

import me.kubbidev.evonyacore.game.core.GameInstance;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerConnectionEvent extends EvonyaPlayerEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final GameInstance gameInstance;
    private boolean cancelled;

    public PlayerConnectionEvent(EvonyaPlayer player, GameInstance gameInstance) {
        super(player);
        this.gameInstance = gameInstance;
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

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
