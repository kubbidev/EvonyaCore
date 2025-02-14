package me.kubbidev.evonyacore.players;

import java.util.UUID;

public class EvonyaStatistic {

    private final UUID uuid;
    private final String username;

    private int kills;
    private int deaths;
    private int games;
    private int wins;

    private double dmgInflicted;
    private double dmgReceived;
    private long timePlayed;

    public EvonyaStatistic(EvonyaPlayer player) {
        this.uuid = player.getUniqueId();
        this.username = player.getUsername();

        this.kills = 0;
        this.deaths = 0;
        this.games = 0;
        this.wins = 0;

        this.dmgInflicted = 0;
        this.dmgReceived = 0;
        this.timePlayed = 0;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void addKill() {
        this.kills++;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void addDeath() {
        this.deaths++;
    }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public void addGame() {
        this.games++;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void addWin() {
        this.wins++;
    }

    public double getDmgInflicted() {
        return dmgInflicted;
    }

    public void setDmgInflicted(double dmgInflicted) {
        this.dmgInflicted = dmgInflicted;
    }

    public void addDmgInflicted(double dmgInflicted) {
        this.dmgInflicted += dmgInflicted;
    }

    public double getDmgReceived() {
        return dmgReceived;
    }

    public void setDmgReceived(double dmgReceived) {
        this.dmgReceived = dmgReceived;
    }

    public void addDmgReceived(double dmgReceived) {
        this.dmgReceived += dmgReceived;
    }

    public long getTimePlayed() {
        return timePlayed;
    }

    public void setTimePlayed(long timePlayed) {
        this.timePlayed = timePlayed;
    }

    public void addTimePlayed() {
        this.timePlayed++;
    }
}

