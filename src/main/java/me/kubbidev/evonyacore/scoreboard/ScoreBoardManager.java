package me.kubbidev.evonyacore.scoreboard;

import me.kubbidev.evonyacore.CityFunction;
import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.LobbyFunction;
import me.kubbidev.evonyacore.game.Tracker;
import me.kubbidev.evonyacore.game.core.GameInstance;
import me.kubbidev.evonyacore.game.core.scenario.Scenario;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
import me.kubbidev.evonyacore.players.Rank;
import me.kubbidev.evonyacore.threads.UpdateScoreBoardThread;
import me.kubbidev.evonyacore.utils.Utils;
import org.bukkit.Bukkit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreBoardManager {

    private final LobbyFunction lobbyFunction;
    private final CityFunction cityFunction;
    private final ScoreBoardLayout scoreBoardLayout;

    private final List<FastBoard> fastBoards = new ArrayList<>();

    public ScoreBoardManager(LobbyFunction lobbyFunction, CityFunction cityFunction) {
        this.lobbyFunction = lobbyFunction;
        this.cityFunction = cityFunction;
        this.scoreBoardLayout = new ScoreBoardLayout();
    }

    public void setupScoreboard(EvonyaPlayer player) {
        final FastBoard fastBoard = new FastBoard(player);
        this.fastBoards.add(fastBoard);

        Bukkit.getScheduler().scheduleSyncDelayedTask(EvonyaPlugin.INSTANCE,
                new UpdateScoreBoardThread(this, fastBoard), 1L);
    }

    public LobbyFunction getLobbyFunction() {
        return lobbyFunction;
    }

    public CityFunction getCityFunction() {
        return cityFunction;
    }

    public ScoreBoardLayout getScoreBoardLayout() {
        return scoreBoardLayout;
    }

    public List<FastBoard> getFastBoards() {
        return fastBoards;
    }

    public String translatePlaceholders(String string, EvonyaPlayer player) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        GameInstance gameInstance = null;
        Tracker tracker = null;

        if (player.hasGameInstance()) {
            gameInstance = player.getGameInstance();

            if (player.isPlaying()) {
                tracker = gameInstance.getTracker(player);
            }
        }

        if (string.contains("%role%"))
            string = string.replace("%role%", tracker.getGameRole().getPrefix());
        if (string.contains("%kills%"))
            string = string.replace("%kills%", String.valueOf(tracker.getKills()));
        if (string.contains("%rank%"))
            string = string.replace("%rank%", player.getPlayerRank().getPrefix());
        if (string.contains("%hosts%"))
            string = string.replace("%hosts%", String.valueOf(player.getHosts()));
        if (string.contains("%pwl%"))
            string = string.replace("%pwl%", String.valueOf(player.getPreWL()));
        if (string.contains("%onlinePlayers%"))
            string = string.replace("%onlinePlayers%", String.valueOf(Bukkit.getOnlinePlayers().size()));
        if (string.contains("%dayTime%"))
            string = string.replace("%dayTime%", dateFormat.format(Calendar.getInstance().getTime()));
        if (string.contains("%gamePlayers%"))
            string = string.replace("%gamePlayers%", String.valueOf(gameInstance.getPlayers().size()));
        if (string.contains("%gameSlots%"))
            string = string.replace("%gameSlots%", String.valueOf(gameInstance.getSlots()));
        if (string.contains("%gameTime%"))
            string = string.replace("%gameTime%", Utils.getFormattedTime(gameInstance.getElapsedTime()));
        if (string.contains("%gameId%"))
            string = string.replace("%gameId%", String.valueOf(gameInstance.getId()));
        if (string.contains("%gameHost%"))
            string = string.replace("%gameHost%", gameInstance.getHost().getUsername());
        if (string.contains("%ip%"))
            string = string.replace("%ip%", Color.translate(getIP()));
        if (string.contains("%ping%"))
            string = string.replace("%ping%", String.valueOf(player.getPing()));
        if (string.contains("%modPrefix%"))
            string = string.replace("%modPrefix%", Rank.MODERATEUR.getPrefix());
        if (string.contains("%scenario%"))
            string = string.replace("%scenario%", getScenarios(gameInstance.getScenarioManager().getRegisteredScenarios()));

        return string;
    }

    private String getScenarios(List<Scenario> scenarios) {
        final String scenario;

        if (scenarios.isEmpty())
            scenario = "&7Aucun scénario activé.";
        else scenario = scenarios.stream().map(s -> s.getInfo().getName()).collect(Collectors.joining(", ", "&7", "&r"));

        return Color.translate(scenario);
    }

    private int step = 0;
    private boolean neg = true;

    private String getIP() {
       final String[] animation = {
               "&ee&6vonya.eu", "&fe&ev&6onya.eu", "&ee&fv&eo&6nya.eu", "&6e&ev&fo&en&6ya.eu",
               "&6ev&eo&fn&ey&6a.eu", "&6evo&en&fy&ea&6.eu", "&6evon&ey&fa&e.&6eu", "&6evony&ea&f.&ee&6u",
               "&6evonya&e.&fe&eu", "&6evonya.&ee&fu", "&6evonya.e&eu", "&6evonya.eu",

               "&6evonya.eu", "&6evonya.eu", "&6evonya.eu", "&6evonya.eu",
               "&6evonya.eu", "&6evonya.eu", "&6evonya.eu", "&6evonya.eu",
               "&6evonya.eu", "&6evonya.eu", "&6evonya.eu", "&6evonya.eu",
               "&6evonya.eu", "&6evonya.eu", "&6evonya.eu", "&6evonya.eu",
               "&6evonya.eu", "&6evonya.eu", "&6evonya.eu", "&6evonya.eu",
               "&6evonya.eu", "&6evonya.eu", "&6evonya.eu", "&6evonya.eu",
               "&6evonya.eu", "&6evonya.eu", "&6evonya.eu", "&6evonya.eu",
               "&6evonya.eu", "&6evonya.eu", "&6evonya.eu", "&6evonya.eu",
               "&6evonya.eu", "&6evonya.eu", "&6evonya.eu", "&6evonya.eu"
       };
        final boolean backAndForth = false;

        if (step + 1 >= animation.length) {

            if (backAndForth) {
                neg = true;
                step--;
            } else {
                step = 0;
            }
        } else {
            if (neg) {
                if (step <= 0) {
                    neg = false;
                    step++;
                } else {
                    step--;
                }
            } else {
                step++;
            }
        }
        return animation[step];
    }
}
