package me.kubbidev.evonyacore.commands;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.game.GameManager;
import me.kubbidev.evonyacore.game.core.GameInstance;
import me.kubbidev.evonyacore.game.core.GameState;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.evonyacore.queue.QueueSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class JoinCommand implements CommandExecutor, TabCompleter {

	private final QueueSystem queueSystem;

	public JoinCommand(QueueSystem queueSystem) {
		this.queueSystem = queueSystem;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {

		if (!(sender instanceof Player)) {
			EvonyaPlugin.LOGGER.warning("Only players can type this command!");
			return true;
		}

		final EvonyaPlayer player = PlayerManager.wrapEvonyaPlayer((Player) sender);

		if (player.hasGameInstance()) {
			if (player.getGameInstance().getGameState() != GameState.ENDED) {
				player.sendMessage(EvonyaPlugin.PREFIX + "Vous êtes déjà dans une &cpartie&f.");
				return true;
			}
		}
		if (queueSystem.isQueue(player)) {
			player.sendMessage(EvonyaPlugin.PREFIX + "Vous êtes déjà dans une &cfile d'attente&f.");
			return true;
		}

		if (args.length != 1) {
			player.sendMessage(EvonyaPlugin.PREFIX + "Veuillez &cindiquer&f une partie.");
			return true;
		}

		final String arg = args[0];

		if (arg.equalsIgnoreCase("demonslayer")) {
			final List<GameInstance> gameInstanceList = GameManager.getGamesInstance().stream().filter(GameInstance::isOpen).filter(game -> game.getGameState() == GameState.WAITING || game.getGameState() == GameState.STARTING).collect(Collectors.toList());

			if (!gameInstanceList.isEmpty()) {
				final GameInstance gameInstance = gameInstanceList.get(0);

				if (player.hasGameInstance()) {
					player.getGameInstance().getPlayers().remove(player);
					queueSystem.getQueue(player).removePlayer(player);
				}
				queueSystem.connect(player, gameInstance);
			} else {
				player.closeInventory();
				player.sendMessage(EvonyaPlugin.PREFIX + "Il n'y a &caucune partie&f disponible pour l'instant.");
			}
			return true;
		}
		int newId;
		try {
			newId = Integer.parseInt(arg);
		} catch (NumberFormatException ex) {
			player.sendMessage(EvonyaPlugin.PREFIX + "Veuillez &cindiquer&f une partie.");
			return true;
		}

		if (GameManager.getGamesInstance().isEmpty()) {
			player.sendMessage(EvonyaPlugin.PREFIX + "Il n'y a &caucune partie&f disponible pour l'instant.");
			return true;
		}
		for (GameInstance gameInstance : GameManager.getGamesInstance()) {
			if (gameInstance.getId() == newId) {
				queueSystem.connect(player, gameInstance);
				return true;
			}
		}
		player.sendMessage(EvonyaPlugin.PREFIX + "Cette partie n'&cexiste plus&f.");

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String string, String[] args) {

		if (args.length == 1)
			return GameManager.getGamesInstance().stream().map(game -> String.valueOf(game.getId())).collect(Collectors.toList());

		return null;
	}
}
