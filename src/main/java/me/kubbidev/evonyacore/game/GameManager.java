package me.kubbidev.evonyacore.game;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.LobbyFunction;
import me.kubbidev.evonyacore.exceptions.GameDoesNotExistException;
import me.kubbidev.evonyacore.game.core.GameInstance;
import me.kubbidev.evonyacore.game.core.GameState;
import me.kubbidev.evonyacore.players.EPlayer;
import me.kubbidev.evonyacore.queue.QueueSystem;
import me.kubbidev.evonyacore.utils.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameManager {

    private static final List<GameInstance> GAME_INSTANCES = new ArrayList<>();

    private final LobbyFunction lobbyFunction;
    private final QueueSystem queueSystem;

    public static int gameIndex = 0;

    public GameManager(LobbyFunction lobbyFunction, QueueSystem queueSystem) {
        this.lobbyFunction = lobbyFunction;
        this.queueSystem = queueSystem;
    }

    public static List<GameInstance> getGamesInstance() {
        return GAME_INSTANCES;
    }

    public static synchronized GameInstance getGameInstance(EPlayer player) {
        try {
            return getGameInstance(player.getWorld().getUID());
        } catch (GameDoesNotExistException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized GameInstance getGameInstance(World world) {
        try {
            return getGameInstance(world.getUID());
        } catch (GameDoesNotExistException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized GameInstance getGameInstance(UUID uuid) throws GameDoesNotExistException {
        for (GameInstance gameInstance : GAME_INSTANCES) {
            if (gameInstance.getUniqueId().equals(uuid))
                return gameInstance;
        }
        throw new GameDoesNotExistException(String.valueOf(uuid));
    }

    public static boolean hasGameInstance(EPlayer player) {
        return GAME_INSTANCES.stream().anyMatch(gameInstance -> gameInstance.getWorldPlayers().contains(player));
    }

    public void createGameInstance(EPlayer player) {
        final World world = WorldManager.createNewWorld(gameIndex);
        final GameInstance gameInstance = new GameInstance(world, player);

        Bukkit.getScheduler().runTaskLater(EvonyaPlugin.INSTANCE, () -> gameInstance.setGameState(GameState.WAITING), 150L);
    }

    public void deleteGameInstance(GameInstance gameInstance) {
        queueSystem.getQueue(gameInstance).close();

        gameInstance.getWorldPlayers().forEach(lobbyFunction::lobbyEnter);
        gameInstance.close();

        Bukkit.getScheduler().runTaskLater(EvonyaPlugin.INSTANCE, () -> WorldManager.unloadWorld(gameInstance.getWorld()), 20L);
    }
}