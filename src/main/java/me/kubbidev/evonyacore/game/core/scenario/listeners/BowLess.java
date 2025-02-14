package me.kubbidev.evonyacore.game.core.scenario.listeners;

import me.kubbidev.evonyacore.game.core.GameInstance;
import me.kubbidev.evonyacore.game.core.scenario.ScenarioListener;
import me.kubbidev.evonyacore.players.EPlayer;
import me.kubbidev.evonyacore.players.PlayerManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;

public class BowLess extends ScenarioListener {

    public BowLess(GameInstance gameInstance) {
        super(gameInstance);
    }

    @EventHandler
    public void onBowShoot(EntityShootBowEvent event) {
        final Entity entity = event.getEntity();

        if (!(entity instanceof Player))
            return;

        final Player bukkitPlayer = (Player) entity;
        final EPlayer player = PlayerManager.wrapPlayer(bukkitPlayer);

        if (player.hasGameInstance()) {
            if (player.getGameInstance().equals(super.gameInstance)) {
                event.setCancelled(true);
            }
        }
    }
}
