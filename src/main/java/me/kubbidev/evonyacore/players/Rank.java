package me.kubbidev.evonyacore.players;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum Rank {
    ADMINISTRATEUR(0, "ADMIN", ChatColor.RED),
    RESPONSABLE(1, "RESP", ChatColor.RED),
    DEVELOPPEUR(2, "DEV", ChatColor.YELLOW),
    MODERATEUR(3, "MOD", ChatColor.BLUE),
    TEAM(4, "TEAM", ChatColor.GOLD),
    GUEST(5, "GUEST", ChatColor.LIGHT_PURPLE),
    /*<!-- AJOUTER PLUS DE GRADE -->*/
    JOUEUR(6, "JOUEUR", ChatColor.GRAY);

    private final int value;
    private final String prefix;
    private final ChatColor color;

    public static final Map<Integer, Rank> VALUES;

    static {
        VALUES = new HashMap<>();
        Arrays.stream(values()).forEach(rank -> VALUES.put(rank.getValue(), rank));
    }

    Rank(int value, String prefix, ChatColor color) {
        this.value = value;
        this.prefix = prefix;
        this.color = color;
    }

    public int getValue() {
        return value;
    }

    public String getPrefix() {
        return getColor() + this.prefix;
    }

    public String getScoreBoardPrefix() {
        return getPrefix() + " ";
    }

    public ChatColor getColor() {
        return color;
    }

    public boolean isLowerThan(Rank rank) {
        return this.getValue() > rank.getValue();
    }

    public boolean isHigherThan(Rank rank) {
        return this.getValue() < rank.getValue();
    }

    public static Rank getRankByValue(int value) {
        return VALUES.get(value);
    }
}