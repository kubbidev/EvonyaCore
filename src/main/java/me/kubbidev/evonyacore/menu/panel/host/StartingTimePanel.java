package me.kubbidev.evonyacore.menu.panel.host;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.game.core.GameInstance;
import me.kubbidev.evonyacore.menu.Menu;
import me.kubbidev.evonyacore.menu.MenuBorder;
import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.evonyacore.utils.HeadType;
import me.kubbidev.evonyacore.menu.PlayerMenuUtility;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
import me.kubbidev.evonyacore.utils.Item;
import me.kubbidev.evonyacore.utils.Utils;
import me.kubbidev.evonyacore.utils.Head;
import me.kubbidev.nexuspowered.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StartingTimePanel extends Menu {

    public StartingTimePanel(PlayerMenuUtility playerMenuUtility, EvonyaPlugin plugin) {
        super(playerMenuUtility, plugin);
    }

    @Override
    public String getMenuName()
    {
        return "&f(&c!&f) &cConfiguration";
    }

    @Override
    public int getRows()
    {
        return 6;
    }

    @Override
    public boolean isUpdated()
    {
        return false;
    }

    @Override
    public long getTicksUpdate()
    {
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
        final GameInstance gameInstance = playerMenuUtility.getOwner().getGameInstance();

        inventory.setItem(29, new Item(Material.EYE_OF_ENDER)
                .setName("&8┃&6 Partie ouverte")
                .setLore(Collections.singletonList("  &8•&r " + Utils.formatBoolean(gameInstance.isOpen())))
                .parseItem());
        inventory.setItem(30, new Item(Material.SIGN)
                .setName("&8┃&6 Statistiques")
                .setLore(Collections.singletonList("  &8•&r " + Utils.formatBoolean(gameInstance.isStatistics())))
                .parseItem());
        inventory.setItem(31, new Item(Material.FEATHER)
                .setName("&8┃&6 Spectateurs")
                .setLore(Collections.singletonList("  &8•&r " + Utils.formatBoolean(gameInstance.isSpectate())))
                .parseItem());
        inventory.setItem(32, new Item(Material.NAME_TAG)
                .setName("&8┃&6 Prefix des équipes")
                .setLore(Collections.singletonList("  &8•&r " + Utils.formatBoolean(gameInstance.isTeamPrefix())))
                .parseItem());


        //////////////////////////////

        inventory.setItem(20, new Item(Material.REDSTONE_TORCH_ON)
                .setName("&8┃&6 Slots")
                .setLore(Collections.singletonList("  &8•&a " + gameInstance.getSlots()))
                .parseItem());
        inventory.setItem(21, new Item(Material.WATCH)
                .setName("&8┃&6 Temps maximal de jeu")
                .setLore(Collections.singletonList("  &8•&a " + Utils.getFormattedTime(gameInstance.getMaxGameTime())))
                .parseItem());
        inventory.setItem(22, new Item(Material.REDSTONE_COMPARATOR)
                .setName("&8┃&6 Temps d'attente")
                .setLore(Collections.singletonList("  &8•&a " + Utils.getFormattedTime(gameInstance.getStartingTime())))
                .addEnchantments(Enchantment.DURABILITY, 1)
                .addItemFlags(ItemFlag.HIDE_ENCHANTS)
                .parseItem());

        //////////////////////////////

        inventory.setItem(49, new Item(Material.REDSTONE_COMPARATOR)
                .setName("&8┃&6 Temps d'attente")
                .setLore(Collections.singletonList("  &8•&a " + Utils.getFormattedTime(gameInstance.getStartingTime())))
                .addEnchantments(Enchantment.DURABILITY, 1)
                .addItemFlags(ItemFlag.HIDE_ENCHANTS)
                .parseItem());
        inventory.setItem(47, new Head("&cEnlever 2 secondes").setHeadType(HeadType.RED_MINUS).parseItem());
        inventory.setItem(48, new Head("&eEnlever 1 seconde").setHeadType(HeadType.YELLOW_MINUS).parseItem());
        inventory.setItem(50, new Head("&eAjouter 1 seconde").setHeadType(HeadType.YELLOW_PLUS).parseItem());
        inventory.setItem(51, new Head("&cAjouter 2 secondes").setHeadType(HeadType.RED_PLUS).parseItem());

        inventory.setItem(4, new Item(Material.SLIME_BALL).setName("&8┃&a Démarrer la partie").parseItem());

        final List<String> lore = Arrays.asList(
                "",
                "  &8┃&f Configurer cet onglet :",
                "  &8┃&c clic-droit&f ou &cclic-gauche",
                ""
        );
        inventory.setItem(18, new Item(Material.GOLD_HELMET).setName("&8┃&c Co-host").setLore(lore).addItemFlags(ItemFlag.HIDE_ATTRIBUTES).parseItem());
        inventory.setItem(27, new Item(Material.WRITTEN_BOOK).setName("&8┃&c Whitelist").setLore(lore).parseItem());

        inventory.setItem(26, new Item(Material.BLAZE_POWDER).setName("&8┃&c Demon Slayer").setLore(lore).parseItem());
        inventory.setItem(35, new Item(Material.BEACON).setName("&8┃&c Scénarios").setLore(lore).parseItem());
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
        final GameInstance gameInstance = player.getGameInstance();

        final boolean bool;
        final String message;

        if (material == Material.STAINED_GLASS_PANE)
            return;

        switch (material) {
            case EYE_OF_ENDER:
                bool = !gameInstance.isOpen();
                message = Utils.formatBoolean(bool);

                player.sendMessage(EvonyaPlugin.PREFIX + "Partie ouverte &8» " + message + "&f.");
                gameInstance.setOpen(bool);
                break;
            case SIGN:
                bool = !gameInstance.isStatistics();
                message = Utils.formatBoolean(bool);

                player.sendMessage(EvonyaPlugin.PREFIX + "Statistiques &8» " + message + "&f.");
                gameInstance.setStatistics(bool);
                break;
            case FEATHER:
                bool = !gameInstance.isSpectate();
                message = Utils.formatBoolean(bool);

                player.sendMessage(EvonyaPlugin.PREFIX + "Spectateurs &8» " + message + "&f.");
                gameInstance.setSpectate(bool);
                break;
            case NAME_TAG:
                bool = !gameInstance.isTeamPrefix();
                message = Utils.formatBoolean(bool);

                player.sendMessage(EvonyaPlugin.PREFIX + "Prefix des équipes &8» " + message + "&f.");
                gameInstance.setTeamPrefix(bool);
                break;
            case REDSTONE_TORCH_ON:
                new SlotsPanel(playerMenuUtility, plugin).open();
                return;
            case WATCH:
                new MaxGameTimeMenu(playerMenuUtility, plugin).open();
                return;
            case SLIME_BALL:
                if (gameInstance.isStarting())
                    return;

                player.getWorldPlayers().forEach(player1 -> player1.sendMessage(EvonyaPlugin.PREFIX + "Démarrage..."));
                gameInstance.startTimer();
                return;
            case GOLD_HELMET:
                new HostPanel(playerMenuUtility, plugin).open();
                return;
            case WRITTEN_BOOK:
                new WhitelistPanel(playerMenuUtility, plugin).open();
                return;
            case BLAZE_POWDER:
                new GameRolePanel(playerMenuUtility, plugin).open();
                return;
            case BEACON:
                new ScenarioPanel(playerMenuUtility, plugin).open();
                return;
            case SKULL_ITEM:
                final long time = gameInstance.getStartingTime();
                long finalTime = time;

                if (name.contains("Ajouter")) {
                    if (name.contains("1"))
                        finalTime = (time + 1);
                    else finalTime = (time + 2);
                }
                else if (name.contains("Enlever")) {
                    if (name.contains("1"))
                        finalTime = (time - 1);
                    else finalTime = (time - 2);
                }
                gameInstance.setStartingTime(finalTime);
                player.sendMessage(EvonyaPlugin.PREFIX + "Temps d'attente &8»&c " + Utils.getFormattedTime(finalTime) + "&f.");
                break;
        }
        super.open();
    }
}
