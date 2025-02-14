package me.kubbidev.evonyacore.commands;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.nexuspowered.Commands;

public final class DiscordCommand {
    private DiscordCommand() {}

    public static void register(EvonyaPlugin plugin) {
        Commands.create()
                .assertPermission("evonyacore.discord")
                .description("See the Discord server invitation link")
                .handler(context -> context.reply("&7[&6!&7] &eDiscord link: &b" + EvonyaPlugin.DISCORD))
                .register("discord");
    }
}
