package me.kubbidev.evonyacore.commands.permission;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.evonyacore.players.Rank;
import me.kubbidev.evonyacore.utils.EvonyaSounds;
import me.kubbidev.evonyacore.exceptions.EvonyaPlayerDoesNotExistException;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class HealCommand implements CommandExecutor, TabCompleter {

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

		if (args.length < 1) {
			player.setHealth(20);
			player.sendMessage(EvonyaPlugin.PREFIX + "&6Votre vie est revenue !");
			player.playSound(EvonyaSounds.SUCCESSFULLY);
			return true;
		}

		if (args.length != 1) {
			player.sendMessage(EvonyaPlugin.PREFIX + "Un problème ? (&cheal&f) + (&cjoueur&f)");
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

		final String all = args[0].toLowerCase();
		if (all.equalsIgnoreCase("all")) {

			player.getWorldPlayers().forEach((onlinePlayer) -> {

				onlinePlayer.setHealth(20);
				onlinePlayer.sendMessage(EvonyaPlugin.PREFIX + "&6Votre vie est revenue !");
				onlinePlayer.playSound(EvonyaSounds.SUCCESSFULLY);
			});

			EvonyaPlugin.LOGGER.info("De la vie pour tout le monde !");
			return true;
		}
		target.playSound(EvonyaSounds.SUCCESSFULLY);

		target.setHealth(20);
		target.sendMessage(EvonyaPlugin.PREFIX + "&6Votre vie est revenue !");

		player.sendMessage(EvonyaPlugin.PREFIX + "&a" + target.getUsername() + "&r a bien été soigné.");
		return true;
	}
	
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String string, String[] args) {
			if (args.length == 1) {
				final List<String> playerNames = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());

				playerNames.add("all");
				return playerNames;
			}
		return null;
	}
}
