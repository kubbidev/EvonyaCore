package me.kubbidev.evonyacore.scoreboard;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.utils.Utils;
import me.kubbidev.nexuspowered.util.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScoreBoardLayout {

    private final List<String> lobby;
    private final List<String> demonslayerLobby;
    private final List<String> demonslayerPlaying;
    private final List<String> demonslayerSpectator;

    private final String titleLobby;
    private final String titlePlaying;

    private final String headerLobby;
    private final String headerPlaying;
    private final String footer;

    public ScoreBoardLayout() {
        this.titleLobby = Text.colorize("&c&lEVONYA&r");
        this.titlePlaying = Text.colorize("&cdemonslayer-%gameId%");

        this.lobby = getUpsideDownLines(Arrays.asList(
                "",
                "&c&lINFOS",
                " &8┃&f Grade : %rank%",
                " &8┃&f Hosts : &6%hosts%",
                " &8┃&f Pre-WL : &b%pwl%",
                "",
                "&c&lSERVEUR",
                " &8┃&f Heure : &a%dayTime%",
                " &8┃&f Joueurs : &a%onlinePlayers%",
                "",
                "%ip%"
        ));
        this.demonslayerLobby = getUpsideDownLines(Arrays.asList(
                "",
                "&c&lINFOS",
                " &8┃&f Host : &c%gameHost%",
                " &8┃&f Joueurs : &c%gamePlayers%&f/&c%gameSlots%",
                "",
                "%ip%"
        ));
        this.demonslayerPlaying = getUpsideDownLines(Arrays.asList(
                "",
                "&c&lPARTIE",
                " &8┃&f Joueurs : &a%gamePlayers%",
                "",
                " &8┃&f Rôle : %role%",
                " &8┃&f Temps : &a%gameTime%",
                " &8┃&f Kills : &6%kills%",
                "",
                "%ip%"
        ));
        this.demonslayerSpectator = getUpsideDownLines(Arrays.asList(
                "",
                "&c&lPARTIE",
                " &8┃&f Joueurs : &a%gamePlayers%",
                " &8┃&f Temps : &a%gameTime%",
                "",
                "%ip%"
        ));
        this.headerLobby = Utils.formatStringList(Arrays.asList(
                "",
                "&8» &c&lEVONYA &8«",
                "&fevonya.eu",
                "",
                "Ping: &a%ping%&f   Joueurs: &a%onlinePlayers%",
                ""
        ));
        this.headerPlaying = Utils.formatStringList(Arrays.asList(
                "",
                "&8» &c&lEVONYA &8«",
                "&fevonya.eu",
                "",
                "Ping: &a%ping%&f   Joueurs: &a%onlinePlayers%",
                "",
                "%scenario%",
                ""
        ));
        this.footer = Utils.formatStringList(Arrays.asList(
                "",
                " Besoin d'aide ? Contacte un %modPrefix% ",
                "&8➥&c /discord",
                ""
        ));
    }

    public List<String> getLines(ScoreBoardTypes scoreboardTypes) {
        switch (scoreboardTypes) {
            case LOBBY:
            case CITY:
                return this.lobby;
            case DEMONSLAYER_LOBBY:
                return this.demonslayerLobby;
            case DEMONSLAYER_PLAYING:
                return this.demonslayerPlaying;
            case DEMONSLAYER_SPECTATOR:
                return this.demonslayerSpectator;
            default:
                throw new RuntimeException("Unable to find scoreboard type!");
        }
    }

    public String getTitle(ScoreBoardTypes scoreBoardTypes) {
        switch (scoreBoardTypes) {
            case LOBBY:
            case CITY:
                return this.titleLobby;
            case DEMONSLAYER_LOBBY:
            case DEMONSLAYER_PLAYING:
            case DEMONSLAYER_SPECTATOR:
                return this.titlePlaying;
            default:
                throw new RuntimeException("Unable to find scoreboard type!");
        }
    }

    public String getFooter() {
        return footer;
    }

    public String getHeader(ScoreBoardTypes scoreBoardTypes) {
        switch (scoreBoardTypes) {
            case LOBBY:
            case CITY:
                return this.headerLobby;
            case DEMONSLAYER_LOBBY:
            case DEMONSLAYER_PLAYING:
            case DEMONSLAYER_SPECTATOR:
                return this.headerPlaying;
            default:
                throw new RuntimeException("Unable to find scoreboard type!");
        }
    }

    private List<String> getUpsideDownLines(List<String> list) {
        final List<String> newList = new ArrayList<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            if (newList.size() == 15) {
                EvonyaPlugin.LOGGER.warning("Scoreboard lines can't have more than 15 lines!");
                break;
            }
            newList.add(Color.translate(list.get(i)));
        }
        return newList;
    }
}
