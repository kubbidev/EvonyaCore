package me.kubbidev.evonyacore.commands;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.exceptions.EvonyaPlayerDoesNotExistException;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.evonyacore.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class MessageCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {

        if (!(sender instanceof Player)) {
            EvonyaPlugin.LOGGER.warning("Only players can type this command!");
            return true;
        }
        final EvonyaPlayer player = PlayerManager.wrapEvonyaPlayer((Player) sender);

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("on")) {
                if (player.isPrivateMessage()) {
                    player.sendMessage(EvonyaPlugin.PREFIX + "Vous avez déjà &aactivé&f vos messages privés.");
                    return true;
                }
                player.setPrivateMessage(true);
                player.sendMessage(EvonyaPlugin.PREFIX + "Vous venez d'&aactiver&f vos messages privés.");
            } else if (args[0].equalsIgnoreCase("off")) {
                if (!player.isPrivateMessage()) {
                    player.sendMessage(EvonyaPlugin.PREFIX + "Vous avez déjà &cdésactivé&f vos messages privés.");
                    return true;
                }
                player.setPrivateMessage(false);
                player.sendMessage(EvonyaPlugin.PREFIX + "Vous venez de &cdésactiver&f vos messages privés.");

            } else sendHelpMessage(player);

        } else if (args.length >= 2) {
            final String targetName = args[0];
            final EvonyaPlayer target;
            try {
                target = PlayerManager.wrapEvonyaPlayer(targetName);
            } catch (EvonyaPlayerDoesNotExistException e) {
                player.sendMessage(EvonyaPlugin.PREFIX + "Ce joueur n'est pas connecté...");
                return true;
            }

            if (target == player) {
                player.sendMessage(EvonyaPlugin.PREFIX + "Vous ne pouvez pas &cenvoyer&f de messages à vous même.");
                return true;
            }

            if (!target.isPrivateMessage()) {
                player.sendMessage(EvonyaPlugin.PREFIX + "Ce joueur ne &creçoit pas&f les messages privés.");
                return true;
            }

            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                if (i > 0) sb.append(args[i]).append(" ");
            }
            messagePlayer(player, target, sb);

        } else sendHelpMessage(player);

        return true;
    }

    public static void messagePlayer(EvonyaPlayer player, EvonyaPlayer target, StringBuilder sb) {
        final String roles = target.getPlayerRank().getScoreBoardPrefix();
        final String name = target.getUsername();

        final String roles1 = player.getPlayerRank().getScoreBoardPrefix();
        final String name1 = player.getUsername();

        final String message = "&cmoi ➠&r " + roles + name + " &8»&6 ";
        final String message1 = roles1 + name1 + " &c➠ moi &8»&6 ";
        player.sendMessage(message + sb);
        target.sendMessage(message1 + sb);

        player.setLastPrivateMessage(target.getUniqueId());
        target.setLastPrivateMessage(player.getUniqueId());
    }

    public static void sendHelpMessage(EvonyaPlayer player) {

        player.sendMessage(" ");
        player.sendMessage(" &8┃ &c&lMessages - Commandes disponibles");
        new Message("    &8•&c /msg <pseudo> <message> &f\u00BB&7 Envoie un message à un joueur.").setCommand("/msg <pseudo> <message>").setHoverMessage("Cliquez pour exécuter").sendSuggestCommand(player.getPlayer());
        new Message("    &8•&c /msg on/off &f\u00BB&7 Active ou désactive la réception des &7messages.").setCommand("/msg <on/off>").setHoverMessage("Cliquez pour exécuter").sendSuggestCommand(player.getPlayer());
        new Message("    &8•&c /r <message> &f\u00BB&7 Réponds au dernier joueur qui vous a &7envoyé un message.").setCommand("/r <message>").setHoverMessage("Cliquez pour exécuter").sendSuggestCommand(player.getPlayer());
        player.sendMessage(" ");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String string, String[] args) {
        if (args.length == 1) {
            final List<String> message = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());

            message.add("on");
            message.add("off");
            return message;
        }
        return null;
    }
}
