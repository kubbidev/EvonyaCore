package me.kubbidev.evonyacore.game.core;

import me.kubbidev.evonyacore.events.GameStartEvent;
import me.kubbidev.evonyacore.game.Tracker;
import me.kubbidev.evonyacore.game.core.inventory.CustomInventory;
import me.kubbidev.evonyacore.utils.EvonyaSounds;
import me.kubbidev.evonyacore.players.State;
import me.kubbidev.evonyacore.players.Role;
import me.kubbidev.evonyacore.players.EPlayer;
import me.kubbidev.evonyacore.utils.CustomSpawn;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class StartHandler {

    private final GameInstance gameInstance;

    public StartHandler(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    public void startGame() {
        gameInstance.getPlayers().forEach(player -> {

            player.refresh();
            player.setGameMode(GameMode.SURVIVAL);
            player.setState(State.DEMONSLAYER_PLAYING);
            player.playSound(EvonyaSounds.START_GAME);

            if (gameInstance.isStatistics())
                player.getEvonyaStatistic().addGame();
        });
        randomRole();
        randomTeleport();
        giveItems();

        gameInstance.getHost().setHosts(gameInstance.getHost().getHosts() - 1);
        gameInstance.startGameCycle();
        gameInstance.getScenarioManager().registerAllScenarios();

        Bukkit.getPluginManager().callEvent(new GameStartEvent(gameInstance));
    }

    private void randomRole() {
        if (gameInstance.getActiveRoles().isEmpty())
            Arrays.stream(Role.values()).forEach(gameInstance.getActiveRoles()::add);

        Role[] role = gameInstance.getActiveRoles().toArray(new Role[0]);

        for (EPlayer player : gameInstance.getPlayers()) {
            final Role picked = role[(new Random().nextInt(role.length))];
            final Tracker playerTracker = gameInstance.getTracker(player);

            playerTracker.setGameRole(picked);
            ArrayUtils.removeElement(role, picked);

            if (role.length == 0)
                role = gameInstance.getActiveRoles().toArray(new Role[0]);
        }
    }

    private void randomTeleport() {
        Spawn[] spawn = Spawn.values();

        for (EPlayer player : gameInstance.getPlayers()) {
            final Spawn picked = spawn[((new Random().nextInt(spawn.length)))];
            final Location location = picked.getSpawn().getLocation(gameInstance.getWorld());

            player.teleport(location);
            ArrayUtils.removeElement(spawn, picked);

            if (spawn.length == 0)
                spawn = Spawn.values();
        }
    }

    private void giveItems() {
        try {
            final CustomInventory customInventory = gameInstance.getCustomInventory().getDeclaredConstructor().newInstance();

            gameInstance.getPlayers().forEach(player -> {
                player.getInventory().setContents(customInventory.getContents());
                player.getInventory().setArmorContents(customInventory.getArmors());
            });

        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    enum Spawn {

        ONE(new CustomSpawn(0.5, 100, 0.5, 0)),
        TWO(new CustomSpawn(0.5, 100, 0.5, 0)),
        THREE(new CustomSpawn(0.5, 100, 0.5, 0)),
        FOUR(new CustomSpawn(0.5, 100, 0.5, 0)),
        FIVE(new CustomSpawn(0.5, 100, 0.5, 0)),
        SIX(new CustomSpawn(0.5, 100, 0.5, 0)),
        SEVEN(new CustomSpawn(0.5, 100, 0.5, 0)),
        EIGHT(new CustomSpawn(0.5, 100, 0.5, 0)),
        NINE(new CustomSpawn(0.5, 100, 0.5, 0)),
        TEN(new CustomSpawn(0.5, 100, 0.5, 0)),
        ELEVEN(new CustomSpawn(0.5, 100, 0.5, 0)),
        TWELVE(new CustomSpawn(0.5, 100, 0.5, 0)),
        THIRTEEN(new CustomSpawn(0.5, 100, 0.5, 0)),
        FOURTEEN(new CustomSpawn(0.5, 100, 0.5, 0)),
        FIFTEEN(new CustomSpawn(0.5, 100, 0.5, 0)),
        SIXTEEN(new CustomSpawn(0.5, 100, 0.5, 0));

        private final CustomSpawn customSpawn;

        public static final List<Spawn> VALUES;

        static {
            VALUES = new ArrayList<>();
            VALUES.addAll(Arrays.asList(values()));
        }

        Spawn(CustomSpawn customSpawn) {
            this.customSpawn = customSpawn;
        }

        public CustomSpawn getSpawn() {
            return customSpawn;
        }
    }
}