package me.kubbidev.evonyacore.utils;

import me.kubbidev.nexuspowered.Services;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class UtilityMethod {
    private UtilityMethod() {}

    public static <T> T orElse(T value, T orElse) {
        return value == null ? orElse : value;
    }

    public static String getDisplayName(Player player, CachedMetaData metaData) {
        String prefix = orElse(metaData.getPrefix(), "");
        String suffix = orElse(metaData.getSuffix(), "");
        return prefix + player.getName() + suffix;
    }

    public static String getDisplayName(Player player) {
        LuckPerms luckPerms = Services.load(LuckPerms.class);
        return getDisplayName(player, luckPerms.getPlayerAdapter(Player.class).getMetaData(player));
    }

    private static final UUID CONSOLE_UUID = new UUID(0, 0);

    public static UUID getUuid(CommandSender sender) {
        return sender instanceof Player ? ((Player) sender).getUniqueId() : CONSOLE_UUID;
    }
}