package me.kubbidev.evonyacore.menu.panel;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.menu.MenuBorder;
import me.kubbidev.evonyacore.utils.HeadType;
import me.kubbidev.evonyacore.menu.Menu;
import me.kubbidev.evonyacore.menu.PlayerMenuUtility;
import me.kubbidev.evonyacore.players.EvonyaStatistic;
import me.kubbidev.evonyacore.utils.Utils;
import me.kubbidev.evonyacore.utils.Head;
import me.kubbidev.nexuspowered.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.Arrays;

public class GameStatsPanel extends Menu {

    private final DecimalFormat decimalFormat;

    private final EvonyaStatistic statistic;

    public GameStatsPanel(PlayerMenuUtility playerMenuUtility, EvonyaPlugin plugin) {
        super(playerMenuUtility, plugin);

        this.decimalFormat = new DecimalFormat("#.##");
        this.statistic = playerMenuUtility.getOwner().getEvonyaStatistic();
    }

    @Override
    public String getMenuName() {
        return "&f(&c!&f) &cStatistiques";
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
        return ItemStackBuilder.of(Material.STAINED_GLASS_PANE).data(7).build();
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(13, new Head("&8┃ &cStatistiques Globales").setHeadType(HeadType.TANJIRO).setLore(Arrays.asList(
                "",
                "&8•&f Kills &7»&a " + statistic.getKills(),
                "&8•&f Morts &7»&a " + statistic.getDeaths(),
                "&8•&f Ratio K/M &7»&a " + decimalFormat.format(ratio(statistic.getKills(), statistic.getDeaths())) + "%",
                "",
                "&8•&f Nombre de victoires &7»&c " + statistic.getWins(),
                "&8•&f Nombre de parties &7»&c " + statistic.getGames(),
                "&8•&f Ratio de victoire &7»&c " + decimalFormat.format((ratio(statistic.getWins(), statistic.getGames())) * 100) + "%",
                "",
                "&8•&f Dégâts infligés &7»&6 " + decimalFormat.format(statistic.getDmgInflicted()),
                "&8•&f Dégâts reçus &7»&6 " + decimalFormat.format(statistic.getDmgReceived()),
                "",
                "&8•&f Temps passé à jouer &7»&c " + Utils.getFormattedTime(statistic.getTimePlayed()),
                ""
        )).parseItem());
    }

    @Override
    public void updateMenu() {
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
    }

    private double ratio(float a, float b) {
        if (b == 0)
            return a;
        return (a / b);
    }
}
