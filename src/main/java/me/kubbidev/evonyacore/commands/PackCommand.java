package me.kubbidev.evonyacore.commands;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.menu.PlayerMenuManager;
import me.kubbidev.evonyacore.menu.panel.ResourcePackPanel;
import me.kubbidev.evonyacore.players.EPlayer;
import me.kubbidev.evonyacore.players.PlayerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PackCommand implements CommandExecutor {

    private final EvonyaPlugin plugin;

    public PackCommand(EvonyaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            EvonyaPlugin.LOGGER.warning("Only players can type this command!");
            return true;
        }
        final EPlayer player = PlayerManager.wrapPlayer((Player) sender);
        new ResourcePackPanel(PlayerMenuManager.getPlayerMenuUtility(player), plugin).open();

        return true;
    }
}
