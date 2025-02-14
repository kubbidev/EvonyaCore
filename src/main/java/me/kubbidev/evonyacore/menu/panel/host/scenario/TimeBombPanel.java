package me.kubbidev.evonyacore.menu.panel.host.scenario;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.game.core.scenario.Scenario;
import me.kubbidev.evonyacore.game.core.scenario.Settings;
import me.kubbidev.evonyacore.menu.MenuBorder;
import me.kubbidev.evonyacore.menu.PlayerMenuUtility;
import me.kubbidev.evonyacore.menu.ScenarioMenu;
import me.kubbidev.evonyacore.menu.panel.host.RegisteredScenarioPanel;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.evonyacore.utils.*;
import me.kubbidev.nexuspowered.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class TimeBombPanel extends ScenarioMenu {

    public TimeBombPanel(PlayerMenuUtility playerMenuUtility, EvonyaPlugin plugin, Scenario scenario) {
        super(playerMenuUtility, plugin, scenario);
    }

    @Override
    public String getMenuName() {
        return "&f(&c!&f) Configuration";
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
        return MenuBorder.LINE;
    }

    @Override
    public ItemStack getBorderMaterial() {
        return ItemStackBuilder.of(Material.STAINED_GLASS_PANE).data(7).build();
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(26, new Item(Material.ARROW).setName("&8┃&f Revenir en &carrière").parseItem());

        inventory.setItem(13, new Item(Material.TNT)
                .setName("&8┃&6 " + scenario.getInfo().getName())
                .setLore(Collections.singletonList("  &8•&a " + Utils.getFormattedTime((long) scenario.getSettings().getValue())))
                .addEnchantments(Enchantment.DURABILITY, 1)
                .addItemFlags(ItemFlag.HIDE_ENCHANTS)
                .parseItem());
        inventory.setItem(11, new Head("&cEnlever 2 secondes").setHeadType(HeadType.RED_MINUS).parseItem());
        inventory.setItem(12, new Head("&eEnlever 1 seconde").setHeadType(HeadType.YELLOW_MINUS).parseItem());
        inventory.setItem(14, new Head("&eAjouter 1 seconde").setHeadType(HeadType.YELLOW_PLUS).parseItem());
        inventory.setItem(15, new Head("&cAjouter 2 secondes").setHeadType(HeadType.RED_PLUS).parseItem());
    }

    @Override
    public void updateMenu() {

    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        final ItemStack itemStack = event.getCurrentItem();
        final Material material = itemStack.getType();

        final String name = itemStack.getItemMeta().getDisplayName();
        final EvonyaPlayer player = PlayerManager.wrapEvonyaPlayer((Player) event.getWhoClicked());
        final Settings settings = scenario.getSettings();

        if (material == Material.STAINED_GLASS_PANE)
            return;

        if (name.equalsIgnoreCase(Color.translate("&8┃&f Revenir en &carrière"))) {
            new RegisteredScenarioPanel(playerMenuUtility, plugin).open();
        }
        else if (material == Material.SKULL_ITEM) {
            final long delay = (long) settings.getValue();
            long finalDelay = delay;

            if (name.contains("Ajouter")) {
                if (name.contains("1"))
                    finalDelay = (delay + 1);
                else finalDelay = (delay + 2);
            }
            else if (name.contains("Enlever")) {
                if (name.contains("1"))
                    finalDelay = (delay - 1);
                else finalDelay = (delay - 2);
            }
            settings.setValue(finalDelay);
            player.sendMessage(EvonyaPlugin.PREFIX + scenario.getInfo().getName() + " &8»&c " + Utils.getFormattedTime(finalDelay) + "&f.");
            super.open();
        }
    }
}
