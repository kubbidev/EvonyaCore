package me.kubbidev.evonyacore.listeners;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.LobbyFunction;
import me.kubbidev.evonyacore.menu.PlayerMenuManager;
import me.kubbidev.evonyacore.menu.PlayerMenuUtility;
import me.kubbidev.evonyacore.menu.panel.game.MainSelectorPanel;
import me.kubbidev.evonyacore.menu.panel.host.SlotsPanel;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
import me.kubbidev.evonyacore.players.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ItemInteractListener implements Listener {

    private final LobbyFunction lobbyFunction;
    private final EvonyaPlugin plugin;

    public ItemInteractListener(LobbyFunction lobbyFunction, EvonyaPlugin plugin) {
        this.lobbyFunction = lobbyFunction;
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final EvonyaPlayer player = PlayerManager.wrapEvonyaPlayer(event.getPlayer());
        final ItemStack itemStack = event.getItem();

        if (itemStack == null)
            return;

        final PlayerMenuUtility playerMenuUtility = PlayerMenuManager.getPlayerMenuUtility(player);

        if (player.getWorld().equals(plugin.getLobby())) {

            switch (itemStack.getType()) {
                case COMPASS:
                    new MainSelectorPanel(playerMenuUtility, plugin).open();
                    break;
                case BARRIER:
                    player.performCommand("quit");
                    lobbyFunction.giveLobbyItems(player);
                    break;
            }
        }
        else if (player.hasGameInstance()) {

            switch (itemStack.getType()) {
                case NETHER_STAR:
                    if (player.isHost())
                        new SlotsPanel(playerMenuUtility, plugin).open();
                    break;
                case DARK_OAK_DOOR:
                    lobbyFunction.lobbyEnter(player);
                    break;
            }
        }
    }
}
