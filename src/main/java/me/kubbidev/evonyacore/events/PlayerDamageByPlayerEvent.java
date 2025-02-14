package me.kubbidev.evonyacore.events;

import me.kubbidev.evonyacore.game.core.GameInstance;
import me.kubbidev.evonyacore.players.EPlayer;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageByPlayerEvent extends PlayerDamageEvent {
    private final EPlayer damager;

    public PlayerDamageByPlayerEvent(EPlayer player, EPlayer damager, EntityDamageEvent.DamageCause cause, double finalDamage, GameInstance gameInstance) {
        super(player, cause, finalDamage, gameInstance);
        this.damager = damager;
    }

    public EPlayer getDamager() {
        return damager;
    }
}
