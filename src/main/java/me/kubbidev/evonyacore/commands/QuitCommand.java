package me.kubbidev.evonyacore.commands;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.game.GameManager;
import me.kubbidev.evonyacore.players.EPlayer;
import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.evonyacore.queue.QueueSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QuitCommand implements CommandExecutor {

    private final QueueSystem queueSystem;

    public QuitCommand(QueueSystem queueSystem) {
        this.queueSystem = queueSystem;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {

        if (!(sender instanceof Player)) {
            EvonyaPlugin.LOGGER.warning("Only players can type this command!");
            return true;
        }
        final EPlayer player = PlayerManager.wrapPlayer((Player) sender);

        if (GameManager.getGamesInstance().isEmpty() || (!queueSystem.isQueue(player))) {
            player.sendMessage(EvonyaPlugin.PREFIX + "Vous n'êtes dans aucune &cfile d'attente&f.");
            return true;
        }

        queueSystem.getQueueList().forEach(queue -> {
            queue.removePlayer(player);
            player.sendMessage(EvonyaPlugin.PREFIX + "Vous venez de quitter la &cfile d'attente&f.");
        });
        return true;
    }
}
