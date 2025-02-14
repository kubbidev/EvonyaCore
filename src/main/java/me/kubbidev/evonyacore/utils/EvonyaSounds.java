package me.kubbidev.evonyacore.utils;

import org.bukkit.Sound;

public enum EvonyaSounds {

    DEATH(Sound.WITHER_SPAWN),
    START_GAME(Sound.CLICK),
    SUCCESSFULLY(Sound.NOTE_PLING);

    private final Sound sound;

    private final float volume;
    private final float pitch;

    EvonyaSounds(Sound sound) {
        this.sound = sound;
        this.volume = (float) 1.0;
        this.pitch = (float) 1.0;
    }

    public Sound getSound() {
        return sound;
    }

    public float getVolume() {
        return volume;
    }

    public float getPitch() {
        return pitch;
    }
}
