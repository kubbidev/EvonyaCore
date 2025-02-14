package me.kubbidev.evonyacore.commands.permission;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.evonyacore.players.Rank;
import me.kubbidev.evonyacore.players.EPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {

        if (!(sender instanceof Player)) {
            EvonyaPlugin.LOGGER.warning("Only players can run this command");
            return true;
        }
        final EPlayer player = PlayerManager.wrapPlayer((Player) sender);

        if (player.getPlayerRank().isLowerThan(Rank.MODERATEUR)) {

            if (player.hasGameInstance()) {
                if (!player.isHost())
                    return true;
            }
            else return true;
        }
        final String message;

        player.setAllowFlight(!player.getAllowFlight());
        message = player.getAllowFlight() ? "Vous venez d'&aactiver&f le mode &cfly&f." : "Vous venez de &cdésactiver&f le mode &cfly&f.";

        player.sendMessage(EvonyaPlugin.PREFIX + message);
        return true;
    }
}
