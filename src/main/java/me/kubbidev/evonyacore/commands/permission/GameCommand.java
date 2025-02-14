package me.kubbidev.evonyacore.commands.permission;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.game.GameManager;
import me.kubbidev.evonyacore.game.core.GameInstance;
import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.evonyacore.players.Rank;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
import me.kubbidev.evonyacore.utils.Message;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class GameCommand implements CommandExecutor, TabCompleter {

    private final GameManager gameManager;

    public GameCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {

        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("list"))
                {
                    EvonyaPlugin.LOGGER.info(" ");
                    EvonyaPlugin.LOGGER.info(" ┃ Games - Liste des serveurs");
                    GameManager.getGamesInstance().stream().map(game -> "   ┃ " + game.getId() + " • " + game.getGameState().getName() + " (" + game.getPlayers().size() + ")").forEach(EvonyaPlugin.LOGGER::info);
                    EvonyaPlugin.LOGGER.info(" ");
                }
            }
            else if (args.length == 2) {
                final String args1 = args[1];

                if (args[0].equalsIgnoreCase("delete")) {
                    int newId;
                    try {
                        newId = Integer.parseInt(args1);
                    }
                    catch (NumberFormatException ex) {
                        EvonyaPlugin.LOGGER.info("Veuillez indiquer une partie.");
                        return true;
                    }
                    final Iterator<GameInstance> iter = GameManager.getGamesInstance().iterator();

                    while (iter.hasNext()) {
                        final GameInstance gameInstance = iter.next();

                        if (gameInstance.getId() == newId) {
                            iter.remove();
                            gameInstance.setEnding(true);

                            EvonyaPlugin.LOGGER.info("La partie " + newId + " viens d'être supprimée.");
                            gameManager.deleteGameInstance(gameInstance);
                            return true;
                        }
                    }
                    EvonyaPlugin.LOGGER.info("Cette partie n'existe plus.");
                }
            }
        }

        if (sender instanceof Player) {
            final EvonyaPlayer player = PlayerManager.wrapEvonyaPlayer((Player) sender);

            if (player.getPlayerRank().isLowerThan(Rank.MODERATEUR))
                return false;

            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("list")) {
                    player.sendMessage(" ");
                    player.sendMessage(" &8┃ &c&lGames - Liste des serveurs");
                    GameManager.getGamesInstance().forEach(game -> new Message("   &8┃&6 " + game.getId() + "&f • " + game.getGameState().getName() + " &f(" + game.getPlayers().size() + ")").setCommand("/join " + game.getId()).setHoverMessage("Clic pour rejoindre").sendRunCommand(player.getPlayer()));
                    player.sendMessage(" ");

                } else sendHelpMessage(player);

            }
            else if (args.length == 2) {
                final String args1 = args[1];

                if (args[0].equalsIgnoreCase("delete")) {
                    int newId;
                    try {
                        newId = Integer.parseInt(args1);
                    }
                    catch (NumberFormatException ex) {
                        player.sendMessage(EvonyaPlugin.PREFIX + "Veuillez indiquer une partie.");
                        return true;
                    }
                    final Iterator<GameInstance> iter = GameManager.getGamesInstance().iterator();

                    while (iter.hasNext()) {
                        final GameInstance gameInstance = iter.next();

                        if (gameInstance.getId() == newId) {
                            iter.remove();
                            gameInstance.setEnding(true);

                            player.sendMessage(EvonyaPlugin.PREFIX + "La partie &6" + newId + "&f viens d'être &csupprimée&f.");
                            gameManager.deleteGameInstance(gameInstance);
                            return true;
                        }
                    }
                    player.sendMessage(EvonyaPlugin.PREFIX + "Cette partie &cn'existe plus&f.");
                }
                else sendHelpMessage(player);

            }
            else sendHelpMessage(player);

        }
        return true;
    }

    private void sendHelpMessage(EvonyaPlayer player) {
        player.sendMessage(" ");
        player.sendMessage(" &8┃ &c&lGame - Commandes disponibles");
        new Message("    &8•&c /game delete <id> &f»&7 Supprime une partie existante.").setCommand("/game delete <id>").setHoverMessage("Cliquez pour exécuter").sendSuggestCommand(player.getPlayer());
        new Message("    &8•&c /game list &f»&7 Affiche la liste des parties disponibles.").setCommand("/game list").setHoverMessage("Cliquez pour exécuter").sendSuggestCommand(player.getPlayer());
        player.sendMessage(" ");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String string, String[] args) {
        if (args.length == 1) return Arrays.asList("delete", "list");

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("delete"))
                return GameManager.getGamesInstance().stream().map(game -> String.valueOf(game.getId())).collect(Collectors.toList());
        }
        return null;
    }
}
