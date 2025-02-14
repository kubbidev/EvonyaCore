package me.kubbidev.evonyacore.listeners;

import me.kubbidev.evonyacore.utils.Utils;
import me.kubbidev.nexuspowered.util.Text;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.WorldInitEvent;

public final class MiscListener implements Listener {
    private static final String[] SERVER_INFORMATION = {
            "                        &e&lEVONYA &7[&f1.8.9&7]",
            "   &e&l• &8&l┃ &d&lNOUVEAU MODE DE JEU &6&lDEMON SLAYER &8&l┃ &e&l•"
    };

    @EventHandler
    public void playerHungerDeplete(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        event.setMotd(Text.colorize(Utils.formatStringArray(SERVER_INFORMATION)));
    }

    @EventHandler
    public void oneWorldInit(WorldInitEvent event) {
        event.getWorld().setKeepSpawnInMemory(false);
    }
}
