package me.kubbidev.evonyacore.events;

import me.kubbidev.evonyacore.players.EvonyaPlayer;
import org.bukkit.event.Event;

public abstract class EvonyaPlayerEvent extends Event {
    private final EvonyaPlayer player;

    public EvonyaPlayerEvent(EvonyaPlayer player) {
        this.player = player;
    }

    public EvonyaPlayer getPlayer() {
        return player;
    }
}
