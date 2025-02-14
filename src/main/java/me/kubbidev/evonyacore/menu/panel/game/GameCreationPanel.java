package me.kubbidev.evonyacore.menu.panel.game;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.game.GameManager;
import me.kubbidev.evonyacore.game.core.GameInstance;
import me.kubbidev.evonyacore.menu.Menu;
import me.kubbidev.evonyacore.menu.MenuBorder;
import me.kubbidev.evonyacore.menu.PlayerMenuUtility;
import me.kubbidev.evonyacore.players.EPlayer;
import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.evonyacore.utils.Head;
import me.kubbidev.evonyacore.utils.HeadType;
import me.kubbidev.evonyacore.utils.Item;
import me.kubbidev.nexuspowered.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class GameCreationPanel extends Menu {

    private final GameManager gameManager;

    public GameCreationPanel(PlayerMenuUtility playerMenuUtility, EvonyaPlugin plugin) {
        super(playerMenuUtility, plugin);

        this.gameManager = plugin.getGameManager();
    }

    @Override
    public String getMenuName() {
        return "&f(&c!&f) &cLancer mon serveur";
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public boolean isUpdated() {
        return false;
    }

    @Override
    public long getTicksUpdate() {
        return 0;
    }

    @Override
    public MenuBorder getBorder() {
        return MenuBorder.CORNER;
    }

    @Override
    public ItemStack getBorderMaterial() {
        return ItemStackBuilder.of(Material.STAINED_GLASS_PANE).data(14).build();
    }

    @Override
    public void setMenuItems() {

        inventory.setItem(26, new Item(Material.ARROW).setName("&8┃&f Revenir en &carrière").parseItem());
        inventory.setItem(13, new Head("&8┃ &6&lDemon Slayer").setHeadType(HeadType.TANJIRO).setLore(Arrays.asList(
                "",
                "  &8•&f Prix : &c1 Host",
                "  &8•&f Temps estimé : &a15s",
                "",
                " &8»&f Cliquez pour créer votre &cserveur.",
                "")).parseItem());
    }

    @Override
    public void updateMenu() {
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        final EPlayer player = PlayerManager.wrapPlayer((Player) event.getWhoClicked());
        final ItemStack itemStack = event.getCurrentItem();
        final String name = itemStack.getItemMeta().getDisplayName();
        final Material material = itemStack.getType();

        if (name.equalsIgnoreCase(Color.translate("&8┃ &6&lDemon Slayer"))) {
            if (player.getHosts() <= 0) {
                player.closeInventory();
                player.sendMessage(EvonyaPlugin.PREFIX + "Vous n'avez pas accès aux &chosts&f!");
                return;
            }
            for (GameInstance gameInstance : GameManager.getGamesInstance()) {
                if (gameInstance.getHost().equals(player)) {
                    player.sendMessage(EvonyaPlugin.PREFIX + "Vous disposez déjà d'une partie &aen attente&f.");
                    return;
                }
            }
            gameManager.createGameInstance(player);

            player.sendMessage(EvonyaPlugin.PREFIX + "Votre serveur est &aen cours&f de création.");
            player.closeInventory();
        }
        else if (material == Material.ARROW) {
            new MainSelectorPanel(playerMenuUtility, plugin).open();
        }
    }
}
