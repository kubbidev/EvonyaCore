package me.kubbidev.evonyacore.menu.panel.host;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.menu.MenuBorder;
import me.kubbidev.evonyacore.menu.PaginatedMenu;
import me.kubbidev.evonyacore.menu.PlayerMenuUtility;
import me.kubbidev.evonyacore.players.Role;
import me.kubbidev.evonyacore.utils.Item;
import me.kubbidev.nexuspowered.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class GameRolePanel extends PaginatedMenu {

    public GameRolePanel(PlayerMenuUtility playerMenuUtility, EvonyaPlugin plugin) {
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

    private List<Role> activeRoles;
    private List<Role> roles;

    @Override
    public void setMenuItems() {
        rolesd.clear();
        activeRoles = playerMenuUtility.getOwner().getGameInstance().getActiveRoles();
        roles = Arrays.asList(Role.values());

        final int actualPage = (page + 1);
        final int gameRoleSize = roles.size();
        final int maxItems = super.getMaxItemsPerPage();

        final int division = (gameRoleSize / maxItems);

        int maxPage = division + 1;

        if ((gameRoleSize % maxItems == 0) && gameRoleSize != 0)
            maxPage--;

        inventory.setItem(53, new Item(Material.ARROW).setName("&8┃&f Revenir en &carrière").parseItem());
        inventory.setItem(49, new Item(Material.PAPER).setName("&8┃&f Page &c" + actualPage + "&f/&c" + maxPage).setLore(Arrays.asList(
                "",
                "  &8┃&c Clic-Gauche&f pour aller à la page suivante.",
                "  &8┃&c Clic-Droit&f pour aller à la page précédente.",
                ""
        )).parseItem());

        for (int i = 0; i < maxItems; i++) {
            index = maxItems * page + i;

            if (index >= gameRoleSize)
                break;

            final Role role = roles.get(index);
            final Material material = role.getMaterial();

            final int amount = getTimes(activeRoles, role);

            final String name = "&8┃&6 " + role.getName() + getTimeToString(activeRoles, role);
            final Item item = new Item(material)
                    .setName(name)
                    .setAmount(amount == 0 ? 1 : amount)
                    .addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS);

            if (amount == 0) {
                item.addEnchantments(Enchantment.DURABILITY, 1);
            }
            else {
                final List<String> lore = new ArrayList<>();
                lore.add("");
                lore.add("  &8┃&c Clic-Gauche&f pour ajouter 1.");
                lore.add("  &8┃&c Clic-Droit&f pour retirer 1.");
                lore.add("");
                item.setLore(lore);
            }


            final ItemStack itemStack = item.parseItem();
            final ItemMeta itemMeta = itemStack.getItemMeta();
            rolesd.put(itemMeta.getDisplayName(), role.name());

            inventory.setItem(getIndex(), itemStack);
        }
    }

    private Map<String, String> rolesd = new HashMap<>();

    private int getTimes(List<Role> activeRoles, Role role) {
        return (int) activeRoles.stream().filter(roles -> roles.equals(role)).count();
    }

    private String getTimeToString(List<Role> activeRoles, Role role) {
        final int times = getTimes(activeRoles, role);
        if (times == 0 )
            return " &f(&c0&f)";
        return " &f(" + times + ")";
    }

    @Override
    public void updateMenu() {
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        final ItemStack itemStack = event.getCurrentItem();
        final ClickType clickType = event.getClick();
        final String name = itemStack.getItemMeta().getDisplayName();
        final int rawSlot = event.getRawSlot();

        if (itemStack.getType() == Material.STAINED_GLASS_PANE)
            return;

        if (name.equalsIgnoreCase(Color.translate("&8┃&f Revenir en &carrière"))) {
            new SlotsPanel(playerMenuUtility, plugin).open();
        }
        else if (rawSlot == 49) {
            switch (clickType) {
                case LEFT:
                    if ((index + 1) < roles.size()) {
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
            final Role role = Role.valueOf(rolesd.get(itemStack.getItemMeta().getDisplayName()));

            switch (clickType) {
                case LEFT:
                    activeRoles.add(role);
                    super.open();
                    break;
                case RIGHT:
                    if (getTimes(activeRoles, role) > 0) {
                        activeRoles.remove(role);
                    }
                    super.open();
                    break;
            }
        }
    }
}
