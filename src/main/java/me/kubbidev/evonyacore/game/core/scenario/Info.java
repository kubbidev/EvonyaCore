package me.kubbidev.evonyacore.game.core.scenario;

import java.util.Arrays;
import java.util.List;

public enum Info {

    TIME_BOMB("Time Bomb", Arrays.asList("Après avoir tué un joueur, son", "loot explosera après X secondes.")),
    FIRE_LESS("Fire Less", Arrays.asList("Vous ne pouvez plus prendre des", "dégats de feu.")),
    BOW_LESS("Bow Less", Arrays.asList("Vous ne pouvez plus utilisez", "votre arc.")),
    SHIELD_LESS("Shield Less", Arrays.asList("Vous ne pouvez plus utilisez", "votre bouclier"));

    private final String name;

    private final List<String> description;

    Info(String name, List<String> description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getDescription() {
        return this.description;
    }
}
