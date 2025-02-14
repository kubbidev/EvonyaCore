package me.kubbidev.evonyacore.commands;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.nexuspowered.Commands;

public final class DiscordCommand {
    private DiscordCommand() {}

    public static void register(EvonyaPlugin plugin) {
        Commands.create()
                .handler(context -> context.reply(EvonyaPlugin.prefixed("Rejoins notre &cdiscord&f : &6" + EvonyaPlugin.DISCORD)))
                .register("discord");
    }
}
