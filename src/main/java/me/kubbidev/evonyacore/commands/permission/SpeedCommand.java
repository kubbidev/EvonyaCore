package me.kubbidev.evonyacore.commands.permission;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.evonyacore.players.Rank;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpeedCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {

        if (!(sender instanceof Player)) {
            EvonyaPlugin.LOGGER.warning("Only players can run this command");
            return true;
        }
        final EvonyaPlayer player = PlayerManager.wrapEvonyaPlayer((Player) sender);

        if (player.getPlayerRank().isLowerThan(Rank.MODERATEUR)) {

            if (player.hasGameInstance()) {
                if (!player.isHost())
                    return true;

            } else return true;
        }

        if (args.length != 1) {
            if (args.length == 0) {
                if (player.isFlying()) {
                    player.setFlySpeed((float) 0.1);
                } else {
                    player.setWalkSpeed((float) 0.2);
                }
                player.sendMessage(EvonyaPlugin.PREFIX + "Votre &cvitesse&f a été réinitialisé.");
            } else player.sendMessage(EvonyaPlugin.PREFIX + "Un problème ? (&cspeed&f) + (&cvaleur&f)");
            return false;
        }
        int speed;
        try {
            speed = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage(EvonyaPlugin.PREFIX + "Veuillez &cindiquer un nombre&f.");
            return false;
        }
        if (speed < 1 || speed > 10) {
            player.sendMessage(EvonyaPlugin.PREFIX + "Veuillez indiquer un nombre entre &c1 et 10&f.");
            return false;
        }
        if (player.isFlying()) {
            player.setFlySpeed((float) speed / 10);
        } else {
            player.setWalkSpeed((float) speed / 10);
        }
        player.sendMessage(EvonyaPlugin.PREFIX + "Votre &cvitesse&f est maintenant réglée sur &a" + speed + "&f.");

        return true;
    }
}
