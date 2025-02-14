package me.kubbidev.evonyacore.events;

import me.kubbidev.evonyacore.players.EPlayer;
import org.bukkit.event.Event;

public abstract class EvonyaPlayerEvent extends Event {
    private final EPlayer player;

    public EvonyaPlayerEvent(EPlayer player) {
        this.player = player;
    }

    public EPlayer getPlayer() {
        return player;
    }
}
