package me.kubbidev.evonyacore.menu.panel.host;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.game.core.ScenarioManager;
import me.kubbidev.evonyacore.game.core.scenario.Scenario;
import me.kubbidev.evonyacore.menu.MenuBorder;
import me.kubbidev.evonyacore.menu.PaginatedMenu;
import me.kubbidev.evonyacore.menu.PlayerMenuUtility;
import me.kubbidev.evonyacore.menu.ScenarioMenu;
import me.kubbidev.evonyacore.players.EPlayer;
import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.evonyacore.utils.Item;
import me.kubbidev.nexuspowered.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

public class RegisteredScenarioPanel extends PaginatedMenu {

    public RegisteredScenarioPanel(PlayerMenuUtility playerMenuUtility, EvonyaPlugin plugin) {
        super(playerMenuUtility, plugin);
    }

    @Override
    public String getMenuName() {
        return "&f(&c!&f) Scénarios activés";
    }

    @Override
    public int getRows() {
        return 4;
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

    private ScenarioManager scenarioManager;
    private List<Scenario> registeredScenarios;

    @Override
    public void setMenuItems() {
        scenarios.clear();
        scenarioManager = playerMenuUtility.getOwner().getGameInstance().getScenarioManager();
        registeredScenarios = scenarioManager.getRegisteredScenarios();

        final int actualPage = (page + 1);
        final int scenarioSize = registeredScenarios.size();
        final int maxItems = super.getMaxItemsPerPage();

        final int division = (scenarioSize / maxItems);

        int maxPage = division + 1;

        if ((scenarioSize % maxItems == 0) && scenarioSize != 0)
            maxPage--;

        inventory.setItem(35, new Item(Material.ARROW).setName("&8┃&f Revenir en &carrière").parseItem());
        inventory.setItem(31, new Item(Material.PAPER).setName("&8┃&f Page &c" + actualPage + "&f/&c" + maxPage).setLore(Arrays.asList(
                "",
                "  &8┃&c Clic-Gauche&f pour aller à la page suivante.",
                "  &8┃&c Clic-Droit&f pour aller à la page précédente.",
                ""
        )).parseItem());

        for (int i = 0; i < maxItems; i++) {
            index = maxItems * page + i;

            if (index >= scenarioSize)
                break;

            final Scenario scenario = registeredScenarios.get(index);
            final Material material = scenario.getMaterial();

            final List<String> lore = scenario.getInfo().getDescription().stream().map(str -> " &8•&7 " + str).collect(Collectors.toList());
            lore.add("");
            lore.add("  &8»&c Clic-Gauche&f pour configurer.");
            lore.add("  &8»&c Clic-Droit&f pour désactiver.");
            lore.add("");
            final ItemStack itemStack = new Item(material)
                    .setName("&8┃&6 " + scenario.getInfo().getName())
                    .setLore(lore)
                    .addEnchantments(Enchantment.DURABILITY, 1)
                    .addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS)
                    .parseItem();

            final ItemMeta itemMeta = itemStack.getItemMeta();
            scenarios.put(itemMeta.getDisplayName(), scenario.getKey());

            inventory.setItem(getIndex(), itemStack);
        }
    }

    private final Map<String, String> scenarios = new HashMap<>();

    @Override
    public void updateMenu() {

    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        final EPlayer player = PlayerManager.wrapPlayer((Player) event.getWhoClicked());
        final ItemStack itemStack = event.getCurrentItem();
        final ClickType clickType = event.getClick();
        final String name = itemStack.getItemMeta().getDisplayName();
        final int rawSlot = event.getRawSlot();

        if (itemStack.getType() == Material.STAINED_GLASS_PANE)
            return;

        if (name.equalsIgnoreCase(Color.translate("&8┃&f Revenir en &carrière"))) {
            new ScenarioPanel(playerMenuUtility, plugin).open();
        }
        else if (rawSlot == 49) {
            switch (clickType) {
                case LEFT:
                    if ((index + 1) < registeredScenarios.size()) {
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
        else {
            final String scenarioKey = scenarios.get(itemStack.getItemMeta().getDisplayName());
            final Optional<Scenario> scenarioOptional = scenarioManager.getScenarioByKey(scenarioKey);

            if (!scenarioOptional.isPresent())
                return;

            final Scenario scenario = scenarioOptional.get();
            switch (clickType) {
                case LEFT:
                    try {
                        final Class<? extends ScenarioMenu> optionsPanel = scenario.getOptionsPanel();
                        final ScenarioMenu menu = optionsPanel.getDeclaredConstructor(PlayerMenuUtility.class, EvonyaPlugin.class, Scenario.class).newInstance(playerMenuUtility, plugin, scenario);

                        menu.open();

                    } catch (Exception e) {
                        player.sendMessage(EvonyaPlugin.PREFIX + "Ce &6scénario&f n'est pas &cconfigurable&f.");
                        return;
                    }
                    break;
                case RIGHT:
                    scenarioManager.unRegisterScenario(scenario);

                    player.sendMessage(EvonyaPlugin.PREFIX + "Scénarios &8»&6 " + scenario.getInfo().getName() + "&f viens d'être &cdésactiver&f." );
                    super.open();
                    break;
            }
        }
    }
}
