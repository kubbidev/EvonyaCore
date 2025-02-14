package me.kubbidev.evonyacore.listeners;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.events.PlayerDamageByPlayerEvent;
import me.kubbidev.evonyacore.events.PlayerDamageEvent;
import me.kubbidev.evonyacore.events.PlayerEliminationEvent;
import me.kubbidev.evonyacore.events.PlayerKillEvent;
import me.kubbidev.evonyacore.exceptions.TrackerDoesNotExistException;
import me.kubbidev.evonyacore.game.Tracker;
import me.kubbidev.evonyacore.game.core.GameInstance;
import me.kubbidev.evonyacore.game.core.GameState;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.evonyacore.players.State;
import me.kubbidev.evonyacore.utils.EvonyaSounds;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DamageListener implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        final Entity victim = event.getEntity();
        final Entity damager = event.getDamager();

        final Player bukkitPlayer = (Player) victim;
        final EvonyaPlayer player = PlayerManager.wrapEvonyaPlayer(bukkitPlayer);

        if (player.hasGameInstance())
            return;

        final EntityDamageEvent.DamageCause cause = event.getCause();
        final GameInstance gameInstance = player.getGameInstance();

        if (gameInstance.getGameState() != GameState.STARTED)
            return;

        Player bukkitKiller = null;

        final double damage = event.getFinalDamage();
        final boolean statistic = gameInstance.isStatistics();

        if (damager instanceof Player) {
            bukkitKiller = (Player) damager;
        } else if (damager instanceof Arrow) {
            final Arrow arrow = (Arrow) damager;

            if (arrow.getShooter() instanceof Player) {
                bukkitKiller = (Player) arrow.getShooter();
            }
        }
        final EvonyaPlayer killer = PlayerManager.wrapEvonyaPlayer(bukkitKiller);
        final UUID killerUUID = killer.getUniqueId();

        if (!player.isPlaying() || !killer.isPlaying()) {
            killer.sendMessage(EvonyaPlugin.PREFIX + "Vous ne pouvez pas tuer &a" + player.getUsername() + "&f car vous n'êtes pas de la &cpartie&f.");
            event.setCancelled(true);
            return;
        }

        final PlayerDamageByPlayerEvent e = new PlayerDamageByPlayerEvent(player, killer, cause, damage, gameInstance);
        final Location location = player.getLocation();

        Bukkit.getPluginManager().callEvent(e);
        if (e.isCancelled()) {
            event.setCancelled(true);
            return;
        }

        if (statistic) {
            player.getEvonyaStatistic().addDmgReceived(damage);
            killer.getEvonyaStatistic().addDmgInflicted(damage);
        }
        try {
            gameInstance.getTracker(killerUUID).addDamage(damage);
        } catch (TrackerDoesNotExistException ignored) {
        }
        if (player.getHealth() <= damage) {
            if (statistic) {
                killer.getEvonyaStatistic().addKill();
            }
            try {
                gameInstance.getTracker(killerUUID).addKills();
            } catch (TrackerDoesNotExistException ignored) {
            }
            Bukkit.getPluginManager().callEvent(new PlayerKillEvent(player, killer, location, gameInstance));
        }
    }

    @EventHandler
    public void onDamageEvent(EntityDamageEvent event) {
        final Entity victim = event.getEntity();

        if ((!(victim instanceof Player)))
            return;

        final Player bukkitPlayer = (Player) victim;
        final EvonyaPlayer player = PlayerManager.wrapEvonyaPlayer(bukkitPlayer);

        if (player.hasGameInstance()) {

            final GameInstance gameInstance = player.getGameInstance();
            final EntityDamageEvent.DamageCause cause = event.getCause();

            if (gameInstance.getGameState() == GameState.STARTED) {

                if (!player.isPlaying()) {
                    player.sendMessage(EvonyaPlugin.PREFIX + "Vous ne pouvez pas prendre de &cdégats&f car vous n'êtes pas de la &cpartie&f.");
                    event.setCancelled(true);
                    return;
                }
                final double damage = event.getFinalDamage();
                final boolean statistic = gameInstance.isStatistics();

                final PlayerDamageEvent e = new PlayerDamageEvent(player, cause, damage, gameInstance);

                Bukkit.getPluginManager().callEvent(e);
                if (e.isCancelled()) {
                    event.setCancelled(true);
                    return;
                }

                if (player.getHealth() <= damage) {
                    event.setDamage(0);

                    final Tracker playerTracker = gameInstance.getTracker(player);
                    final Location location = victim.getLocation();

                    final List<Item> drops = new ArrayList<>();
                    final Location deathLocation = victim.getLocation().clone().add(0, 1, 0);

                    for (ItemStack itemStack : player.getInventory().getContents()) {
                        if (itemStack == null)
                            continue;
                        drops.add(victim.getWorld().dropItemNaturally(deathLocation, itemStack));
                    }

                    if (statistic)
                        player.getEvonyaStatistic().addDeath();

                    player.setState(State.DEMONSLAYER_SPECTATOR);
                    player.setGameMode(GameMode.SPECTATOR);
                    player.setAllowFlight(true);

                    gameInstance.getWorldPlayers().forEach((arenaPlayer) -> {

                        arenaPlayer.sendMessage(" ");
                        arenaPlayer.sendMessage(" &8┃ &a" + playerTracker.getUsername() + "&f est mort.");
                        arenaPlayer.sendMessage(" &8┃ &fSon rôle était : " + playerTracker.getGameRole().getPrefix() + "&f.");
                        arenaPlayer.sendMessage(" ");
                        arenaPlayer.playSound(EvonyaSounds.DEATH);
                    });
                    gameInstance.getPlayers().remove(player);
                    gameInstance.getEndHandler().checkForRemainingPlayer();

                    Bukkit.getPluginManager().callEvent(new PlayerEliminationEvent(player, drops, location, gameInstance));
                }
                return;
            }
        }
        event.setCancelled(true);
    }
}