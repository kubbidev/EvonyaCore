package me.kubbidev.evonyacore.menu.panel.game;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.exceptions.GameDoesNotExistException;
import me.kubbidev.evonyacore.game.GameManager;
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

import java.util.*;

public class GamePanel extends PaginatedMenu {

    public GamePanel(PlayerMenuUtility playerMenuUtility, EvonyaPlugin plugin) {
        super(playerMenuUtility, plugin);
    }

    @Override
    public String getMenuName() {
        return "&f(&c!&f) &cDemon Slayer";
    }

    @Override
    public int getRows() {
        return 6;
    }

    @Override
    public boolean isUpdated() {
        return true;
    }

    @Override
    public long getTicksUpdate() {
        return 20;
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

        final int actualPage = (page + 1);
        final int demonslayer = GameManager.getGamesInstance().size();
        final int maxItems = super.getMaxItemsPerPage();

        final int division = (demonslayer / maxItems);

        int maxPage = division + 1;

        if ((demonslayer % maxItems == 0) && demonslayer != 0)
            maxPage--;

        inventory.setItem(53, new Item(Material.ARROW).setName("&8┃&f Revenir en &carrière").parseItem());
        inventory.setItem(49, new Item(Material.PAPER).setName("&8┃&f Page &c" + actualPage + "&f/&c" + maxPage).setLore(Arrays.asList(
                "",
                " &8┃&c Clic-Gauche &fpour aller à la page suivante.",
                " &8┃&c Clic-Droit &fpour aller à la page précédente.",
                ""
        )).parseItem());
    }

    @Override
    public void updateMenu() {
        this.games.clear();
        final List<GameInstance> gameInstances = GameManager.getGamesInstance();

        for (int i = 0; i < getMaxItemsPerPage(); i++) {
            index = getMaxItemsPerPage() * page + i;

            if (index >= gameInstances.size())
                break;

            final GameInstance gameInstance = gameInstances.get(index);

            final HeadType headType;
            final String name = "&8┃&6 demonslayer-" + gameInstance.getId();
            final List<String> lore = new ArrayList<>();

            lore.add(" ");
            lore.add("  &8»&f Host : &c" + gameInstance.getHost().getUsername());
            lore.add("  &8»&f Joueurs : &c" + gameInstance.getPlayers().size() + "&f/&c" + gameInstance.getSlots());
            lore.add(" ");
            lore.add("  &8┃&f Scénarios :");

            final int scenarios = gameInstance.getScenarioManager().getRegisteredScenarios().size();
            int iter = 0;

            while (iter < scenarios) {
                if (iter < 5) {
                    lore.add("    &8•&c " + gameInstance.getScenarioManager().getRegisteredScenarios().get(iter).getInfo().getName());
                }
                if (iter >= 5) {
                    lore.add("     &7&o+" + (gameInstance.getPlayers().size() - 5) + " autres...");
                    break;
                }
                iter++;
            }
            lore.add(" ");
            lore.add("  &8•&f Status : " + gameInstance.getGameState().getName());
            lore.add("  &8•&f File d'attente : " + gameIsOpen(gameInstance));
            lore.add(" ");
            lore.add("  &8┃&f Rejoindre :");
            lore.add("  &8┃&c clic-droit&f ou &cclic-gauche");
            lore.add(" ");
            if (gameInstance.getWhitelist().contains(playerMenuUtility.getOwner().getUsername()))
                lore.add("&7Vous êtes whitelist sur ce serveur.");

            switch (gameInstance.getGameState()) {
                case LOADING:
                    headType = HeadType.BLUE;
                    break;
                case STARTED:
                case ENDED:
                    headType = HeadType.RED;
                    break;
                case WAITING:
                case STARTING:
                    headType = HeadType.LIME;
                    break;
                default:
                    continue;
            }
            final int amount = gameInstance.getPlayers().size();

            final ItemStack itemStack = new Head(name).setHeadType(headType).setLore(lore).setAmount(amount == 0 ? 1 : amount).parseItem();
            final ItemMeta itemMeta = itemStack.getItemMeta();
            games.put(itemMeta.getDisplayName(), gameInstance.getUniqueId());
            inventory.setItem(getIndex(), itemStack);
        }
    }

    private final Map<String, UUID> games = new HashMap<>();

    private String gameIsOpen(GameInstance gameInstance) {
        if (gameInstance.isOpen())
            return "&aOuverte";
        return "&cFermée";
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        final EvonyaPlayer player = PlayerManager.wrapEvonyaPlayer((Player) event.getWhoClicked());
        final ItemStack itemStack = event.getCurrentItem();
        final String name = itemStack.getItemMeta().getDisplayName();
        final ClickType clickType = event.getClick();
        final Material material = itemStack.getType();

        if (material == Material.ARROW) {
            new MainSelectorPanel(playerMenuUtility, plugin).open();
        } else {
            UUID uuid = games.get(name);
            if (uuid != null) {
                final GameInstance gameInstance;
                try {
                    gameInstance = GameManager.getGameInstance(uuid);
                } catch (GameDoesNotExistException e) {
                    throw new RuntimeException(e);
                }
                plugin.getQueueSystem().connect(player, gameInstance);
                player.closeInventory();
            }
        }
        if (event.getCurrentItem().getType() == Material.PAPER) {
            switch (clickType) {
                case LEFT:
                    if ((index + 1) < GameManager.getGamesInstance().size()) {
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
        }
    }
}