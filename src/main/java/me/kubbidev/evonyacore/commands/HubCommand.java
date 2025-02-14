package me.kubbidev.evonyacore.commands;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.LobbyFunction;
import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.nexuspowered.Commands;

public final class HubCommand {
    private HubCommand() {}

    public static void register(EvonyaPlugin plugin) {
        LobbyFunction function = plugin.getLobbyFunction();
        Commands.create()
                .assertPlayer()
                .handler(context -> function.lobbyEnter(PlayerManager.wrapEvonyaPlayer(context.sender())))
                .register("hub", "lobby", "spawn");
    }
}
