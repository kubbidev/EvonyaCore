package me.kubbidev.evonyacore.game;

import me.kubbidev.evonyacore.players.EvonyaPlayer;
import me.kubbidev.evonyacore.players.Role;

import java.util.UUID;

public class Tracker {

    private final UUID uniqueId;
    private final String username;

    private int kills;
    private double damage;

    private Role role;

    public Tracker(EvonyaPlayer player) {
        this.uniqueId = player.getUniqueId();
        this.username = player.getUsername();

        this.kills = 0;
        this.damage = 0;

        this.role = null;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getUsername() {
        return username;
    }

    public int getKills() {
        return kills;
    }

    public void addKills() {
        this.kills++;
    }

    public double getDamage() {
        return damage;
    }

    public void addDamage(double damage) {
        this.damage += damage;
    }

    public Role getGameRole() {
        return role;
    }

    public void setGameRole(Role role) {
        this.role = role;
    }
}
