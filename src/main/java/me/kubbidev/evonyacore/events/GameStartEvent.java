package me.kubbidev.evonyacore.events;

import me.kubbidev.evonyacore.game.core.GameInstance;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStartEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final GameInstance gameInstance;

    public GameStartEvent(GameInstance gameInstance) {
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
