package me.kubbidev.evonyacore.menu;

import me.kubbidev.evonyacore.players.EPlayer;

public final class PlayerMenuUtility {

    private final EPlayer owner;

    public PlayerMenuUtility(EPlayer owner) {
        this.owner = owner;
    }

    public EPlayer getOwner() {
        return owner;
    }
}
