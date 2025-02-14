package me.kubbidev.evonyacore.menu.panel;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.menu.Menu;
import me.kubbidev.evonyacore.menu.MenuBorder;
import me.kubbidev.evonyacore.menu.PlayerMenuUtility;
import me.kubbidev.evonyacore.menu.panel.game.MainSelectorPanel;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.evonyacore.players.Rank;
import me.kubbidev.evonyacore.utils.Head;
import me.kubbidev.evonyacore.utils.HeadType;
import me.kubbidev.evonyacore.utils.Item;
import me.kubbidev.nexuspowered.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HubPanel extends Menu {

    public HubPanel(PlayerMenuUtility playerMenuUtility, EvonyaPlugin plugin) {
        super(playerMenuUtility, plugin);
    }

    @Override
    public String getMenuName() {
        return "&f(&c!&f) &cHubs";
    }

    @Override
    public int getRows() {
        return 4;
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
        inventory.setItem(35, new Item(Material.ARROW).setName("&8┃&f Revenir en &carrière").parseItem());
    }

    @Override
    public void updateMenu() {

        final int amount = plugin.getLobby().getPlayers().size();
        final List<String> lore = new ArrayList<>();

        lore.add("");
        lore.add("  &8┃&f Joueurs : &c" + amount);
        lore.add("  &8┃&f Population : " + getPopulation());
        lore.add("");
        Arrays.stream(Rank.values()).filter(ranks -> getRank(ranks) > 0).map(ranks -> "     &8• " + ranks.getPrefix() + " &f: " + getRank(ranks)).forEach(lore::add);

        lore.add("");
        lore.add(" &8»&f Cliquez pour &crejoindre&f.");
        lore.add("");


        inventory.setItem(10, new Head("&8┃ &chub-1").setHeadType(HeadType.LIME).setLore(lore).setAmount(amount).parseItem());
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        final EvonyaPlayer player = PlayerManager.wrapEvonyaPlayer((Player) event.getWhoClicked());
        final ItemStack itemStack = event.getCurrentItem();
        final String name = itemStack.getItemMeta().getDisplayName();
        final Material material = itemStack.getType();

        if (material == Material.ARROW) {
            new MainSelectorPanel(playerMenuUtility, plugin).open();
        }
        else if (name.equalsIgnoreCase(Color.translate("&8┃ &chub-1"))) {
            plugin.getLobbyFunction().lobbyEnter(player);
        }
    }

    private String getPopulation() {
        final List<Player> players = plugin.getLobby().getPlayers();

        if (players.size() <= 5)
            return Color.translate("&aFaible");
        else if (players.size() <= 10)
            return Color.translate("&eMoyenne");
        return Color.translate("&cElevée");
    }

    private int getRank(Rank rank) {
        final List<EvonyaPlayer> admin = new ArrayList<>();

        plugin.getLobby().getPlayers().stream().map(PlayerManager::wrapEvonyaPlayer).forEach(player -> {

            if (player.getPlayerRank() == rank) {
                admin.add(player);
            }
        });
        return admin.size();
    }
}
