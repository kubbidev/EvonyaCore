package me.kubbidev.evonyacore.menu;

import me.kubbidev.evonyacore.players.EvonyaPlayer;

public final class PlayerMenuUtility {

    private final EvonyaPlayer owner;

    public PlayerMenuUtility(EvonyaPlayer owner) {
        this.owner = owner;
    }

    public EvonyaPlayer getOwner() {
        return owner;
    }
}
