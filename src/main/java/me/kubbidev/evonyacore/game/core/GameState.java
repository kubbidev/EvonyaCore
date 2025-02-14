package me.kubbidev.evonyacore.game.core;

public enum GameState {

    LOADING("&9En préparation..."),
    WAITING("&aEn attente"),
    STARTING("&6Démarrage"),
    STARTED("&cPartie en cours"),
    ENDED("&cPartie terminée");

    private final String name;

    GameState(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
