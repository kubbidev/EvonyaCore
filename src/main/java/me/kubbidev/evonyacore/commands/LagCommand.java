package me.kubbidev.evonyacore.commands;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.players.EPlayer;
import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.nexuspowered.util.Tps;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LagCommand implements CommandExecutor {

    final SimpleDateFormat sdfYear = new SimpleDateFormat("dd/MM/yy");
    final SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm:ss");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {

        String tps = Tps.read().toString();
        if (sender instanceof ConsoleCommandSender) {
            EvonyaPlugin.LOGGER.info(" ");
            EvonyaPlugin.LOGGER.info(" INFOS ┃ EVONYA");
            EvonyaPlugin.LOGGER.info(" ");
            EvonyaPlugin.LOGGER.info(" ▪ Date: " + sdfYear.format(Calendar.getInstance().getTime()) + "  " + sdfHour.format(Calendar.getInstance().getTime()));
            EvonyaPlugin.LOGGER.info(" ▪ Joueurs: " + Bukkit.getOnlinePlayers().size());
            EvonyaPlugin.LOGGER.info(" ▪ TPS: " + tps);
            EvonyaPlugin.LOGGER.info(" ▪ Ton ping: N/A");
            EvonyaPlugin.LOGGER.info(" ");
        }

        if (sender instanceof Player) {
            final EPlayer player = PlayerManager.wrapPlayer((Player) sender);

            player.sendMessage(" ");
            player.sendMessage(" &c&lINFOS ┃ EVONYA");
            player.sendMessage(" ");
            player.sendMessage(" &8▪&f Date: &6" + sdfYear.format(Calendar.getInstance().getTime()) + "  " + sdfHour.format(Calendar.getInstance().getTime()));
            player.sendMessage(" &8▪&f Joueurs: &6" + Bukkit.getOnlinePlayers().size());
            player.sendMessage(" &8▪&f TPS: &a" + tps);
            player.sendMessage(" &8▪&f Ton ping: &a" + player.getPing() + "ms");
            player.sendMessage(" ");

            HandlerList.getRegisteredListeners(EvonyaPlugin.INSTANCE).stream().map(rListener -> rListener.getListener().toString()).forEach(player::sendMessage);
        }

        return true;
    }
}
