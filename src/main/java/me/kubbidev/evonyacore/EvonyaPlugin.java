package me.kubbidev.evonyacore;

import me.kubbidev.evonyacore.commands.*;
import me.kubbidev.evonyacore.commands.permission.*;
import me.kubbidev.evonyacore.game.GameManager;
import me.kubbidev.evonyacore.listeners.*;
import me.kubbidev.evonyacore.players.EPlayer;
import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.evonyacore.queue.QueueSystem;
import me.kubbidev.evonyacore.scoreboard.ScoreBoardManager;
import me.kubbidev.evonyacore.storage.EvonyaPlayerProvider;
import me.kubbidev.evonyacore.storage.EvonyaStatisticProvider;
import me.kubbidev.evonyacore.utils.WorldManager;
import me.kubbidev.nexuspowered.plugin.ExtendedJavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;
import java.util.logging.Logger;

public final class EvonyaPlugin extends ExtendedJavaPlugin {

    public static EvonyaPlugin INSTANCE;
    public static Logger LOGGER;

    public static final String PREFIX = "&cEvonya &8•&r ";
    public static final String DISCORD = "https://discord.kubbidev.me";

    public static String prefixed(String message) {
        return PREFIX + " " + message;
    }

    private LobbyFunction lobbyFunction;
    private CityFunction cityFunction;
    private QueueSystem queueSystem;
    private GameManager gameManager;
    private ScoreBoardManager scoreBoardManager;

    @Override
    public void load() {

    }

    @Override
    public void enable() {
        INSTANCE = this;
        LOGGER = getLogger();

        lobbyFunction = new LobbyFunction();
        cityFunction = new CityFunction();
        queueSystem = new QueueSystem(lobbyFunction);
        gameManager = new GameManager(lobbyFunction, queueSystem);
        scoreBoardManager = new ScoreBoardManager(lobbyFunction, cityFunction);

        initListeners();
        initCommands();
        initPluginFile();
    }

    @Override
    public void disable() {
        Bukkit.getOnlinePlayers().forEach(bukkitPlayer -> {
            final EPlayer ePlayer = PlayerManager.wrapPlayer(bukkitPlayer);

            new EvonyaPlayerProvider(bukkitPlayer).saveAccount();
            new EvonyaStatisticProvider(ePlayer).saveStatistic();
        });
        WorldManager.deleteAllWorlds();
    }

    public World getLobby() {
        return lobbyFunction.getLobby();
    }

    public World getCity() {
        return cityFunction.getCity();
    }

    public LobbyFunction getLobbyFunction() {
        return lobbyFunction;
    }

    public CityFunction getCityFunction() {
        return cityFunction;
    }

    public QueueSystem getQueueSystem() {
        return queueSystem;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    private void initListeners() {
        registerListener(new BlockListener(lobbyFunction, cityFunction));
        registerListener(new ChatListener());
        registerListener(new DamageListener());
        registerListener(new DropItemListener(cityFunction));
        registerListener(new InventoryInteractListener(cityFunction));
        registerListener(new ItemInteractListener(lobbyFunction, this));
        registerListener(new JoinListener(scoreBoardManager, lobbyFunction));
        registerListener(new MiscListener());
        registerListener(new MoveListener(lobbyFunction));
        registerListener(new QuitListener(queueSystem));
    }

    private void initCommands() {
        DiscordCommand.register(this);
        HubCommand.register(this);
        MessageCommand.register(this);
        ReplyCommand.register(this);
        getCommand("help").setExecutor(new HelpCommand());
        getCommand("join").setExecutor(new JoinCommand(queueSystem));
        getCommand("quit").setExecutor(new QuitCommand(queueSystem));
        getCommand("lag").setExecutor(new LagCommand());
        getCommand("stats").setExecutor(new StatsCommand(this));
        getCommand("pack").setExecutor(new PackCommand(this));

        getCommand("broadcast").setExecutor(new BroadcastCommand());
        getCommand("heal").setExecutor(new HealCommand());
        getCommand("rank").setExecutor(new RankCommand());
        getCommand("game").setExecutor(new GameCommand(gameManager));
        getCommand("bypass").setExecutor(new BypassCommand());
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("gamemode").setExecutor(new GamemodeCommand());
        getCommand("speed").setExecutor(new SpeedCommand());
        getCommand("time").setExecutor(new TimeCommand());
        getCommand("host").setExecutor(new HostCommand());
        getCommand("pwl").setExecutor(new PreWLCommand());
    }

    private void initPluginFile() {
        final File file = getDataFolder();
        if (!file.exists())
            file.mkdir();
    }
}
