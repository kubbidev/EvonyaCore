package me.kubbidev.evonyacore.events;

import me.kubbidev.evonyacore.game.core.GameInstance;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageByPlayerEvent extends PlayerDamageEvent {
    private final EvonyaPlayer damager;

    public PlayerDamageByPlayerEvent(EvonyaPlayer player, EvonyaPlayer damager, EntityDamageEvent.DamageCause cause, double finalDamage, GameInstance gameInstance) {
        super(player, cause, finalDamage, gameInstance);
        this.damager = damager;
    }

    public EvonyaPlayer getDamager() {
        return damager;
    }
}
