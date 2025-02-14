package me.kubbidev.evonyacore.events;

import me.kubbidev.evonyacore.game.core.GameInstance;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageEvent extends EvonyaPlayerEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final EntityDamageEvent.DamageCause cause;
    private final GameInstance gameInstance;
    private final double finalDamage;
    private boolean cancelled;

    public PlayerDamageEvent(EvonyaPlayer player, EntityDamageEvent.DamageCause cause, double finalDamage, GameInstance gameInstance) {
        super(player);
        this.cause = cause;
        this.finalDamage = finalDamage;
        this.gameInstance = gameInstance;
    }

    public EntityDamageEvent.DamageCause getCause() {
        return cause;
    }

    public double getFinalDamage() {
        return finalDamage;
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
