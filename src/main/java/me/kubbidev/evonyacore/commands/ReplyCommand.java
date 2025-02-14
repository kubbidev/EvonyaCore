package me.kubbidev.evonyacore.commands;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.exceptions.EvonyaPlayerDoesNotExistException;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
import me.kubbidev.evonyacore.players.PlayerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ReplyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {

        if (!(sender instanceof Player)) {
            EvonyaPlugin.LOGGER.warning("Only players can type this command!");
            return true;
        }
        final EvonyaPlayer player = PlayerManager.wrapEvonyaPlayer((Player) sender);

        if (args.length < 1) {
            MessageCommand.sendHelpMessage(player);
            return true;
        }

        if (!player.hasLastPrivateMessage()) {
            player.sendMessage(EvonyaPlugin.PREFIX + "Aucun joueur disponible.");
            return true;
        }

        final EvonyaPlayer target;
        try {
            target = PlayerManager.wrapEvonyaPlayer(player.getLastPrivateMessage());
        } catch (EvonyaPlayerDoesNotExistException e) {
            throw new RuntimeException(e);
        }
        if (target == null) {
            player.sendMessage(EvonyaPlugin.PREFIX + "Ce joueur n'est pas connecté.");
            return true;
        }

        if (!target.isPrivateMessage()) {
            player.sendMessage(EvonyaPlugin.PREFIX + "Ce joueur ne &creçoit pas&f les messages privés.");
            return true;
        }

        final StringBuilder sb = new StringBuilder();
        Arrays.stream(args).forEach(arg -> sb.append(arg).append(" "));

        MessageCommand.messagePlayer(player, target, sb);
        return true;
    }
}
