package me.kubbidev.evonyacore.commands.permission;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.evonyacore.players.Rank;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class TimeCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {

        if (!(sender instanceof Player)) {
            EvonyaPlugin.LOGGER.warning("Only players can run this command");
            return true;
        }
        final EvonyaPlayer player = PlayerManager.wrapEvonyaPlayer((Player) sender);

        if (player.getPlayerRank().isLowerThan(Rank.DEVELOPPEUR))
            return false;

        if (args.length == 1) {

            final String time = args[0];

            switch (time.toLowerCase()) {
                case "day":
                    player.getWorld().setTime(1000);
                    player.sendMessage(EvonyaPlugin.PREFIX + "Heure fixée à &a" + player.getWorld().getTime() + "&f.");
                    break;
                case "midnight":
                    player.getWorld().setTime(18000);
                    player.sendMessage(EvonyaPlugin.PREFIX + "Heure fixée à &a" + player.getWorld().getTime() + "&f.");
                    break;
                case "night":
                    player.getWorld().setTime(13000);
                    player.sendMessage(EvonyaPlugin.PREFIX + "Heure fixée à &a" + player.getWorld().getTime() + "&f.");
                    break;
                case "noon":
                    player.getWorld().setTime(6000);
                    player.sendMessage(EvonyaPlugin.PREFIX + "Heure fixée à &a" + player.getWorld().getTime() + "&f.");
                    break;
                default:
                    long timeLong;
                    try {
                        timeLong = Long.parseLong(args[0]);
                    } catch (NumberFormatException e) {
                        player.sendMessage(EvonyaPlugin.PREFIX + "Veuillez &cindiquer un nombre&f.");
                        return false;
                    }
                    player.getWorld().setTime(timeLong);
                    player.sendMessage(EvonyaPlugin.PREFIX + "Heure fixée à &a" + player.getWorld().getTime() + "&f.");
                    break;
            }

        } else player.sendMessage(EvonyaPlugin.PREFIX + "Un problème ? (&ctime&f) + (&cheure&f)");

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String string, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("day", "midnight", "night", "noon");
        }
        return null;
    }
}
