package me.kubbidev.evonyacore.menu.panel.game;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.game.GameManager;
import me.kubbidev.evonyacore.menu.Menu;
import me.kubbidev.evonyacore.menu.MenuBorder;
import me.kubbidev.evonyacore.menu.PlayerMenuUtility;
import me.kubbidev.evonyacore.menu.panel.HubPanel;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
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

public class MainSelectorPanel extends Menu {

    public MainSelectorPanel(PlayerMenuUtility playerMenuUtility, EvonyaPlugin plugin) {
        super(playerMenuUtility, plugin);
    }

    @Override
    public String getMenuName() {
        return "&f(&c!&f) &cNaviguation";
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
        return MenuBorder.CORNER;
    }

    @Override
    public ItemStack getBorderMaterial() {
        return ItemStackBuilder.of(Material.STAINED_GLASS_PANE).data(7).build();
    }

    @Override
    public void setMenuItems() {

        inventory.setItem(3, new Head("&8┃ &9Discord").setHeadType(HeadType.DISCORD).parseItem());
        inventory.setItem(4, new Head("&8┃ &cComing soon...").setHeadType(HeadType.TWITTER).parseItem());
        inventory.setItem(5, new Item(Material.PAPER).setName("&8┃ &cComing soon...").parseItem());
        inventory.setItem(13, new Item(Material.FEATHER).setName("&8┃ &cComing soon...").parseItem());

        inventory.setItem(18, new Head("&8┃ &aHubs").setHeadType(HeadType.EARTH).parseItem());
        inventory.setItem(27, new Head("&8┃ &6Lancer mon serveur").setHeadType(HeadType.SERVER).setLore(Arrays.asList(
                "",
                " &8»&f Un grade est &cnécéssaire",
                " &8»&f pour pouvoir lancer un &eserveur&f.",
                ""
        )).parseItem());
        ItemStack comingSoon = ItemStackBuilder.of(Material.SKULL_ITEM).data(1).name("&8┃ &cComing soon...").build();
        inventory.setItem(20, comingSoon);
        inventory.setItem(21, comingSoon);
        inventory.setItem(23, comingSoon);

        inventory.setItem(24, new Head("&8┃ &6&lDemon Slayer").setHeadType(HeadType.TANJIRO).setLore(Arrays.asList(
                "",
                "  &8┃&f Mode de jeu &cbataille générale",
                "  &8┃&f incarnez vos &cpersonnages&f préférés",
                "  &8┃&f de l'anime dans ce mode de jeu.",
                "",
                " &8»&f Joueurs dans le mode : &c&l" + GameManager.getGamesInstance().stream().mapToInt(game -> game.getPlayers().size()).sum(),
                " &8»&f Auteur(s) : &ckubbidev",
                " &8»&f Développeur : &ckubbidev",
                "",
                "  &8•&f Accès : &cFile d'attente.",
                "  &8•&f Jeu : &aPublic",
                "",
                " &8»&f Cliquez pour voir les &cserveurs.",
                ""
        )).parseItem());
        inventory.setItem(29, comingSoon);
        inventory.setItem(30, comingSoon);
        inventory.setItem(32, comingSoon);
        inventory.setItem(33, comingSoon);

        inventory.setItem(49, new Head("&8┃ &c&lCity World").setHeadType(HeadType.CITY).setLore(Arrays.asList(
                "",
                "  &8┃&f Mode de jeu de &aconstruction",
                "  &8┃&f développer vos meilleures &cidées",
                "  &8┃&f imaginable dans ce mode de jeu.",
                "",
                " &8»&f Joueurs dans le mode : &c&l" + plugin.getCity().getPlayers().size(),
                " &8»&f Auteur(s) : &cVelonoisette",
                " &8»&f Développeur : &cAlkaaz",
                "",
                "  &8•&f Accès : &cFermé.",
                "  &8•&f Jeu : &cPrivé",
                "",
                " &8»&f Cliquez pour &crejoindre&f.",
                ""
        )).parseItem());
    }

    @Override
    public void updateMenu() {
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        final EvonyaPlayer player = PlayerManager.wrapEvonyaPlayer((Player) event.getWhoClicked());
        final String name = event.getCurrentItem().getItemMeta().getDisplayName();

        if (name.equalsIgnoreCase(Color.translate("&8┃ &aHubs"))) {
            new HubPanel(playerMenuUtility, plugin).open();
        }
        else if (name.equalsIgnoreCase(Color.translate("&8┃ &6&lDemon Slayer"))) {
            new GamePanel(playerMenuUtility, plugin).open();
        }
        else if (name.equalsIgnoreCase(Color.translate("&8┃ &6Lancer mon serveur"))) {
            new GameCreationPanel(playerMenuUtility, plugin).open();
        }
        else if (name.equalsIgnoreCase(Color.translate("&8┃ &c&lCity World"))) {
            plugin.getCityFunction().cityEnter(playerMenuUtility.getOwner());
        }
        else if (name.equalsIgnoreCase(Color.translate("&8┃ &9Discord"))) {
            player.performCommand("discord");
            player.closeInventory();
        }
    }
}
