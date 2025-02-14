package me.kubbidev.evonyacore.listeners;

import me.kubbidev.evonyacore.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.WorldInitEvent;

import java.util.Arrays;

public class MiscListener implements Listener {

    @EventHandler
    public void playerHungerDeplete(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState())
            event.setCancelled(true);
    }

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        event.setMotd(Color.translate(Utils.convertListToString(Arrays.asList(
                "                        &e&lEVONYA &7[&f1.8.9&7]",
                "   &e&l• &8&l┃ &d&lNOUVEAU MODE DE JEU &6&lDEMON SLAYER &8&l┃ &e&l•"
        ))));
    }

    @EventHandler
    public void oneWorldInit(WorldInitEvent event) {
        event.getWorld().setKeepSpawnInMemory(false);
    }
}
