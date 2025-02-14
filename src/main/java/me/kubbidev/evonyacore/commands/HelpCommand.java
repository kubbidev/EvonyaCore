package me.kubbidev.evonyacore.commands;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.evonyacore.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {

        Object[] e = new Object[4];
        e[0] = "discord";
        e[1] = "https://discord.gg/";
        e[2] = "username";
        e[3] = "kubbidev";

        for (int i = 0; (i + 1) < e.length; i += 2) {
            Object o = e[i];
            Object a = e[i + 1];
            assert o != null : "o";
            assert a != null : "a";
        }

        if(!(sender instanceof Player)) {
            EvonyaPlugin.LOGGER.warning("Only players can type this command!");
            return true;
        }

        final EvonyaPlayer player = PlayerManager.wrapEvonyaPlayer((Player) sender);

        player.sendMessage(" ");
        player.sendMessage(" &8┃ &c&lAide - Page 1/1");
        new Message("    &8•&c /discord &f\u00BB&7 Affiche le discord.").setCommand("/discord").setHoverMessage("Cliquez pour exécuter").sendSuggestCommand(player.getPlayer());
        new Message("    &8•&c /hub &f\u00BB&7 Se téléporter au hub.").setCommand("/hub").setHoverMessage("Cliquez pour exécuter").sendSuggestCommand(player.getPlayer());
        new Message("    &8•&c /join &f\u00BB&7 Rejoindre la file d'attente.").setCommand("/join <id>").setHoverMessage("Cliquez pour exécuter").sendSuggestCommand(player.getPlayer());
        new Message("    &8•&c /lag &f\u00BB&7 Voir les TPS du serveur ainsi que votre ping.").setCommand("/lag").setHoverMessage("Cliquez pour exécuter").sendSuggestCommand(player.getPlayer());
        new Message("    &8•&c /msg &f\u00BB&7 Envoyer un message à un autre joueur connecté.").setCommand("/msg <pseudo> <message>").setHoverMessage("Cliquez pour exécuter").sendSuggestCommand(player.getPlayer());
        new Message("    &8•&c /pack &f\u00BB&7 Seléctionnez et activez le texture pack cliqué.").setCommand("/pack").setHoverMessage("Cliquez pour exécuter").sendSuggestCommand(player.getPlayer());
        new Message("    &8•&c /quit &f\u00BB&7 Quitter la file d'attente.").setCommand("/quit").setHoverMessage("Cliquez pour exécuter").sendSuggestCommand(player.getPlayer());
        new Message("    &8•&c /stats &f\u00BB&7 Affichez vos statistiques.").setCommand("/stats").setHoverMessage("Cliquez pour exécuter").sendSuggestCommand(player.getPlayer());
        player.sendMessage(" ");
        return true;
    }
}
