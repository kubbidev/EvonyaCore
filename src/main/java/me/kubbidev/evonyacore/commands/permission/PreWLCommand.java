package me.kubbidev.evonyacore.commands.permission;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.exceptions.EvonyaPlayerDoesNotExistException;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.evonyacore.players.Rank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class PreWLCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof ConsoleCommandSender) {

            if (args.length != 2) {
                EvonyaPlugin.LOGGER.info("Un problème ? (pwl) + (joueur) + (valeur)");
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

            final int pwl;
            try {
                pwl = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                EvonyaPlugin.LOGGER.info("Veuillez indiquer un chiffre.");
                return true;
            }
            target.setPreWL(pwl);

            EvonyaPlugin.LOGGER.info(("Les pre-whitelist de " + target.getUsername() + " ont bien été mis à jour."));
            target.sendMessage(EvonyaPlugin.PREFIX + "Vos pre-whitelist ont été &cmis à jour&f, vous disposez désomrais de &a" + pwl + "&f pre-whitelist.");
        }

        if (sender instanceof Player) {

            final EvonyaPlayer player = PlayerManager.wrapEvonyaPlayer((Player) sender);

            if (player.getPlayerRank().isLowerThan(Rank.DEVELOPPEUR))
                return false;

            if (args.length != 2) {
                player.sendMessage(EvonyaPlugin.PREFIX + "Un problème ? (&cpwl&f) + (&cjoueur&f) + (&cvaleur&f)");
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

            final int pwl;
            try {
                pwl = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage(EvonyaPlugin.PREFIX + "Veuillez &eindiquer un nombre&f.");
                return true;
            }
            target.setPreWL(pwl);

            player.sendMessage(EvonyaPlugin.PREFIX + "Les pre-whitelist de &a" + target.getUsername() + "&f ont bien été mis a jour.");
            target.sendMessage(EvonyaPlugin.PREFIX + "Vos pre-whitelist ont été &cmis à jour&f, vous disposez désomrais de &a" + pwl + "&f pre-whitelist.");
        }
        return true;
    }
}
