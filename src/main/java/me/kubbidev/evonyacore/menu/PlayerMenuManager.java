package me.kubbidev.evonyacore.menu;

import me.kubbidev.evonyacore.players.EvonyaPlayer;

import java.util.HashMap;
import java.util.Map;

public class PlayerMenuManager {

    private static final Map<EvonyaPlayer, PlayerMenuUtility> PLAYER_MENU_UTILITY_MAP = new HashMap<>();

    public static PlayerMenuUtility getPlayerMenuUtility(EvonyaPlayer player) {
        return PLAYER_MENU_UTILITY_MAP.computeIfAbsent(player, PlayerMenuUtility::new);
    }
}
