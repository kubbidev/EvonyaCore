package me.kubbidev.evonyacore.commands.permission;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.exceptions.EvonyaPlayerDoesNotExistException;
import me.kubbidev.evonyacore.players.EPlayer;
import me.kubbidev.evonyacore.players.PlayerManager;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

import static me.kubbidev.evonyacore.players.Rank.*;

public class RankCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {

        if(sender instanceof ConsoleCommandSender) {

            if (args.length != 2) {
                EvonyaPlugin.LOGGER.info("Un problème ? (rank) + (joueur) + (grade)");
                return true;
            }
            final String targetName = args[0];
            final EPlayer target;
            try {
                target = PlayerManager.wrapPlayer(targetName);
            } catch (EvonyaPlayerDoesNotExistException e) {
                EvonyaPlugin.LOGGER.info("Ce joueur n'est pas connecté...");
                return true;
            }
            final String rankName = args[1].toLowerCase();
            final int rankValue = getRank(rankName);

            if (rankValue < 0) {
                EvonyaPlugin.LOGGER.info("Ce grade n'existe pas.");
                return true;
            }
            target.setPlayerRank(getRankByValue(rankValue));
            target.sendMessage(EvonyaPlugin.PREFIX + "Votre grade a été &cmis à jour&f, vous disposez désormais du grade " + target.getPlayerRank().getPrefix() + ".");

            EvonyaPlugin.LOGGER.info("Le grade de " + target.getUsername() + " a bien été mis à jour.");
        }

        if(sender instanceof Player) {

            final EPlayer player = PlayerManager.wrapPlayer((Player) sender);

            if (player.getPlayerRank().isLowerThan(DEVELOPPEUR))
                return false;

            if (args.length != 2) {
                player.sendMessage(EvonyaPlugin.PREFIX + "Un problème ? (&crank&f) + (&cjoueur&f) + (&cgrade&f)");
                return true;
            }
            final String targetName = args[0];
            final EPlayer target;
            try {
                target = PlayerManager.wrapPlayer(targetName);
            } catch (EvonyaPlayerDoesNotExistException e) {
                player.sendMessage(EvonyaPlugin.PREFIX + "Ce joueur n'est pas connecté...");
                return true;
            }
            final String rankName = args[1].toLowerCase();
            final int rankValue = getRank(rankName);

            if (rankValue < 0) {
                player.sendMessage(EvonyaPlugin.PREFIX + "Ce grade n'existe pas.");
                return true;
            }
            if (rankValue <= player.getPlayerRank().getValue()) {
                player.sendMessage(EvonyaPlugin.PREFIX + "Vous ne pouvez pas vous attribuer un grade plus &chaut ou égal&f au votre.");
                return true;
            }
            if (player.getPlayerRank().getValue() >= target.getPlayerRank().getValue() && target != player) {
                player.sendMessage(EvonyaPlugin.PREFIX + "Vous ne pouvez pas modifier le grade d'une personne plus &chaute ou égal&f au votre.");
                return true;
            }
            target.setPlayerRank(getRankByValue(rankValue));
            target.sendMessage(EvonyaPlugin.PREFIX + "Votre grade a été &cmis à jour&f, vous disposez désormais du grade " + target.getPlayerRank().getPrefix() + "&f.");

            player.sendMessage(EvonyaPlugin.PREFIX + "Le grade de &a" + target.getUsername() + "&f a bien été mis a jour.");
        }

        return true;
    }

    private int getRank(String rankName) {
        switch (rankName) {
            case "administrateur":
                return ADMINISTRATEUR.getValue();
            case "responsable":
                return RESPONSABLE.getValue();
            case "developpeur":
                return DEVELOPPEUR.getValue();
            case "moderateur":
                return MODERATEUR.getValue();
            case "team":
                return TEAM.getValue();
            case "guest":
                return GUEST.getValue();
            case "joueur":
                return JOUEUR.getValue();
            default:
                return -1;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String string, String[] args) {

        if (args.length == 2)
            return Arrays.asList("administrateur", "responsable", "developpeur", "moderateur", "team", "guest", "joueur");
        return null;
    }
}
