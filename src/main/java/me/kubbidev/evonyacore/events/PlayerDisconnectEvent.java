package me.kubbidev.evonyacore.events;

import me.kubbidev.evonyacore.game.core.GameInstance;
import me.kubbidev.evonyacore.players.EPlayer;
import org.bukkit.event.HandlerList;

public class PlayerDisconnectEvent extends EvonyaPlayerEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    private final GameInstance gameInstance;

    public PlayerDisconnectEvent(EPlayer player, GameInstance gameInstance) {
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
}
