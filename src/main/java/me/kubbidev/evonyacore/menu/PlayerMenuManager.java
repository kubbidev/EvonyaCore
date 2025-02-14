package me.kubbidev.evonyacore.menu;

import me.kubbidev.evonyacore.players.EPlayer;

import java.util.HashMap;
import java.util.Map;

public class PlayerMenuManager {

    private static final Map<EPlayer, PlayerMenuUtility> PLAYER_MENU_UTILITY_MAP = new HashMap<>();

    public static PlayerMenuUtility getPlayerMenuUtility(EPlayer player) {
        return PLAYER_MENU_UTILITY_MAP.computeIfAbsent(player, PlayerMenuUtility::new);
    }
}
