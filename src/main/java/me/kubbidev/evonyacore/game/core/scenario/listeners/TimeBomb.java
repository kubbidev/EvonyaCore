package me.kubbidev.evonyacore.game.core.scenario.listeners;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.events.PlayerEliminationEvent;
import me.kubbidev.evonyacore.game.core.GameInstance;
import me.kubbidev.evonyacore.game.core.scenario.Option;
import me.kubbidev.evonyacore.game.core.scenario.ScenarioListener;
import me.kubbidev.evonyacore.game.core.scenario.Settings;
import me.kubbidev.evonyacore.utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Objects;

public class TimeBomb extends ScenarioListener {

    public TimeBomb(GameInstance gameInstance) {
        super(gameInstance);
    }

    @Option(key = Settings.TIME_BOMB_DELAY)
    private long delay;

    @EventHandler
    public void onGameDeath(PlayerEliminationEvent event) {
        final GameInstance gameInstance = event.getGameInstance();
        final Location deathLocation = event.getLocation();

        final World world = deathLocation.getWorld();
        final List<Item> items = event.getDrops();

        if (!gameInstance.equals(super.gameInstance))
            return;

        new BukkitRunnable() {
            long timer = delay;

            @Override
            public void run() {
                items.stream().filter(Objects::nonNull).forEach(item -> {
                    item.setCustomNameVisible(true);
                    item.setCustomName(getColor(timer) + Utils.getFormattedTime(timer));
                });
                if (timer <= 0) {
                    world.playSound(deathLocation, Sound.EXPLODE, 1, 1);
                    items.stream().filter(Objects::nonNull).forEach(Item::remove);
                    this.cancel();
                }
                timer--;
            }
        }.runTaskTimer(EvonyaPlugin.INSTANCE, 1L, 20L);
    }

    private ChatColor getColor(long timer) {
        if (timer > delay / 2) {
            return ChatColor.GREEN;
        }
        if (timer <= delay / 2 && timer > delay / 4) {
            return ChatColor.GOLD;
        }
        if (timer <= delay / 4) {
            return ChatColor.RED;
        }
        throw new RuntimeException("Cannot find color of items timer");
    }
}
