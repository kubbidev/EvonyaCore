package me.kubbidev.evonyacore.menu.panel.host;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.game.core.GameInstance;
import me.kubbidev.evonyacore.menu.MenuBorder;
import me.kubbidev.evonyacore.menu.PaginatedMenu;
import me.kubbidev.evonyacore.menu.PlayerMenuUtility;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.evonyacore.utils.Head;
import me.kubbidev.evonyacore.utils.HeadType;
import me.kubbidev.evonyacore.utils.Item;
import me.kubbidev.nexuspowered.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WhitelistPanel extends PaginatedMenu {

    public WhitelistPanel(PlayerMenuUtility playerMenuUtility, EvonyaPlugin plugin) {
        super(playerMenuUtility, plugin);
    }

    @Override
    public String getMenuName() {
        return "&f(&c!&f) &cWhitelist";
    }

    @Override
    public int getRows() {
        return 6;
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
        return MenuBorder.LINE;
    }

    @Override
    public ItemStack getBorderMaterial() {
        return ItemStackBuilder.of(Material.STAINED_GLASS_PANE).data(7).build();
    }

    private List<String> whitelist;

    @Override
    public void setMenuItems() {
        usernames.clear();
        final GameInstance gameInstance = playerMenuUtility.getOwner().getGameInstance();
        final String host = gameInstance.getHost().getUsername();

        whitelist = gameInstance.getWhitelist().stream().filter(s -> !host.equals(s)).collect(Collectors.toList());

        final int actualPage = (page + 1);
        final int whitelistSize = whitelist.size();
        final int maxItems = super.getMaxItemsPerPage();

        final int division = (whitelistSize / maxItems);

        int maxPage = division + 1;

        if ((whitelistSize % maxItems == 0) && whitelistSize != 0)
            maxPage--;

        inventory.setItem(4, new Head("&8┃&c Ajouter un joueur").setHeadType(HeadType.RED_PLUS).parseItem());
        inventory.setItem(53, new Item(Material.ARROW).setName("&8┃&f Revenir en &carrière").parseItem());
        inventory.setItem(49, new Item(Material.PAPER).setName("&8┃&f Page &c" + actualPage + "&f/&c" + maxPage).setLore(Arrays.asList(
                "",
                "  &8┃&c Clic-Gauche&f pour aller à la page suivante.",
                "  &8┃&c Clic-Droit&f pour aller à la page précédente.",
                ""
        )).parseItem());

        for (int i = 0; i < maxItems; i++) {
            index = maxItems * page + i;

            if (index >= whitelistSize)
                break;

            final String userName = whitelist.get(index);
            final String name = "&8┃&6 " + userName;
            final ItemStack itemStack = new Item(Material.SKULL_ITEM).setName(name).parseItem();
            final ItemMeta itemMeta = itemStack.getItemMeta();
            usernames.put(itemMeta.getDisplayName(), userName);

            inventory.setItem(getIndex(), itemStack);
        }
    }

    private final Map<String, String> usernames = new HashMap<>();

    @Override
    public void updateMenu() {

    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        final EvonyaPlayer player = PlayerManager.wrapEvonyaPlayer((Player) event.getWhoClicked());
        final ItemStack itemStack = event.getCurrentItem();
        final ClickType clickType = event.getClick();
        final Material material = itemStack.getType();

        if (itemStack.getType() == Material.STAINED_GLASS_PANE)
            return;

        if (material == Material.ARROW) {
            new SlotsPanel(playerMenuUtility, plugin).open();
            return;
        }
        final GameInstance gameInstance = playerMenuUtility.getOwner().getGameInstance();

        switch (itemStack.getType()) {
            case PAPER:
                switch (clickType) {
                    case LEFT:
                        if ((index + 1) < whitelist.size()) {
                            page++;
                            super.open();
                        }
                        break;
                    case RIGHT:
                        if (page != 0) {
                            page--;
                            super.open();
                        }
                        break;
                }
                break;
            case SKULL_ITEM:
                String skullName = itemStack.getItemMeta().getDisplayName();
                String username = usernames.get(skullName);
                if (username == null) {
                    new WhitelistAddingPanel(playerMenuUtility, plugin).open();
                } else {
                    gameInstance.getWhitelist().remove(username);
                    player.sendMessage(EvonyaPlugin.PREFIX + "Whitelist &8»&f Vous venez de supprimer &a" + username + "&f.");
                    super.open();
                }
                break;
        }
    }
}
