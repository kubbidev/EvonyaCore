package me.kubbidev.evonyacore.menu;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.game.core.scenario.Scenario;

public abstract class ScenarioMenu extends Menu {

    protected final Scenario scenario;

    public ScenarioMenu(PlayerMenuUtility playerMenuUtility, EvonyaPlugin plugin, Scenario scenario) {
        super(playerMenuUtility, plugin);
        this.scenario = scenario;
    }
}
