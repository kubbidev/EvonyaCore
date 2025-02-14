package me.kubbidev.evonyacore.menu.panel.host;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.exceptions.EvonyaPlayerDoesNotExistException;
import me.kubbidev.evonyacore.game.core.GameInstance;
import me.kubbidev.evonyacore.menu.AnvilMenu;
import me.kubbidev.evonyacore.menu.PlayerMenuUtility;
import me.kubbidev.evonyacore.players.EPlayer;
import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.evonyacore.utils.Item;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;

import java.util.Collections;

public class HostAddingPanel extends AnvilMenu {

    public HostAddingPanel(PlayerMenuUtility playerMenuUtility, EvonyaPlugin plugin) {
        super(playerMenuUtility, plugin);
    }

    @Override
    public void open() {
        BUILDER.onClick((slot, stateSnapshot) -> {
            if (slot != AnvilGUI.Slot.OUTPUT) {
                return Collections.emptyList();
            }

            String text = stateSnapshot.getText();
            EPlayer player = PlayerManager.wrapPlayer(stateSnapshot.getPlayer());

            final GameInstance gameInstance = player.getGameInstance();
            final EPlayer coHost;
            try {
                coHost = PlayerManager.wrapPlayer(text);
            } catch (EvonyaPlayerDoesNotExistException e) {
                player.sendMessage(EvonyaPlugin.PREFIX + "Ce joueur n'est pas connecté...");
                return Collections.singletonList(AnvilGUI.ResponseAction.close());
            }
            if (gameInstance.getCoHost().contains(coHost)) {
                player.sendMessage(EvonyaPlugin.PREFIX + "Ce joueur fait déjà partie des &cco-host&f.");
                return Collections.singletonList(AnvilGUI.ResponseAction.run(() -> new HostPanel(playerMenuUtility, plugin).open()));
            }
            gameInstance.getCoHost().add(coHost);
            player.sendMessage(EvonyaPlugin.PREFIX + "Co-host &8»&f Vous venez d'ajouter &a" + coHost.getUsername() + "&f.");

            return Collections.singletonList(AnvilGUI.ResponseAction.run(() -> new HostPanel(playerMenuUtility, plugin).open()));
        }).itemLeft(new Item(Material.PAPER).parseItem()).text("Entrer un pseudo").title("Ajouter un joueur").plugin(plugin).open(playerMenuUtility.getOwner().getPlayer());
    }
}
