package me.kubbidev.evonyacore.menu.panel.host;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.exceptions.EvonyaPlayerDoesNotExistException;
import me.kubbidev.evonyacore.game.core.GameInstance;
import me.kubbidev.evonyacore.menu.MenuBorder;
import me.kubbidev.evonyacore.menu.PaginatedMenu;
import me.kubbidev.evonyacore.menu.PlayerMenuUtility;
import me.kubbidev.evonyacore.players.EPlayer;
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

import java.util.*;
import java.util.stream.Collectors;

public class HostPanel extends PaginatedMenu {

    public HostPanel(PlayerMenuUtility playerMenuUtility, EvonyaPlugin plugin) {
        super(playerMenuUtility, plugin);
    }

    @Override
    public String getMenuName() {
        return "&f(&c!&f) &cCo-host";
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

    private List<EPlayer> coHosts;

    @Override
    public void setMenuItems() {
        coHostsd.clear();
        final GameInstance gameInstance = playerMenuUtility.getOwner().getGameInstance();
        final EPlayer host = gameInstance.getHost();

        coHosts = gameInstance.getCoHost().stream().filter(p -> !host.equals(p)).collect(Collectors.toList());

        final int actualPage = (page + 1);
        final int coHostsSize = coHosts.size();
        final int maxItems = super.getMaxItemsPerPage();

        final int division = (coHostsSize / maxItems);

        int maxPage = division + 1;

        if ((coHostsSize % maxItems == 0) && coHostsSize != 0)
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

            if (index >= coHostsSize)
                break;

            final EPlayer coHost = coHosts.get(index);
            final String name = "&8┃&6 " + coHost.getUsername();
            final ItemStack itemStack = new Item(Material.SKULL_ITEM).setName(name).parseItem();
            final ItemMeta itemMeta = itemStack.getItemMeta();
            coHostsd.put(itemMeta.getDisplayName(), coHost.getUniqueId());

            inventory.setItem(getIndex(), itemStack);
        }
    }

    private final Map<String, UUID> coHostsd = new HashMap<>();

    @Override
    public void updateMenu() {

    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        final EPlayer player = PlayerManager.wrapPlayer((Player) event.getWhoClicked());
        final ItemStack itemStack = event.getCurrentItem();
        final ClickType clickType = event.getClick();
        final Material material = itemStack.getType();

        if (itemStack.getType() == Material.STAINED_GLASS_PANE)
            return;

        if (material == Material.ARROW) {
            new SlotsPanel(playerMenuUtility, plugin).open();
            return;
        }
        switch (itemStack.getType()) {
            case PAPER:
                switch (clickType) {
                    case LEFT:
                        if ((index + 1) < coHosts.size()) {
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
                UUID cohostUUID = coHostsd.get(skullName);
                if (cohostUUID == null) {
                    new HostAddingPanel(playerMenuUtility, plugin).open();
                } else {
                    final EPlayer coHost;
                    try {
                        coHost = PlayerManager.wrapPlayer(cohostUUID);
                    } catch (EvonyaPlayerDoesNotExistException e) {
                        throw new RuntimeException(e);
                    }
                    coHosts.remove(coHost);
                    player.sendMessage(EvonyaPlugin.PREFIX + "Co-host &8»&f Vous venez de supprimer &a" + coHost.getUsername() + "&f.");
                    super.open();
                }
                break;
        }
    }
}
