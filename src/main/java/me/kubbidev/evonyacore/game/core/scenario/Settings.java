package me.kubbidev.evonyacore.game.core.scenario;

public enum Settings {

    TIME_BOMB_DELAY(30L);

    private Object value;

    Settings(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
