package me.kubbidev.evonyacore.commands;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.exceptions.EvonyaPlayerDoesNotExistException;
import me.kubbidev.evonyacore.menu.PlayerMenuManager;
import me.kubbidev.evonyacore.menu.panel.GameStatsPanel;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
import me.kubbidev.evonyacore.players.PlayerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand implements CommandExecutor {

    private final EvonyaPlugin plugin;

    public StatsCommand(EvonyaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            EvonyaPlugin.LOGGER.warning("Only players can type this command!");
            return true;
        }
        final EvonyaPlayer player = PlayerManager.wrapEvonyaPlayer((Player) sender);

        if (args.length == 0) {
            new GameStatsPanel(PlayerMenuManager.getPlayerMenuUtility(player), plugin).open();
        }
        else if (args.length == 1) {
            final String targetName = args[0];
            final EvonyaPlayer target;
            try {
                target = PlayerManager.wrapEvonyaPlayer(targetName);
            } catch (EvonyaPlayerDoesNotExistException e) {
                player.sendMessage(EvonyaPlugin.PREFIX + "Ce joueur n'est pas connecté...");
                return true;
            }

            new GameStatsPanel(PlayerMenuManager.getPlayerMenuUtility(target), plugin).open();
        }
        else player.sendMessage(EvonyaPlugin.PREFIX + "Un problème ? (&cstats&f) + (&cjoueur&f)");

        return true;
    }
}
