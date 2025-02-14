package me.kubbidev.evonyacore.commands.permission;

import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.evonyacore.players.Rank;
import me.kubbidev.evonyacore.utils.EvonyaSounds;
import me.kubbidev.evonyacore.players.EPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class BroadcastCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {

        if (sender instanceof ConsoleCommandSender) {

            final StringBuilder message = new StringBuilder();

            Arrays.stream(args).forEach(arg -> message.append(arg).append(" "));
            sendBroadcast(sender.getName(), message);
        }

        if (sender instanceof Player) {

            final EPlayer player = PlayerManager.wrapPlayer((Player) sender);

            if (player.getPlayerRank().isLowerThan(Rank.DEVELOPPEUR))
                return false;

            final StringBuilder message = new StringBuilder();

            Arrays.stream(args).forEach(arg -> message.append(arg).append(" "));
            sendBroadcast(player.getUsername(), message);
        }
        return true;
    }

    private void sendBroadcast(String sender, StringBuilder message) {
        PlayerManager.getEvonyaPlayers().stream()
                .filter(EPlayer::isOnline)
                .forEach(player -> {

                    player.sendMessage(" ");
                    player.sendMessage("&c&l" + sender + " :&r " + message.toString());
                    player.sendMessage(" ");
                    player.playSound(EvonyaSounds.SUCCESSFULLY);
                });
    }
}
