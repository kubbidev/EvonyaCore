package me.kubbidev.evonyacore.game.core.scenario.listeners;

import me.kubbidev.evonyacore.events.PlayerDamageEvent;
import me.kubbidev.evonyacore.game.core.GameInstance;
import me.kubbidev.evonyacore.game.core.scenario.ScenarioListener;
import org.bukkit.event.EventHandler;

public class FireLess extends ScenarioListener {

    public FireLess(GameInstance gameInstance) {
        super(gameInstance);
    }

    @EventHandler
    public void onEntityDamage(PlayerDamageEvent event) {
        final GameInstance gameInstance = event.getGameInstance();

        if (!gameInstance.equals(super.gameInstance))
            return;

        switch (event.getCause()) {
            case FIRE:
            case FIRE_TICK:
            case LAVA:
                event.setCancelled(true);
                break;
            default:
                break;
        }

    }
}
