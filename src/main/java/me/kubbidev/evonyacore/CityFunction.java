package me.kubbidev.evonyacore;

import me.kubbidev.evonyacore.players.EPlayer;
import me.kubbidev.evonyacore.players.Rank;
import me.kubbidev.evonyacore.players.State;
import me.kubbidev.evonyacore.utils.WorldManager;
import org.bukkit.*;

public class CityFunction {

    private final World city;
    private final Location cityLocation;

    public CityFunction() {
        this.city = WorldManager.loadWorld("CITY");
        this.cityLocation = new Location(city, -180.5, 29, -50.5, 180, 0);
    }

    public World getCity() {
        return city;
    }

    public void cityEnter(EPlayer player) {
        if (player.getPlayerRank().isLowerThan(Rank.GUEST)) {
            player.sendMessage(EvonyaPlugin.PREFIX + "Vous &cn'avez pas&f la permission de &arejoindre&f ce monde.");
            player.closeInventory();
            return;
        }
        player.teleport(cityLocation);
        player.refresh();
        player.setGameMode(GameMode.ADVENTURE);
        player.setState(State.CITY);
    }
}
