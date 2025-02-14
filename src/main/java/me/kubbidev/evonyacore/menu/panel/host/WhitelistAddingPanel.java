package me.kubbidev.evonyacore.menu.panel.host;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.game.core.GameInstance;
import me.kubbidev.evonyacore.menu.AnvilMenu;
import me.kubbidev.evonyacore.menu.PlayerMenuUtility;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.evonyacore.utils.Item;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;

import java.util.Collections;

public class WhitelistAddingPanel extends AnvilMenu {

    public WhitelistAddingPanel(PlayerMenuUtility playerMenuUtility, EvonyaPlugin plugin) {
        super(playerMenuUtility, plugin);
    }

    @Override
    public void open() {
        BUILDER.onClick((slot, stateSnapshot) -> {
            if (slot != AnvilGUI.Slot.OUTPUT) {
                return Collections.emptyList();
            }

            String text = stateSnapshot.getText();
            EvonyaPlayer player = PlayerManager.wrapEvonyaPlayer(stateSnapshot.getPlayer());

            final GameInstance gameInstance = player.getGameInstance();
            if (gameInstance.getWhitelist().contains(text)) {
                player.sendMessage(EvonyaPlugin.PREFIX + "Ce joueur fait déjà partie de la &cwhitelist&f.");
                return Collections.singletonList(AnvilGUI.ResponseAction.run(() -> new WhitelistPanel(playerMenuUtility, plugin).open()));
            }
            gameInstance.getWhitelist().add(text);
            player.sendMessage(EvonyaPlugin.PREFIX + "Whitelist &8»&f Vous venez d'ajouter &a" + text + "&f.");

            return Collections.singletonList(AnvilGUI.ResponseAction.run(() -> new WhitelistPanel(playerMenuUtility, plugin)));
        }).itemLeft(new Item(Material.PAPER).parseItem()).text("Entrer un pseudo").title("Ajouter un joueur").plugin(plugin).open(playerMenuUtility.getOwner().getPlayer());
    }
}
