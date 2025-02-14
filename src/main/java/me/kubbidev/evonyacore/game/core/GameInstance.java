package me.kubbidev.evonyacore.game.core;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.exceptions.TrackerDoesNotExistException;
import me.kubbidev.evonyacore.game.GameManager;
import me.kubbidev.evonyacore.game.Tracker;
import me.kubbidev.evonyacore.game.core.inventory.CustomInventory;
import me.kubbidev.evonyacore.game.core.inventory.EvonyaInventory;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.evonyacore.players.Role;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public class GameInstance {

    private final World world;
    private final EvonyaPlayer host;

    private final UUID uniqueId;
    private final int id;
    private final List<EvonyaPlayer> coHost;

    private final List<EvonyaPlayer> players;
    private final List<String> whitelist;
    private final List<Role> activeRoles;
    private final List<Location> placedBlock;

    private final StartHandler startHandler;
    private final EndHandler endHandler;
    private final ConnectionUtils connectionUtils;
    private final CycleHandler cycleHandler;
    private final ScenarioManager scenarioManager;
    /*
     * Variable
     */
    private final List<Tracker> trackers;
    private final Class<? extends CustomInventory> customInventory;

    private GameState gameState;
    private boolean open;
    private int slots;
    private boolean statistics;
    private boolean spectate;
    private boolean teamPrefix;
    private long elapsedTime;
    private long maxGameTime;
    private long startingTime;

    private boolean starting;
    private boolean ending;

    /**
     * Hosted Games
     *
     * @param host
     * the actual host of the game
     */
    public GameInstance(World world, EvonyaPlayer host) {
        this.world = world;
        this.host = host;

        this.uniqueId = world.getUID();
        this.id = GameManager.gameIndex;
                  GameManager.gameIndex++;

        this.coHost = new ArrayList<>(Collections.singleton(host));

        this.players = new ArrayList<>();
        this.whitelist = new ArrayList<>(Collections.singleton(host.getUsername()));
        this.activeRoles = new ArrayList<>(Arrays.asList(Role.values()));
        this.placedBlock = new ArrayList<>();

        this.startHandler = new StartHandler(this);
        this.endHandler = new EndHandler(this);
        this.connectionUtils = new ConnectionUtils(this);
        this.cycleHandler = new CycleHandler(this);
        this.scenarioManager = new ScenarioManager(this);

        this.trackers = new ArrayList<>();
        this.customInventory = EvonyaInventory.DEFAULT_ITEM.getCustomInventory();

        this.gameState = GameState.LOADING;
        this.open = false;
        this.slots = 16;
        this.statistics = true;
        this.spectate = false;
        this.teamPrefix = false;
        this.elapsedTime = 0;
        this.maxGameTime = 600;
        this.startingTime = 5;

        this.starting = false;
        this.ending = false;

        GameManager.getGamesInstance().add(this);
    }

    public void startTimer() {
        setGameState(GameState.STARTING);
        setStarting(true);
        getWorldPlayers().forEach(evonyaPlayer -> {
            evonyaPlayer.getInventory().clear();
            evonyaPlayer.closeInventory();

            getTrackers().add(new Tracker(evonyaPlayer));
        });
        new BukkitRunnable() {
            private long timer = startingTime;

            @Override
            public void run() {
                getPlayers().forEach(player -> player.setExp((float) timer / startingTime));
                if (timer <= 0) {
                    getStartHandler().startGame();
                    cancel();
                }
                timer--;
            }
        }.runTaskTimer(EvonyaPlugin.INSTANCE, 1, 20);
    }

    public void startGameCycle() {
        this.gameState = GameState.STARTED;
        this.cycleHandler.runTaskTimer(EvonyaPlugin.INSTANCE, 1L, 20L);
    }

    public Location getSpawnLocation() {
        return new Location(getWorld(), 0.5, 59, 0.5, 0, 0);
    }

    public World getWorld() {
        return world;
    }

    public EvonyaPlayer getHost() {
        return host;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public int getId() {
        return id;
    }

    public List<EvonyaPlayer> getCoHost() {
        return coHost;
    }

    public List<EvonyaPlayer> getPlayers() {
        return players;
    }

    public List<String> getWhitelist() {
        return whitelist;
    }
    
    public List<Role> getActiveRoles() {
		return activeRoles;
	}

    public Class<? extends CustomInventory> getCustomInventory() {
        return customInventory;
    }

    public List<Location> getPlacedBlock() {
        return placedBlock;
    }

    public StartHandler getStartHandler() {
        return startHandler;
    }

    public EndHandler getEndHandler() {
        return endHandler;
    }

    public ConnectionUtils getConnectionUtils() {
        return connectionUtils;
    }



    public ScenarioManager getScenarioManager() {
        return scenarioManager;
    }

    public List<Tracker> getTrackers() {
        return trackers;
    }

    public GameState getGameState() {
        return gameState;
    }

    public boolean isStarted() {
        return (gameState == GameState.STARTED || gameState == GameState.ENDED);
    }

    public boolean isWaiting() {
        return (gameState == GameState.WAITING || gameState == GameState.STARTING);
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public boolean isStatistics() {
        return statistics;
    }

    public void setStatistics(boolean statistics) {
        this.statistics = statistics;
    }

    public boolean isSpectate() {
        return spectate;
    }

    public void setSpectate(boolean spectate) {
        this.spectate = spectate;
    }

    public boolean isTeamPrefix() {
        return teamPrefix;
    }

    public void setTeamPrefix(boolean teamPrefix) {
        this.teamPrefix = teamPrefix;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public long getMaxGameTime() {
        return maxGameTime;
    }

    public void setMaxGameTime(long maxGameTime) {
        this.maxGameTime = maxGameTime;
    }

    public long getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(long startingTime) {
        this.startingTime = startingTime;
    }

    public boolean isStarting() {
        return starting;
    }

    public void setStarting(boolean starting) {
        this.starting = starting;
    }

    public boolean isEnding() {
        return ending;
    }

    public void setEnding(boolean ending) {
        this.ending = ending;
    }

    // -----| Utils Methods |----------------------------------------------------------------------------------------------------------------

    public List<EvonyaPlayer> getWorldPlayers() {
        return world.getPlayers().stream().map(PlayerManager::wrapEvonyaPlayer).collect(Collectors.toList());
    }

    public Tracker getTracker(EvonyaPlayer player) {
        try {
            return getTracker(player.getUniqueId());
        } catch (TrackerDoesNotExistException e) {
            throw new RuntimeException(e);
        }
    }

    public Tracker getTracker(UUID uuid) throws TrackerDoesNotExistException {
        for (Tracker tracker : trackers) {
            if (uuid.equals(tracker.getUniqueId()))
                return tracker;
        }
        throw new TrackerDoesNotExistException(uuid.toString());
    }

    public void close() {
        this.scenarioManager.disableAllScenarios();
        this.players.clear();
        GameManager.getGamesInstance().remove(this);
    }
}