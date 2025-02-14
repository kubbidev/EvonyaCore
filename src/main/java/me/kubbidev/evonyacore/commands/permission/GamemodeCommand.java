package me.kubbidev.evonyacore.commands.permission;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.evonyacore.players.Rank;
import me.kubbidev.evonyacore.exceptions.EvonyaPlayerDoesNotExistException;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class GamemodeCommand implements CommandExecutor, TabCompleter {

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
            }
            else return true;
        }

        if (args.length == 1) {

            final String gamemode = args[0];

            switch (gamemode.toLowerCase()) {
                case "survival":
                    player.setGameMode(GameMode.SURVIVAL);
                    player.sendMessage(EvonyaPlugin.PREFIX + "Votre &cmode de jeu&f a été mis à jour sur &c" + gamemode + "&f.");
                    break;
                case "creative":
                    player.setGameMode(GameMode.CREATIVE);
                    player.sendMessage(EvonyaPlugin.PREFIX + "Votre &cmode de jeu&f a été mis à jour sur &c" + gamemode + "&f.");
                    break;
                case "adventure":
                    player.setGameMode(GameMode.ADVENTURE);
                    player.sendMessage(EvonyaPlugin.PREFIX + "Votre &cmode de jeu&f a été mis à jour sur &c" + gamemode + "&f.");
                    break;
                case "spectator":
                    player.setGameMode(GameMode.SPECTATOR);
                    player.sendMessage(EvonyaPlugin.PREFIX + "Votre &cmode de jeu&f a été mis à jour sur &c" + gamemode + "&f.");
                    break;
                default:
                    player.sendMessage(EvonyaPlugin.PREFIX + "Ce mode de jeu n'existe pas.");
                    break;
            }

        } else if (args.length == 2) {

            final String gamemode = args[0];

            final String targetName = args[1];
            final EvonyaPlayer target;
            try {
                target = PlayerManager.wrapEvonyaPlayer(targetName);
            } catch (EvonyaPlayerDoesNotExistException e) {
                player.sendMessage(EvonyaPlugin.PREFIX + "Ce joueur n'est pas connecté...");
                return true;
            }
            switch (gamemode.toLowerCase()) {
                case "survival":
                    target.setGameMode(GameMode.SURVIVAL);
                    target.sendMessage(EvonyaPlugin.PREFIX + "Votre &cmode de jeu&f a été mis à jour sur &c" + gamemode + "&f.");
                    player.sendMessage(EvonyaPlugin.PREFIX + "Le mode de jeu de &a" + targetName + "&f a été mis à jour sur &c" + gamemode + "&f.");
                    break;
                case "creative":
                    target.setGameMode(GameMode.CREATIVE);
                    target.sendMessage(EvonyaPlugin.PREFIX + "Votre &cmode de jeu&f a été mis à jour sur &c" + gamemode + "&f.");
                    player.sendMessage(EvonyaPlugin.PREFIX + "Le mode de jeu de &a" + targetName + "&f a été mis à jour sur &c" + gamemode + "&f.");
                    break;
                case "adventure":
                    target.setGameMode(GameMode.ADVENTURE);
                    target.sendMessage(EvonyaPlugin.PREFIX + "Votre &cmode de jeu&f a été mis à jour sur &c" + gamemode + "&f.");
                    player.sendMessage(EvonyaPlugin.PREFIX + "Le mode de jeu de &a" + targetName + "&f a été mis à jour sur &c" + gamemode + "&f.");
                    break;
                case "spectator":
                    target.setGameMode(GameMode.SPECTATOR);
                    target.sendMessage(EvonyaPlugin.PREFIX + "Votre &cmode de jeu&f a été mis à jour sur &c" + gamemode + "&f.");
                    player.sendMessage(EvonyaPlugin.PREFIX + "Le mode de jeu de &a" + targetName + "&f a été mis à jour sur &c" + gamemode + "&f.");
                    break;
                default:
                    player.sendMessage(EvonyaPlugin.PREFIX + "Ce mode de jeu n'existe pas.");
                    break;
            }

        } else player.sendMessage(EvonyaPlugin.PREFIX + "Un problème ? (&cgamemode&f) + (&ctype&f) + (&cjoueur&f)");

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String string, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("survival", "creative", "adventure", "spectator");
        }
        return null;
    }
}
