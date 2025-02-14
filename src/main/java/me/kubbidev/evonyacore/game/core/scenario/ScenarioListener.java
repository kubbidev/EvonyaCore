package me.kubbidev.evonyacore.game.core.scenario;

import me.kubbidev.evonyacore.game.core.GameInstance;
import me.kubbidev.evonyacore.game.core.ScenarioManager;
import org.bukkit.event.Listener;

public abstract class ScenarioListener implements Listener {

    protected final GameInstance gameInstance;

    public ScenarioListener(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    public ScenarioManager getScenarioManager() {
        return gameInstance.getScenarioManager();
    }

    public boolean isEnabled(Scenario scenario) {
        return getScenarioManager().isEnabled(scenario);
    }

    public void onEnable() {}

    public void onDisable() {}
}
