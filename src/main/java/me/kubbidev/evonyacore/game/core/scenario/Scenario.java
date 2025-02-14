package me.kubbidev.evonyacore.game.core.scenario;

import me.kubbidev.evonyacore.game.core.scenario.listeners.BowLess;
import me.kubbidev.evonyacore.game.core.scenario.listeners.FireLess;
import me.kubbidev.evonyacore.game.core.scenario.listeners.TimeBomb;
import me.kubbidev.evonyacore.menu.ScenarioMenu;
import me.kubbidev.evonyacore.menu.panel.host.scenario.TimeBombPanel;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

public enum Scenario {

    TIME_BOMB("timebomb", Material.TNT, TimeBomb.class, Info.TIME_BOMB, TimeBombPanel.class, Settings.TIME_BOMB_DELAY),
    FIRE_LESS("fireless", Material.FLINT_AND_STEEL, FireLess.class, Info.FIRE_LESS),
    BOW_LESS("bowless", Material.BOW, BowLess.class, Info.BOW_LESS);

    private final String key;

    private final Material material;

    private final Class<? extends ScenarioListener> listener;

    private final Info info;

    private Class<? extends ScenarioMenu> optionsPanel;

    private Settings settings;

    Scenario(String key, Material material, Class<? extends ScenarioListener> listener, Info info) {
        this.key = key;
        this.material = material;
        this.listener = listener;
        this.info = info;
    }

    Scenario(String key, Material material, Class<? extends ScenarioListener> listener, Info info, Class<? extends ScenarioMenu> optionsPanel, Settings settings) {
        this(key, material, listener, info);
        this.optionsPanel = optionsPanel;
        this.settings = settings;
    }

    public Info getInfo() {
        return info;
    }

    public String getKey() {
        return key;
    }

    public Material getMaterial() {
        return this.material;
    }

    public Class<? extends ScenarioListener> getListener() {
        return this.listener;
    }

    @Nullable
    public Class<? extends ScenarioMenu> getOptionsPanel() {
        return optionsPanel;
    }

    @Nullable
    public Settings getSettings() {
        return this.settings;
    }
}
