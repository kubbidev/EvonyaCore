package me.kubbidev.evonyacore.commands.permission;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.evonyacore.players.Rank;
import me.kubbidev.evonyacore.exceptions.EvonyaPlayerDoesNotExistException;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class HostCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof ConsoleCommandSender) {

            if (args.length != 2) {
                EvonyaPlugin.LOGGER.info("Un problème ? (host) + (joueur) + (valeur)");
                return true;
            }
            final String targetName = args[0];
            final EvonyaPlayer target;
            try {
                target = PlayerManager.wrapEvonyaPlayer(targetName);
            } catch (EvonyaPlayerDoesNotExistException e) {
                EvonyaPlugin.LOGGER.info("Ce joueur n'est pas connecté...");
                return true;
            }

            final int hosts;
            try {
                hosts = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                EvonyaPlugin.LOGGER.info("Veuillez indiquer un chiffre.");
                return true;
            }
            target.setHosts(hosts);

            EvonyaPlugin.LOGGER.info(("Les hosts de " + target.getUsername() + " ont bien été mis à jour."));
            target.sendMessage(EvonyaPlugin.PREFIX + "Vos hosts ont été &cmis à jour&f, vous disposez désomrais de &a" + hosts + "&f hosts.");
        }

        if (sender instanceof Player) {

            final EvonyaPlayer player = PlayerManager.wrapEvonyaPlayer((Player) sender);

            if (player.getPlayerRank().isLowerThan(Rank.DEVELOPPEUR))
                return false;

            if (args.length != 2) {
                player.sendMessage(EvonyaPlugin.PREFIX + "Un problème ? (&chost&f) + (&cjoueur&f) + (&cvaleur&f)");
                return true;
            }
            final String targetName = args[0];
            final EvonyaPlayer target;
            try {
                target = PlayerManager.wrapEvonyaPlayer(targetName);
            } catch (EvonyaPlayerDoesNotExistException e) {
                player.sendMessage(EvonyaPlugin.PREFIX + "Ce joueur n'est pas connecté...");
                return true;
            }

            final int hosts;
            try {
                hosts = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage(EvonyaPlugin.PREFIX + "Veuillez &cindiquer un nombre&f.");
                return true;
            }
            target.setHosts(hosts);

            player.sendMessage(EvonyaPlugin.PREFIX + "Les hosts de &a" + target.getUsername() + "&f ont bien été mis a jour.");
            target.sendMessage(EvonyaPlugin.PREFIX + "Vos hosts ont été &cmis à jour&f, vous disposez désomrais de &a" + hosts + "&f hosts.");
        }
        return true;
    }
}
